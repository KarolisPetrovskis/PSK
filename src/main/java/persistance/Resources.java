package persistance;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.SynchronizationType;
import mybatis.ClientMapper;
import mybatis.TrainerMapper;
import mybatis.GymMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;

@ApplicationScoped
public class Resources {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Produces
    @Default
    @RequestScoped
    private EntityManager createJTAEntityManager() {
        return emf.createEntityManager(SynchronizationType.SYNCHRONIZED);
    }

    private void closeDefaultEntityManager(@Disposes @Default EntityManager em) {
        em.close();
    }

    @Produces
    @ApplicationScoped
    public SqlSessionFactory createSqlSessionFactory() {
        try {
            return new SqlSessionFactoryBuilder().build(org.apache.ibatis.io.Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            throw new RuntimeException("MyBatis configuration file not found", e);
        }
    }

    @Produces
    @RequestScoped
    public SqlSession createSqlSession(SqlSessionFactory sqlSessionFactory) {
        return sqlSessionFactory.openSession();
    }

    public void closeSqlSession(@Disposes SqlSession sqlSession) {
        sqlSession.close();
    }

    @Produces
    @RequestScoped
    public GymMapper gymMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(GymMapper.class);
    }

    @Produces
    @RequestScoped
    public TrainerMapper trainerMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(TrainerMapper.class);
    }

    @Produces
    @RequestScoped
    public ClientMapper clientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ClientMapper.class);
    }
}
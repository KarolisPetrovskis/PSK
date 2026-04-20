package mybatis;

import mybatis.model.Client;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClientMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Client record);
    Client selectByPrimaryKey(Long id);
    Client selectByPrimaryKeyWithTrainers(Long id);
    List<Client> selectAll();
    List<Client> selectAllWithTrainers();
    int updateByPrimaryKey(Client record);
    int countClientTrainer(@Param("clientId") Long clientId, @Param("trainerId") Long trainerId);
    int insertClientTrainer(@Param("clientId") Long clientId, @Param("trainerId") Long trainerId);
    int deleteClientTrainer(@Param("clientId") Long clientId, @Param("trainerId") Long trainerId);
}
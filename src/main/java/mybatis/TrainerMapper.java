package mybatis;

import mybatis.model.Trainer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TrainerMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Trainer record);
    Trainer selectByPrimaryKey(Long id);
    List<Trainer> selectAll();
    int updateByPrimaryKey(Trainer record);
    List<Trainer> selectByGym(Long gymId);
    List<Trainer> findByGymId(Long gymId);
    List<Trainer> findByNameAndGym(@Param("name") String name, @Param("gymId") Long gymId);
}
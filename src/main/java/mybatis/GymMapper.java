package mybatis;

import mybatis.model.Gym;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GymMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Gym record);
    Gym selectByPrimaryKey(Long id);
    List<Gym> selectAll();
    int updateByPrimaryKey(Gym record);
    List<Gym> selectAllWithTrainers();
}
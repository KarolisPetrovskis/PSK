package mybatis;

import entities.Gym;
import entities.Trainer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GymMapper {
    int deleteByPrimaryKey(Long id);
    int insert(Gym record);
    Gym selectByPrimaryKey(Long id);
    List<Gym> selectAll();
    int updateByPrimaryKey(Gym record);
}
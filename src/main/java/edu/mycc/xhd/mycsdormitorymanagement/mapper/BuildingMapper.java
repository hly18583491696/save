package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 宿舍楼Mapper接口
 */
@Mapper
public interface BuildingMapper extends BaseMapper<Building> {
    
    /**
     * 根据楼栋编号查询
     */
    @Select("SELECT * FROM dorm_building WHERE building_code = #{buildingCode} AND deleted = 0")
    Building findByBuildingCode(String buildingCode);
    
    /**
     * 根据楼栋类型查询
     */
    @Select("SELECT * FROM dorm_building WHERE building_type = #{buildingType} AND deleted = 0")
    List<Building> findByBuildingType(String buildingType);
    
    /**
     * 根据状态查询
     */
    @Select("SELECT * FROM dorm_building WHERE status = #{status} AND deleted = 0")
    List<Building> findByStatus(Integer status);
}
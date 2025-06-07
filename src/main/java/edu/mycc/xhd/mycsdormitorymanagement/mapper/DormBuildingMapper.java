package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormBuilding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 宿舍楼数据访问层
 */
@Mapper
public interface DormBuildingMapper extends BaseMapper<DormBuilding> {
    
    /**
     * 根据楼栋编号查找楼栋
     */
    @Select("SELECT * FROM dorm_building WHERE building_code = #{buildingCode} AND deleted = 0")
    DormBuilding findByBuildingCode(String buildingCode);
    
    /**
     * 根据楼栋类型查找楼栋
     */
    @Select("SELECT * FROM dorm_building WHERE building_type = #{buildingType} AND deleted = 0")
    List<DormBuilding> findByBuildingType(String buildingType);
    
    /**
     * 查询所有有效楼栋
     */
    @Select("SELECT * FROM dorm_building WHERE deleted = 0 ORDER BY building_code")
    List<DormBuilding> findAllActive();
}
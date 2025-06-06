package edu.mycc.xhd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.entity.Building;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 宿舍楼Mapper接口
 * 
 * @author xhd
 */
@Mapper
public interface BuildingMapper extends BaseMapper<Building> {
    
    /**
     * 根据楼栋编号查询楼栋
     * 
     * @param buildingCode 楼栋编号
     * @return 楼栋信息
     */
    Building selectByBuildingCode(@Param("buildingCode") String buildingCode);
    
    /**
     * 检查楼栋编号是否存在
     * 
     * @param buildingCode 楼栋编号
     * @param id 楼栋ID（排除自己）
     * @return 数量
     */
    int checkBuildingCodeExists(@Param("buildingCode") String buildingCode, @Param("id") Long id);
}
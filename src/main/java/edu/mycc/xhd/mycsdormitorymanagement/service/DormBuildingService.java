package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormBuilding;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormBuildingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宿舍楼服务类
 */
@Service
public class DormBuildingService {
    
    @Autowired
    private DormBuildingMapper dormBuildingMapper;
    
    /**
     * 获取所有楼栋
     */
    public List<DormBuilding> getAllBuildings() {
        return dormBuildingMapper.findAllActive();
    }
    
    /**
     * 根据ID获取楼栋
     */
    public DormBuilding getBuildingById(Long id) {
        return dormBuildingMapper.selectById(id);
    }
    
    /**
     * 根据楼栋编号获取楼栋
     */
    public DormBuilding getBuildingByCode(String buildingCode) {
        return dormBuildingMapper.findByBuildingCode(buildingCode);
    }
    
    /**
     * 根据楼栋类型获取楼栋
     */
    public List<DormBuilding> getBuildingsByType(String buildingType) {
        return dormBuildingMapper.findByBuildingType(buildingType);
    }
    
    /**
     * 添加楼栋
     */
    public boolean addBuilding(DormBuilding building) {
        // 检查楼栋编号是否已存在
        DormBuilding existing = dormBuildingMapper.findByBuildingCode(building.getBuildingCode());
        if (existing != null) {
            throw new RuntimeException("楼栋编号已存在");
        }
        
        building.setCreateTime(LocalDateTime.now());
        building.setUpdateTime(LocalDateTime.now());
        building.setDeleted(0);
        
        return dormBuildingMapper.insert(building) > 0;
    }
    
    /**
     * 更新楼栋
     */
    public boolean updateBuilding(DormBuilding building) {
        building.setUpdateTime(LocalDateTime.now());
        return dormBuildingMapper.updateById(building) > 0;
    }
    
    /**
     * 删除楼栋（逻辑删除）
     */
    public boolean deleteBuilding(Long id) {
        DormBuilding building = new DormBuilding();
        building.setId(id);
        building.setDeleted(1);
        building.setUpdateTime(LocalDateTime.now());
        return dormBuildingMapper.updateById(building) > 0;
    }
}
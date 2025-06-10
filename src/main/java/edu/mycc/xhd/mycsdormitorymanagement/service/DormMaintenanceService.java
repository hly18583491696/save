package edu.mycc.xhd.mycsdormitorymanagement.service;

import edu.mycc.xhd.mycsdormitorymanagement.entity.DormMaintenance;
import edu.mycc.xhd.mycsdormitorymanagement.mapper.DormMaintenanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 维修管理服务类
 */
@Service
public class DormMaintenanceService {
    
    @Autowired
    private DormMaintenanceMapper maintenanceMapper;
    
    /**
     * 获取所有维修记录
     */
    public List<DormMaintenance> getAllMaintenances() {
        return maintenanceMapper.findAllActive();
    }
    
    /**
     * 根据ID获取维修记录
     */
    public DormMaintenance getMaintenanceById(Long id) {
        return maintenanceMapper.selectById(id);
    }
    
    /**
     * 根据申请单号获取维修记录
     */
    public DormMaintenance getMaintenanceByRequestNumber(String requestNumber) {
        return maintenanceMapper.findByRequestNumber(requestNumber);
    }
    
    /**
     * 根据申请人ID获取维修记录
     */
    public List<DormMaintenance> getMaintenancesByApplicantId(Long applicantId) {
        return maintenanceMapper.findByApplicantId(applicantId);
    }
    
    /**
     * 根据房间ID获取维修记录
     */
    public List<DormMaintenance> getMaintenancesByRoomId(Long roomId) {
        return maintenanceMapper.findByRoomId(roomId);
    }
    
    /**
     * 根据状态获取维修记录
     */
    public List<DormMaintenance> getMaintenancesByStatus(String status) {
        return maintenanceMapper.findByStatus(status);
    }
    
    /**
     * 根据维修类型获取维修记录
     */
    public List<DormMaintenance> getMaintenancesByType(String maintenanceType) {
        return maintenanceMapper.findByMaintenanceType(maintenanceType);
    }
    
    /**
     * 根据紧急程度获取维修记录
     */
    public List<DormMaintenance> getMaintenancesByUrgencyLevel(String urgencyLevel) {
        return maintenanceMapper.findByUrgencyLevel(urgencyLevel);
    }
    
    /**
     * 根据分配人获取维修记录
     */
    public List<DormMaintenance> getMaintenancesByAssignedTo(String assignedTo) {
        return maintenanceMapper.findByAssignedTo(assignedTo);
    }
    
    /**
     * 根据楼栋ID获取维修记录
     */
    public List<DormMaintenance> getMaintenancesByBuildingId(Long buildingId) {
        return maintenanceMapper.findByBuildingId(buildingId);
    }
    
    /**
     * 创建维修申请
     */
    @Transactional
    public DormMaintenance createMaintenance(DormMaintenance maintenance) {
        // 生成申请单号
        String requestNumber = generateRequestNumber();
        maintenance.setRequestNumber(requestNumber);
        
        // 设置默认状态
        if (maintenance.getStatus() == null) {
            maintenance.setStatus("PENDING");
        }
        
        // 设置默认紧急程度
        if (maintenance.getUrgencyLevel() == null) {
            maintenance.setUrgencyLevel("NORMAL");
        }
        
        // 设置创建时间
        maintenance.setCreateTime(LocalDateTime.now());
        maintenance.setUpdateTime(LocalDateTime.now());
        maintenance.setDeleted(0);
        
        maintenanceMapper.insert(maintenance);
        return maintenance;
    }
    
    /**
     * 更新维修记录
     */
    @Transactional
    public DormMaintenance updateMaintenance(DormMaintenance maintenance) {
        maintenance.setUpdateTime(LocalDateTime.now());
        maintenanceMapper.updateById(maintenance);
        return maintenance;
    }
    
    /**
     * 删除维修记录（逻辑删除）
     */
    @Transactional
    public boolean deleteMaintenance(Long id) {
        DormMaintenance maintenance = maintenanceMapper.selectById(id);
        if (maintenance != null) {
            maintenance.setDeleted(1);
            maintenance.setUpdateTime(LocalDateTime.now());
            maintenanceMapper.updateById(maintenance);
            return true;
        }
        return false;
    }
    
    /**
     * 物理删除维修记录
     */
    @Transactional
    public boolean deleteMaintenancePhysically(Long id) {
        return maintenanceMapper.deleteByIdPhysically(id) > 0;
    }
    
    /**
     * 处理维修申请（审核通过或拒绝）
     */
    @Transactional
    public boolean processMaintenance(Long id, String status, String processRemark) {
        DormMaintenance maintenance = maintenanceMapper.selectById(id);
        if (maintenance != null) {
            maintenance.setStatus(status);
            maintenance.setProcessRemark(processRemark);
            maintenance.setUpdateTime(LocalDateTime.now());
            maintenanceMapper.updateById(maintenance);
            return true;
        }
        return false;
    }
    
    /**
     * 更新维修状态
     */
    @Transactional
    public boolean updateMaintenanceStatus(Long id, String status) {
        return maintenanceMapper.updateStatus(id, status) > 0;
    }
    
    /**
     * 分配维修任务
     */
    @Transactional
    public boolean assignMaintenance(Long id, String assignedTo) {
        return maintenanceMapper.assignMaintenance(id, assignedTo) > 0;
    }
    
    /**
     * 完成维修任务
     */
    @Transactional
    public boolean completeMaintenance(Long id, BigDecimal actualCost) {
        return maintenanceMapper.completeMaintenance(id, actualCost) > 0;
    }
    
    /**
     * 添加反馈和评分
     */
    @Transactional
    public boolean addFeedback(Long id, String feedback, Integer rating) {
        DormMaintenance maintenance = maintenanceMapper.selectById(id);
        if (maintenance != null) {
            maintenance.setFeedback(feedback);
            maintenance.setRating(rating);
            maintenance.setUpdateTime(LocalDateTime.now());
            maintenanceMapper.updateById(maintenance);
            return true;
        }
        return false;
    }
    
    /**
     * 获取维修统计信息
     */
    public Map<String, Object> getMaintenanceStatistics() {
        List<Map<String, Object>> statusStats = maintenanceMapper.countByStatus();
        List<Map<String, Object>> typeStats = maintenanceMapper.countByMaintenanceType();
        
        return Map.of(
            "statusStatistics", statusStats,
            "typeStatistics", typeStats,
            "totalCount", getAllMaintenances().size()
        );
    }
    
    /**
     * 生成申请单号
     */
    private String generateRequestNumber() {
        String prefix = "MR";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }
    
    /**
     * 根据条件搜索维修记录
     */
    public List<DormMaintenance> searchMaintenances(String keyword, String status, String maintenanceType, String urgencyLevel) {
        // 这里可以实现复杂的搜索逻辑
        // 暂时返回所有记录，实际项目中可以使用MyBatis-Plus的条件构造器
        return getAllMaintenances();
    }
}
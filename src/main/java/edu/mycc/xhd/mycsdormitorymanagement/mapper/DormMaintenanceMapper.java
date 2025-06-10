package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.DormMaintenance;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 维修管理数据访问层
 */
@Mapper
public interface DormMaintenanceMapper extends BaseMapper<DormMaintenance> {
    
    /**
     * 根据申请单号查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE request_number = #{requestNumber} AND deleted = 0")
    DormMaintenance findByRequestNumber(String requestNumber);
    
    /**
     * 根据申请人ID查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE applicant_id = #{applicantId} AND deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findByApplicantId(Long applicantId);
    
    /**
     * 根据房间ID查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE room_id = #{roomId} AND deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findByRoomId(Long roomId);
    
    /**
     * 根据状态查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE status = #{status} AND deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findByStatus(String status);
    
    /**
     * 根据维修类型查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE maintenance_type = #{maintenanceType} AND deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findByMaintenanceType(String maintenanceType);
    
    /**
     * 根据紧急程度查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE urgency_level = #{urgencyLevel} AND deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findByUrgencyLevel(String urgencyLevel);
    
    /**
     * 根据分配人查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE assigned_to = #{assignedTo} AND deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findByAssignedTo(String assignedTo);
    
    /**
     * 查询所有有效的维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findAllActive();
    
    /**
     * 根据楼栋ID查找维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE building_id = #{buildingId} AND deleted = 0 ORDER BY create_time DESC")
    List<DormMaintenance> findByBuildingId(Long buildingId);
    
    /**
     * 更新维修状态
     */
    @Update("UPDATE dorm_maintenance SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(Long id, String status);
    
    /**
     * 分配维修任务
     */
    @Update("UPDATE dorm_maintenance SET assigned_to = #{assignedTo}, status = 'IN_PROGRESS', update_time = NOW() WHERE id = #{id}")
    int assignMaintenance(Long id, String assignedTo);
    
    /**
     * 完成维修任务
     */
    @Update("UPDATE dorm_maintenance SET status = 'COMPLETED', completed_time = NOW(), actual_cost = #{actualCost}, update_time = NOW() WHERE id = #{id}")
    int completeMaintenance(Long id, java.math.BigDecimal actualCost);
    
    /**
     * 统计各状态的维修记录数量
     */
    @Select("SELECT status, COUNT(*) as count FROM dorm_maintenance WHERE deleted = 0 GROUP BY status")
    List<java.util.Map<String, Object>> countByStatus();
    
    /**
     * 统计各维修类型的记录数量
     */
    @Select("SELECT maintenance_type, COUNT(*) as count FROM dorm_maintenance WHERE deleted = 0 GROUP BY maintenance_type")
    List<java.util.Map<String, Object>> countByMaintenanceType();
    
    /**
     * 物理删除维修记录
     */
    @Delete("DELETE FROM dorm_maintenance WHERE id = #{id}")
    int deleteByIdPhysically(Long id);
}
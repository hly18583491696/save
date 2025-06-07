package edu.mycc.xhd.mycsdormitorymanagement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.mycc.xhd.mycsdormitorymanagement.entity.Maintenance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 维修申请Mapper接口
 */
@Mapper
public interface MaintenanceMapper extends BaseMapper<Maintenance> {
    
    /**
     * 根据申请人ID查询维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE applicant_id = #{applicantId} AND deleted = 0")
    List<Maintenance> findByApplicantId(Long applicantId);
    
    /**
     * 根据房间ID查询维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE room_id = #{roomId} AND deleted = 0")
    List<Maintenance> findByRoomId(Long roomId);
    
    /**
     * 根据状态查询维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE status = #{status} AND deleted = 0")
    List<Maintenance> findByStatus(String status);
    
    /**
     * 根据维修人员ID查询维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE repairer_id = #{repairerId} AND deleted = 0")
    List<Maintenance> findByRepairerId(Long repairerId);
    
    /**
     * 根据维修类型查询维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE maintenance_type = #{maintenanceType} AND deleted = 0")
    List<Maintenance> findByMaintenanceType(String maintenanceType);
    
    /**
     * 根据紧急程度查询维修记录
     */
    @Select("SELECT * FROM dorm_maintenance WHERE urgency = #{urgency} AND deleted = 0")
    List<Maintenance> findByUrgency(String urgency);
}
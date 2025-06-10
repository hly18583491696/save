package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维修管理实体类
 */
@Data
@TableName("dorm_maintenance")
public class DormMaintenance {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("request_number")
    private String requestNumber;
    
    @TableField("applicant_id")
    private Long applicantId;
    
    @TableField("applicant_name")
    private String applicantName;
    
    @TableField("room_id")
    private Long roomId;
    
    @TableField("room_number")
    private String roomNumber;
    
    @TableField("building_id")
    private Long buildingId;
    
    @TableField("building_name")
    private String buildingName;
    
    @TableField("maintenance_type")
    private String maintenanceType;
    
    private String description;
    
    @TableField("urgency_level")
    private String urgencyLevel;
    
    private String status;
    
    @TableField("assigned_to")
    private String assignedTo;
    
    @TableField("estimated_cost")
    private BigDecimal estimatedCost;
    
    @TableField("actual_cost")
    private BigDecimal actualCost;
    
    @TableField("scheduled_time")
    private LocalDateTime scheduledTime;
    
    @TableField("completed_time")
    private LocalDateTime completedTime;
    
    private String feedback;
    
    private Integer rating;
    
    @TableField("process_remark")
    private String processRemark;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
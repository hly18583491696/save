package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访客记录实体类
 */
@Data
@TableName("dorm_visitor")
public class DormVisitor {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("visitor_name")
    private String visitorName;
    
    @TableField("id_card")
    private String idCard;
    
    private String phone;
    
    @TableField("visited_student_id")
    private Long visitedStudentId;
    
    @TableField("visited_student_name")
    private String visitedStudentName;
    
    @TableField("room_id")
    private Long roomId;
    
    @TableField("room_number")
    private String roomNumber;
    
    @TableField("building_id")
    private Long buildingId;
    
    @TableField("building_name")
    private String buildingName;
    
    @TableField("visit_purpose")
    private String visitPurpose;
    
    @TableField("visit_time")
    private LocalDateTime visitTime;
    
    @TableField("expected_leave_time")
    private LocalDateTime expectedLeaveTime;
    
    @TableField("actual_leave_time")
    private LocalDateTime actualLeaveTime;
    
    private String status;
    
    @TableField("guard_name")
    private String guardName;
    
    private String remarks;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
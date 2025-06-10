package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 住宿记录实体类
 */
@Data
@TableName("dorm_accommodation")
public class DormAccommodation {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("student_id")
    private Long studentId;
    
    @TableField("student_name")
    private String studentName;
    
    @TableField("student_number")
    private String studentNumber;
    
    @TableField("class_name")
    private String className;
    
    @TableField("id_card")
    private String idCard;
    
    @TableField("phone")
    private String phone;
    
    @TableField("email")
    private String email;
    
    @TableField("room_id")
    private Long roomId;
    
    @TableField("room_number")
    private String roomNumber;
    
    @TableField("building_id")
    private Long buildingId;
    
    @TableField("building_name")
    private String buildingName;
    
    @TableField("check_in_date")
    private LocalDate checkInDate;
    
    @TableField("check_out_date")
    private LocalDate checkOutDate;
    
    private String status;
    
    @TableField("bed_number")
    private String bedNumber;
    
    private String remarks;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted; // 0-未删除, 1-已删除
}
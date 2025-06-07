package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
    
    private Long studentId;
    
    private String studentName;
    
    private String studentNumber;
    
    private Long roomId;
    
    private String roomNumber;
    
    private Long buildingId;
    
    private String buildingName;
    
    private LocalDate checkInDate;
    
    private LocalDate checkOutDate;
    
    private String status;
    
    private String bedNumber;
    
    private String remarks;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
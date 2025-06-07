package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 宿舍房间实体类
 */
@Data
@TableName("dorm_room")
public class DormRoom {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String roomNumber;
    
    private Long buildingId;
    
    private Integer floor;
    
    private String roomType;
    
    private Integer capacity;
    
    private Integer currentCount;
    
    private String gender;
    
    private String status;
    
    private String facilities;
    
    private BigDecimal rent;
    
    private String description;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    
    @TableField("room_number")
    private String roomNumber;
    
    @TableField("building_id")
    private Long buildingId;
    
    private Integer floor;
    
    @TableField("room_type")
    private String roomType;
    
    private Integer capacity;
    
    @TableField("current_count")
    private Integer currentCount;
    
    private String gender;
    
    private String status;
    
    private String facilities;
    
    private BigDecimal rent;
    
    private String description;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
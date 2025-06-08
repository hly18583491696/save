package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍楼实体类
 */
@Data
@TableName("dorm_building")
public class DormBuilding {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("building_code")
    private String buildingCode;
    
    @TableField("building_name")
    private String buildingName;
    
    @TableField("building_type")
    private String buildingType;
    
    private Integer floors;
    
    @TableField("rooms_per_floor")
    private Integer roomsPerFloor;
    
    @TableField("manager_name")
    private String managerName;
    
    @TableField("manager_phone")
    private String managerPhone;
    
    private String description;
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
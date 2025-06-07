package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
    
    private String buildingCode;
    
    private String buildingName;
    
    private String buildingType;
    
    private Integer floors;
    
    private Integer roomsPerFloor;
    
    private String managerName;
    
    private String managerPhone;
    
    private String description;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
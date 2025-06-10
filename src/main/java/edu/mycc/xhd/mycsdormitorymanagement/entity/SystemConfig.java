package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统配置实体类
 */
@Data
@TableName("system_config")
public class SystemConfig {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("config_key")
    private String configKey;
    
    @TableField("config_value")
    private String configValue;
    
    @TableField("config_type")
    private String configType; // basic, dormitory, notification, security, backup
    
    @TableField("config_description")
    private String configDescription;
    
    @TableField("is_system")
    private Integer isSystem; // 1-系统配置(不可删除), 0-用户配置
    
    private Integer status; // 1-启用, 0-禁用
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
}
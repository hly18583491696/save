package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@Data
@TableName("student")
public class Student {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    @TableField("student_number")
    private String studentNumber;
    
    @TableField("student_name")
    private String studentName;
    
    @TableField("class_name")
    private String className;
    
    @TableField("id_card")
    private String idCard;
    
    private String phone;
    
    private String email;
    
    private Integer status; // 1-正常, 0-禁用
    
    @TableField("create_time")
    private LocalDateTime createTime;
    
    @TableField("update_time")
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField("deleted")
    private Integer deleted; // 0-未删除, 1-已删除
}
package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学生实体类
 */
@Data
@TableName("sys_user")
public class Student {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String realName;
    
    private String email;
    
    private String phone;
    
    private String gender;
    
    private String studentNumber;
    
    private String grade;
    
    private String major;
    
    private String className;
    
    private String idCard;
    
    private String address;
    
    private String avatar;
    
    private String status;
    
    private String remark;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private Integer deleted;
}
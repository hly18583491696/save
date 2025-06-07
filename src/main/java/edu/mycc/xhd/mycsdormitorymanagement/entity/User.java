package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
// import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
// @Schema(description = "用户信息")
public class User extends BaseEntity {
    
    // @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    // @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;
    
    // @Schema(description = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    private String realName;
    
    // @Schema(description = "学号/工号")
    private String studentId;
    
    // @Schema(description = "性别 0-女 1-男")
    private Integer gender;
    
    // @Schema(description = "手机号")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    // @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    // @Schema(description = "头像URL")
    private String avatar;
    
    // @Schema(description = "角色 ADMIN-管理员 STUDENT-学生 TEACHER-教师")
    private String role;
    
    // @Schema(description = "状态 0-禁用 1-启用")
    private Integer status;
    
    // @Schema(description = "学院")
    private String college;
    
    // @Schema(description = "专业")
    private String major;
    
    // @Schema(description = "班级")
    private String className;
    
    // @Schema(description = "备注")
    private String remark;
}
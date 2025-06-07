package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
// import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * 访客记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dorm_visitor")
// @Schema(description = "访客记录信息")
public class Visitor extends BaseEntity {
    
    // @Schema(description = "访客姓名")
    @NotBlank(message = "访客姓名不能为空")
    private String visitorName;
    
    // @Schema(description = "访客身份证号")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$", 
             message = "身份证号格式不正确")
    private String idCard;
    
    // @Schema(description = "访客电话")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    // @Schema(description = "被访问学生ID")
    @NotNull(message = "被访问学生ID不能为空")
    private Long studentId;
    
    // @Schema(description = "被访问学生姓名")
    private String studentName;
    
    // @Schema(description = "房间ID")
    @NotNull(message = "房间ID不能为空")
    private Long roomId;
    
    // @Schema(description = "房间号")
    private String roomNumber;
    
    // @Schema(description = "楼栋ID")
    private Long buildingId;
    
    // @Schema(description = "楼栋名称")
    private String buildingName;
    
    // @Schema(description = "访问目的")
    private String purpose;
    
    // @Schema(description = "与被访问人关系")
    private String relationship;
    
    // @Schema(description = "预约访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentTime;
    
    // @Schema(description = "实际进入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInTime;
    
    // @Schema(description = "离开时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkOutTime;
    
    // @Schema(description = "访问状态 PENDING-待审核 APPROVED-已批准 REJECTED-已拒绝 IN_VISIT-访问中 COMPLETED-已完成")
    private String status;
    
    // @Schema(description = "审核人ID")
    private Long reviewerId;
    
    // @Schema(description = "审核人姓名")
    private String reviewerName;
    
    // @Schema(description = "审核时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewTime;
    
    // @Schema(description = "审核意见")
    private String reviewComment;
    
    // @Schema(description = "登记人ID")
    private Long registrarId;
    
    // @Schema(description = "登记人姓名")
    private String registrarName;
    
    // @Schema(description = "备注")
    private String remark;
}
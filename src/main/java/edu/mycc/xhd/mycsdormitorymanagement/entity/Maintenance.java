package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
// import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * 维修申请实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dorm_maintenance")
// @Schema(description = "维修申请信息")
public class Maintenance extends BaseEntity {
    
    // @Schema(description = "申请单号")
    private String requestNumber;
    
    // @Schema(description = "申请人ID")
    @NotNull(message = "申请人ID不能为空")
    private Long applicantId;
    
    // @Schema(description = "申请人姓名")
    private String applicantName;
    
    // @Schema(description = "房间ID")
    @NotNull(message = "房间ID不能为空")
    private Long roomId;
    
    // @Schema(description = "房间号")
    private String roomNumber;
    
    // @Schema(description = "楼栋ID")
    private Long buildingId;
    
    // @Schema(description = "楼栋名称")
    private String buildingName;
    
    // @Schema(description = "维修类型 WATER-水电 FURNITURE-家具 NETWORK-网络 DOOR_WINDOW-门窗 OTHER-其他")
    private String maintenanceType;
    
    // @Schema(description = "问题描述")
    @NotBlank(message = "问题描述不能为空")
    private String description;
    
    // @Schema(description = "紧急程度 LOW-低 MEDIUM-中 HIGH-高 URGENT-紧急")
    private String urgency;
    
    // @Schema(description = "申请状态 PENDING-待处理 ASSIGNED-已分配 IN_PROGRESS-处理中 COMPLETED-已完成 CANCELLED-已取消")
    private String status;
    
    // @Schema(description = "维修人员ID")
    private Long repairerId;
    
    // @Schema(description = "维修人员姓名")
    private String repairerName;
    
    // @Schema(description = "维修人员电话")
    private String repairerPhone;
    
    // @Schema(description = "预约维修时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledTime;
    
    // @Schema(description = "实际开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    
    // @Schema(description = "完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedTime;
    
    // @Schema(description = "维修费用")
    private BigDecimal cost;
    
    // @Schema(description = "维修结果描述")
    private String result;
    
    // @Schema(description = "满意度评分 1-5分")
    private Integer rating;
    
    // @Schema(description = "评价内容")
    private String feedback;
    
    // @Schema(description = "图片附件URL")
    private String attachments;
    
    // @Schema(description = "备注")
    private String remark;
}
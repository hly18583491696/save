package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
// import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * 住宿记录实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dorm_accommodation")
// @Schema(description = "住宿记录信息")
public class Accommodation extends BaseEntity {
    
    // @Schema(description = "学生ID")
    @NotNull(message = "学生ID不能为空")
    private Long studentId;
    
    // @Schema(description = "学生姓名")
    private String studentName;
    
    // @Schema(description = "学号")
    private String studentNumber;
    
    // @Schema(description = "房间ID")
    @NotNull(message = "房间ID不能为空")
    private Long roomId;
    
    // @Schema(description = "房间号")
    private String roomNumber;
    
    // @Schema(description = "楼栋ID")
    private Long buildingId;
    
    // @Schema(description = "楼栋名称")
    private String buildingName;
    
    // @Schema(description = "床位号")
    private String bedNumber;
    
    // @Schema(description = "入住日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkInDate;
    
    // @Schema(description = "退宿日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate checkOutDate;
    
    // @Schema(description = "住宿状态 LIVING-在住 CHECKED_OUT-已退宿 SUSPENDED-暂停")
    private String status;
    
    // @Schema(description = "学期")
    private String semester;
    
    // @Schema(description = "学年")
    private String academicYear;
    
    // @Schema(description = "住宿费用")
    private BigDecimal fee;
    
    // @Schema(description = "缴费状态 UNPAID-未缴费 PAID-已缴费 PARTIAL-部分缴费")
    private String paymentStatus;
    
    // @Schema(description = "缴费日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;
    
    // @Schema(description = "备注")
    private String remark;
}
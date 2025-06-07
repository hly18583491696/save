package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
// import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * 宿舍房间实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dorm_room")
// @Schema(description = "宿舍房间信息")
public class Room extends BaseEntity {
    
    // @Schema(description = "房间号")
    @NotBlank(message = "房间号不能为空")
    private String roomNumber;
    
    // @Schema(description = "所属楼栋ID")
    @NotNull(message = "所属楼栋不能为空")
    private Long buildingId;
    
    // @Schema(description = "楼栋名称")
    private String buildingName;
    
    // @Schema(description = "楼层")
    @Min(value = 1, message = "楼层不能小于1")
    private Integer floor;
    
    // @Schema(description = "房间类型 2-双人间 4-四人间 6-六人间")
    private Integer roomType;
    
    // @Schema(description = "床位总数")
    @Min(value = 1, message = "床位总数不能小于1")
    private Integer totalBeds;
    
    // @Schema(description = "已入住床位数")
    private Integer occupiedBeds;
    
    // @Schema(description = "可用床位数")
    private Integer availableBeds;
    
    // @Schema(description = "房间面积(平方米)")
    private BigDecimal area;
    
    // @Schema(description = "房间设施")
    private String facilities;
    
    // @Schema(description = "房间状态 AVAILABLE-可入住 FULL-已满 MAINTENANCE-维修中 CLOSED-关闭")
    private String status;
    
    // @Schema(description = "房间费用(元/学期)")
    private BigDecimal fee;
    
    // @Schema(description = "备注")
    private String remark;
}
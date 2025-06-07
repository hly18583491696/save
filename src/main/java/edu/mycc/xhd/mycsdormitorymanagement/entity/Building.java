package edu.mycc.xhd.mycsdormitorymanagement.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
// import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

/**
 * 宿舍楼实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dorm_building")
// @Schema(description = "宿舍楼信息")
public class Building extends BaseEntity {
    
    // @Schema(description = "楼栋编号")
    @NotBlank(message = "楼栋编号不能为空")
    private String buildingCode;
    
    // @Schema(description = "楼栋名称")
    @NotBlank(message = "楼栋名称不能为空")
    private String buildingName;
    
    // @Schema(description = "楼层数")
    @Min(value = 1, message = "楼层数不能小于1")
    private Integer floors;
    
    // @Schema(description = "每层房间数")
    @Min(value = 1, message = "每层房间数不能小于1")
    private Integer roomsPerFloor;
    
    // @Schema(description = "楼栋类型 MALE-男生宿舍 FEMALE-女生宿舍")
    private String buildingType;
    
    // @Schema(description = "管理员ID")
    private Long managerId;
    
    // @Schema(description = "管理员姓名")
    private String managerName;
    
    // @Schema(description = "联系电话")
    private String contactPhone;
    
    // @Schema(description = "地址")
    private String address;
    
    // @Schema(description = "状态 0-停用 1-启用")
    private Integer status;
    
    // @Schema(description = "备注")
    private String remark;
}
package edu.mycc.xhd.entity;

// import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宿舍楼实体类
 * 
 * @author xhd
 */
@Data
@EqualsAndHashCode(callSuper = true)
// @TableName("dorm_building")
public class Building extends BaseEntity {
    
    /**
     * 楼栋编号
     */
    private String buildingCode;
    
    /**
     * 楼栋名称
     */
    private String buildingName;
    
    /**
     * 楼栋类型（1：男生宿舍，2：女生宿舍）
     */
    private Integer buildingType;
    
    /**
     * 总楼层数
     */
    private Integer totalFloors;
    
    /**
     * 每层房间数
     */
    private Integer roomsPerFloor;
    
    /**
     * 总房间数
     */
    private Integer totalRooms;
    
    /**
     * 宿管员ID
     */
    private Long managerId;
    
    /**
     * 宿管员姓名
     */
    private String managerName;
    
    /**
     * 状态（0：停用，1：启用）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
}
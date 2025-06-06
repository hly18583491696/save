package edu.mycc.xhd.entity;

// import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宿舍房间实体类
 * 
 * @author xhd
 */
@Data
@EqualsAndHashCode(callSuper = true)
// @TableName("dorm_room")
public class Room extends BaseEntity {
    
    /**
     * 房间号
     */
    private String roomNumber;
    
    /**
     * 所属楼栋ID
     */
    private Long buildingId;
    
    /**
     * 楼栋名称
     */
    private String buildingName;
    
    /**
     * 楼层
     */
    private Integer floor;
    
    /**
     * 房间类型（1：2人间，2：4人间，3：6人间）
     */
    private Integer roomType;
    
    /**
     * 床位总数
     */
    private Integer totalBeds;
    
    /**
     * 已入住人数
     */
    private Integer occupiedBeds;
    
    /**
     * 剩余床位
     */
    private Integer availableBeds;
    
    /**
     * 房间状态（1：正常，2：维修中，3：停用）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
}
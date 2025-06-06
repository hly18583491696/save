package edu.mycc.xhd.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

/**
 * 住宿记录实体类
 * 
 * @author xhd
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dorm_accommodation")
public class Accommodation extends BaseEntity {
    
    /**
     * 学生ID
     */
    private Long studentId;
    
    /**
     * 学生姓名
     */
    private String studentName;
    
    /**
     * 学号
     */
    private String studentCode;
    
    /**
     * 楼栋ID
     */
    private Long buildingId;
    
    /**
     * 楼栋名称
     */
    private String buildingName;
    
    /**
     * 房间ID
     */
    private Long roomId;
    
    /**
     * 房间号
     */
    private String roomNumber;
    
    /**
     * 床位号
     */
    private String bedNumber;
    
    /**
     * 入住日期
     */
    private LocalDate checkInDate;
    
    /**
     * 退宿日期
     */
    private LocalDate checkOutDate;
    
    /**
     * 住宿状态（1：在住，2：已退宿，3：请假）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
}
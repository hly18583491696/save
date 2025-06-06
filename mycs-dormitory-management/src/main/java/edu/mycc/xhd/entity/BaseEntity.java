package edu.mycc.xhd.entity;

// import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 
 * @author xhd
 */
@Data
public class BaseEntity {
    
    /**
     * 主键ID
     */
    // @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    /**
     * 创建时间
     */
    // @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    // @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 创建人
     */
    // @TableField(fill = FieldFill.INSERT)
    private Long createBy;
    
    /**
     * 更新人
     */
    // @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;
    
    /**
     * 逻辑删除标志（0：未删除，1：已删除）
     */
    // @TableLogic
    // @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
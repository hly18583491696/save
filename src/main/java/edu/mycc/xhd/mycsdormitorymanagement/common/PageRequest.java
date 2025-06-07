package edu.mycc.xhd.mycsdormitorymanagement.common;

import lombok.Data;
// import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * 分页查询请求类
 */
@Data
// @Schema(description = "分页查询请求")
public class PageRequest {
    
    // @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;
    
    // @Schema(description = "每页大小", example = "10")
    @Min(value = 1, message = "每页大小不能小于1")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer pageSize = 10;
    
    // @Schema(description = "排序字段", example = "createTime")
    private String sortField;
    
    // @Schema(description = "排序方向", example = "desc")
    private String sortOrder = "desc";
    
    // @Schema(description = "搜索关键词")
    private String keyword;
    
    // 手动添加getter和setter方法以解决Lombok问题
    public Integer getPageNum() {
        return pageNum;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public String getSortField() {
        return sortField;
    }
    
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
    
    public String getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
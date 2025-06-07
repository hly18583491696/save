package edu.mycc.xhd.mycsdormitorymanagement.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 统一响应结果类
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    
    private Integer code;
    private String message;
    private T data;
    
    // 成功响应
    public static <T> Result<T> success() {
        return new Result<>(200, "操作成功", null);
    }
    
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }
    
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    // 失败响应
    public static <T> Result<T> error() {
        return new Result<>(500, "操作失败", null);
    }
    
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    // 参数错误
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }
    
    // 未授权
    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(401, message, null);
    }
    
    // 禁止访问
    public static <T> Result<T> forbidden(String message) {
        return new Result<>(403, message, null);
    }
    
    // 资源未找到
    public static <T> Result<T> notFound(String message) {
        return new Result<>(404, message, null);
    }
}
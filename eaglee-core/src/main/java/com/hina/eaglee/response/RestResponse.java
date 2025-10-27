package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 统一响应结果类
 * @param <T> 响应数据类型
 */
@Data
@Schema(description = "统一响应结果")
public class RestResponse<T> implements Response {
    
    /**
     * 响应状态码
     */
    @Schema(description = "响应状态码", example = "200")
    private Integer code;
    
    /**
     * 响应消息
     */
    @Schema(description = "响应消息", example = "操作成功")
    private String message;
    
    /**
     * 响应数据
     */
    @Schema(description = "响应数据")
    private T data;
    
    /**
     * 响应时间戳
     */
    @Schema(description = "响应时间戳")
    private LocalDateTime timestamp;
    
    /**
     * 请求追踪ID
     */
    @Schema(description = "请求追踪ID")
    private String traceId;
    
    /**
     * 私有构造函数
     */
    private RestResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * 私有构造函数
     */
    private RestResponse(Integer code, String message, T data) {
        this();
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    /**
     * 成功响应
     */
    public static <T> RestResponse<T> success() {
        return new RestResponse<>(200, "操作成功", null);
    }
    
    /**
     * 成功响应带数据
     */
    public static <T> RestResponse<T> success(T data) {
        return new RestResponse<>(200, "操作成功", data);
    }
    
    /**
     * 成功响应带消息和数据
     */
    public static <T> RestResponse<T> success(String message, T data) {
        return new RestResponse<>(200, message, data);
    }
    
    /**
     * 失败响应
     */
    public static <T> RestResponse<T> error(String message) {
        return new RestResponse<>(500, message, null);
    }
    
    /**
     * 失败响应带状态码
     */
    public static <T> RestResponse<T> error(Integer code, String message) {
        return new RestResponse<>(code, message, null);
    }
    
    /**
     * 失败响应带状态码和数据
     */
    public static <T> RestResponse<T> error(Integer code, String message, T data) {
        return new RestResponse<>(code, message, data);
    }
    
    /**
     * 参数验证失败响应
     */
    public static <T> RestResponse<T> validationError(String message) {
        return new RestResponse<>(400, message, null);
    }
    
    /**
     * 业务异常响应
     */
    public static <T> RestResponse<T> businessError(String message) {
        return new RestResponse<>(422, message, null);
    }
    
    /**
     * 未找到资源响应
     */
    public static <T> RestResponse<T> notFound(String message) {
        return new RestResponse<>(404, message, null);
    }
    
    /**
     * 未授权响应
     */
    public static <T> RestResponse<T> unauthorized(String message) {
        return new RestResponse<>(401, message, null);
    }
    
    /**
     * 禁止访问响应
     */
    public static <T> RestResponse<T> forbidden(String message) {
        return new RestResponse<>(403, message, null);
    }
    
    /**
     * 设置追踪ID
     */
    public RestResponse<T> withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return this.code != null && this.code == 200;
    }
}
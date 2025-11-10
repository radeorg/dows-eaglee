package org.dows.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 错误响应类
 */
@Data
@Schema(description = "错误响应")
public class ErrorResponse {
    
    /**
     * 错误代码
     */
    @Schema(description = "错误代码", example = "VALIDATION_ERROR")
    private String code;
    
    /**
     * 错误消息
     */
    @Schema(description = "错误消息", example = "参数验证失败")
    private String message;
    
    /**
     * 详细错误信息
     */
    @Schema(description = "详细错误信息")
    private List<ErrorDetail> details;
    
    /**
     * 错误发生时间
     */
    @Schema(description = "错误发生时间")
    private LocalDateTime timestamp;
    
    /**
     * 请求路径
     */
    @Schema(description = "请求路径")
    private String path;
    
    /**
     * 请求追踪ID
     */
    @Schema(description = "请求追踪ID")
    private String traceId;
    
    /**
     * 私有构造函数
     */
    private ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * 私有构造函数
     */
    private ErrorResponse(String code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    /**
     * 创建错误响应
     */
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }
    
    /**
     * 创建错误响应带详细信息
     */
    public static ErrorResponse of(String code, String message, List<ErrorDetail> details) {
        ErrorResponse response = new ErrorResponse(code, message);
        response.setDetails(details);
        return response;
    }
    
    /**
     * 设置请求路径
     */
    public ErrorResponse withPath(String path) {
        this.path = path;
        return this;
    }
    
    /**
     * 设置追踪ID
     */
    public ErrorResponse withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    /**
     * 错误详细信息
     */
    @Data
    @Schema(description = "错误详细信息")
    public static class ErrorDetail {
        
        /**
         * 字段名称
         */
        @Schema(description = "字段名称", example = "projectName")
        private String field;
        
        /**
         * 错误值
         */
        @Schema(description = "错误值", example = "")
        private Object rejectedValue;
        
        /**
         * 错误消息
         */
        @Schema(description = "错误消息", example = "项目名称不能为空")
        private String message;
        
        /**
         * 构造函数
         */
        public ErrorDetail(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }
        
        /**
         * 创建错误详细信息
         */
        public static ErrorDetail of(String field, Object rejectedValue, String message) {
            return new ErrorDetail(field, rejectedValue, message);
        }
    }
}
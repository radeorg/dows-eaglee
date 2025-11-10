package org.dows.eaglee.exception;

import lombok.Getter;

/**
 * 业务异常类
 * 用于封装业务逻辑中的异常情况
 */
@Getter
public class BusinessException extends RuntimeException {
    
    /**
     * 异常代码
     */
    private final String code;
    
    /**
     * 构造函数
     * @param code 异常代码
     * @param message 异常消息
     */
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    /**
     * 构造函数
     * @param code 异常代码
     * @param message 异常消息
     * @param cause 原因异常
     */
    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    // 常见业务异常代码常量
    public static final String TASK_PROJECT_NOT_FOUND = "TASK_PROJECT_NOT_FOUND";
    public static final String TASK_INSTANCE_NOT_FOUND = "TASK_INSTANCE_NOT_FOUND";
    public static final String INVALID_TIME_INTERVAL = "INVALID_TIME_INTERVAL";
    public static final String COLLECTION_FAILED = "COLLECTION_FAILED";
    public static final String INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String DATA_NOT_FOUND = "DATA_NOT_FOUND";
    public static final String OPERATION_FAILED = "OPERATION_FAILED";
    public static final String DUPLICATE_DATA = "DUPLICATE_DATA";
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    
    /**
     * 创建任务项目未找到异常
     */
    public static BusinessException taskProjectNotFound(Long taskProjectId) {
        return new BusinessException(TASK_PROJECT_NOT_FOUND, 
            String.format("任务项目不存在，ID: %d", taskProjectId));
    }
    
    /**
     * 创建任务实例未找到异常
     */
    public static BusinessException taskInstanceNotFound(Long taskInstanceId) {
        return new BusinessException(TASK_INSTANCE_NOT_FOUND, 
            String.format("任务实例不存在，ID: %d", taskInstanceId));
    }
    
    /**
     * 创建无效时间间隔异常
     */
    public static BusinessException invalidTimeInterval(Integer interval) {
        return new BusinessException(INVALID_TIME_INTERVAL, 
            String.format("无效的时间间隔: %d，间隔必须不超过60秒且能被60整除", interval));
    }
    
    /**
     * 创建数据采集失败异常
     */
    public static BusinessException collectionFailed(String reason) {
        return new BusinessException(COLLECTION_FAILED, 
            String.format("数据采集失败: %s", reason));
    }
    
    /**
     * 创建参数无效异常
     */
    public static BusinessException invalidParameter(String paramName, Object paramValue) {
        return new BusinessException(INVALID_PARAMETER, 
            String.format("参数无效: %s = %s", paramName, paramValue));
    }
}
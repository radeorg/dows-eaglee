package com.hina.eaglee.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 业务异常测试类
 */
class BusinessExceptionTest {
    
    @Test
    void shouldCreateBusinessExceptionWithCodeAndMessage() {
        // Given
        String code = "TEST_ERROR";
        String message = "测试异常消息";
        
        // When
        BusinessException exception = new BusinessException(code, message);
        
        // Then
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getMessage()).isEqualTo(message);
    }
    
    @Test
    void shouldCreateBusinessExceptionWithCodeMessageAndCause() {
        // Given
        String code = "TEST_ERROR";
        String message = "测试异常消息";
        Throwable cause = new RuntimeException("原因异常");
        
        // When
        BusinessException exception = new BusinessException(code, message, cause);
        
        // Then
        assertThat(exception.getCode()).isEqualTo(code);
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
    }
    
    @Test
    void shouldCreateTaskProjectNotFoundException() {
        // Given
        Long taskProjectId = 123L;
        
        // When
        BusinessException exception = BusinessException.taskProjectNotFound(taskProjectId);
        
        // Then
        assertThat(exception.getCode()).isEqualTo(BusinessException.TASK_PROJECT_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("任务项目不存在，ID: 123");
    }
    
    @Test
    void shouldCreateTaskInstanceNotFoundException() {
        // Given
        Long taskInstanceId = 456L;
        
        // When
        BusinessException exception = BusinessException.taskInstanceNotFound(taskInstanceId);
        
        // Then
        assertThat(exception.getCode()).isEqualTo(BusinessException.TASK_INSTANCE_NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("任务实例不存在，ID: 456");
    }
    
    @Test
    void shouldCreateInvalidTimeIntervalException() {
        // Given
        Integer interval = 90;
        
        // When
        BusinessException exception = BusinessException.invalidTimeInterval(interval);
        
        // Then
        assertThat(exception.getCode()).isEqualTo(BusinessException.INVALID_TIME_INTERVAL);
        assertThat(exception.getMessage()).isEqualTo("无效的时间间隔: 90，间隔必须不超过60秒且能被60整除");
    }
    
    @Test
    void shouldCreateCollectionFailedException() {
        // Given
        String reason = "网络连接超时";
        
        // When
        BusinessException exception = BusinessException.collectionFailed(reason);
        
        // Then
        assertThat(exception.getCode()).isEqualTo(BusinessException.COLLECTION_FAILED);
        assertThat(exception.getMessage()).isEqualTo("数据采集失败: 网络连接超时");
    }
    
    @Test
    void shouldCreateInvalidParameterException() {
        // Given
        String paramName = "interval";
        Object paramValue = -1;
        
        // When
        BusinessException exception = BusinessException.invalidParameter(paramName, paramValue);
        
        // Then
        assertThat(exception.getCode()).isEqualTo(BusinessException.INVALID_PARAMETER);
        assertThat(exception.getMessage()).isEqualTo("参数无效: interval = -1");
    }
}
package com.hina.eaglee.response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 响应结果测试类
 */
class ResponseResultTest {
    
    @Test
    void shouldCreateSuccessResponse() {
        // When
        ResponseResult<Void> response = ResponseResult.success();
        
        // Then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("操作成功");
        assertThat(response.getData()).isNull();
        assertThat(response.getTimestamp()).isNotNull();
        assertThat(response.isSuccess()).isTrue();
    }
    
    @Test
    void shouldCreateSuccessResponseWithData() {
        // Given
        String data = "测试数据";
        
        // When
        ResponseResult<String> response = ResponseResult.success(data);
        
        // Then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("操作成功");
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.isSuccess()).isTrue();
    }
    
    @Test
    void shouldCreateSuccessResponseWithMessageAndData() {
        // Given
        String message = "自定义成功消息";
        String data = "测试数据";
        
        // When
        ResponseResult<String> response = ResponseResult.success(message, data);
        
        // Then
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isEqualTo(data);
        assertThat(response.isSuccess()).isTrue();
    }
    
    @Test
    void shouldCreateErrorResponse() {
        // Given
        String message = "错误消息";
        
        // When
        ResponseResult<Void> response = ResponseResult.error(message);
        
        // Then
        assertThat(response.getCode()).isEqualTo(500);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.getData()).isNull();
        assertThat(response.isSuccess()).isFalse();
    }
    
    @Test
    void shouldCreateErrorResponseWithCode() {
        // Given
        Integer code = 400;
        String message = "错误消息";
        
        // When
        ResponseResult<Void> response = ResponseResult.error(code, message);
        
        // Then
        assertThat(response.getCode()).isEqualTo(code);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.isSuccess()).isFalse();
    }
    
    @Test
    void shouldCreateValidationErrorResponse() {
        // Given
        String message = "参数验证失败";
        
        // When
        ResponseResult<Void> response = ResponseResult.validationError(message);
        
        // Then
        assertThat(response.getCode()).isEqualTo(400);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.isSuccess()).isFalse();
    }
    
    @Test
    void shouldCreateBusinessErrorResponse() {
        // Given
        String message = "业务异常";
        
        // When
        ResponseResult<Void> response = ResponseResult.businessError(message);
        
        // Then
        assertThat(response.getCode()).isEqualTo(422);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.isSuccess()).isFalse();
    }
    
    @Test
    void shouldCreateNotFoundResponse() {
        // Given
        String message = "资源未找到";
        
        // When
        ResponseResult<Void> response = ResponseResult.notFound(message);
        
        // Then
        assertThat(response.getCode()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo(message);
        assertThat(response.isSuccess()).isFalse();
    }
    
    @Test
    void shouldSetTraceId() {
        // Given
        String traceId = "test-trace-id";
        
        // When
        ResponseResult<Void> response = ResponseResult.<Void>success().withTraceId(traceId);
        
        // Then
        assertThat(response.getTraceId()).isEqualTo(traceId);
    }
}
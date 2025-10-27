package com.hina.eaglee.exception;

import com.hina.eaglee.response.ErrorResponse;
import com.hina.eaglee.response.RestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 全局异常处理器测试类
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void shouldHandleBusinessException() {
        // Given
        BusinessException exception = new BusinessException("TEST_ERROR", "测试业务异常");

        // When
        ResponseEntity<RestResponse<Void>> response = globalExceptionHandler.handleBusinessException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(422);
        assertThat(response.getBody().getMessage()).isEqualTo("测试业务异常");
        assertThat(response.getBody().getTraceId()).isNotNull();
    }

    @Test
    void shouldHandleValidationException() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("testObject", "testField", "测试字段不能为空");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        // When
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.getBody().getMessage()).isEqualTo("参数验证失败");
        assertThat(response.getBody().getDetails()).hasSize(1);
        assertThat(response.getBody().getDetails().get(0).getField()).isEqualTo("testField");
        assertThat(response.getBody().getDetails().get(0).getMessage()).isEqualTo("测试字段不能为空");
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("非法参数");

        // When
        ResponseEntity<RestResponse<Void>> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("非法参数");
    }

    @Test
    void shouldHandleRuntimeException() {
        // Given
        RuntimeException exception = new RuntimeException("运行时异常");

        // When
        ResponseEntity<RestResponse<Void>> response = globalExceptionHandler.handleRuntimeException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).isEqualTo("系统运行异常，请稍后重试");
    }

    @Test
    void shouldHandleGeneralException() {
        // Given
        Exception exception = new Exception("系统异常");

        // When
        ResponseEntity<RestResponse<Void>> response = globalExceptionHandler.handleGeneralException(exception);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).isEqualTo("系统内部错误，请联系管理员");
    }
}
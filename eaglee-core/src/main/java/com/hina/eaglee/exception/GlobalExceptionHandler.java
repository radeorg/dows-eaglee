package com.hina.eaglee.exception;

import com.hina.eaglee.response.ErrorResponse;
import com.hina.eaglee.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 统一处理系统中的各种异常，返回标准格式的错误响应
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ResponseResult<Void>> handleBusinessException(BusinessException e) {
        String traceId = generateTraceId();
        log.warn("业务异常 [{}]: {}", traceId, e.getMessage());
        
        ResponseResult<Void> result = ResponseResult.<Void>businessError(e.getMessage())
                .withTraceId(traceId);
        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
    }
    
    /**
     * 处理参数验证异常 - @Valid注解触发
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String traceId = generateTraceId();
        log.warn("参数验证异常 [{}]: {}", traceId, e.getMessage());
        
        List<ErrorResponse.ErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.ErrorDetail.of(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.of("VALIDATION_ERROR", "参数验证失败", details)
                .withPath(getCurrentRequestPath())
                .withTraceId(traceId);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        String traceId = generateTraceId();
        log.warn("参数绑定异常 [{}]: {}", traceId, e.getMessage());
        
        List<ErrorResponse.ErrorDetail> details = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.ErrorDetail.of(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.of("BIND_ERROR", "参数绑定失败", details)
                .withPath(getCurrentRequestPath())
                .withTraceId(traceId);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * 处理约束违反异常 - @Validated注解触发
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String traceId = generateTraceId();
        log.warn("约束违反异常 [{}]: {}", traceId, e.getMessage());
        
        List<ErrorResponse.ErrorDetail> details = e.getConstraintViolations()
                .stream()
                .map(violation -> ErrorResponse.ErrorDetail.of(
                        getFieldName(violation),
                        violation.getInvalidValue(),
                        violation.getMessage()
                ))
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.of("CONSTRAINT_VIOLATION", "约束验证失败", details)
                .withPath(getCurrentRequestPath())
                .withTraceId(traceId);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String traceId = generateTraceId();
        log.warn("参数类型不匹配异常 [{}]: {}", traceId, e.getMessage());
        
        String message = String.format("参数 '%s' 的值 '%s' 无法转换为 %s 类型", 
                e.getName(), e.getValue(), e.getRequiredType().getSimpleName());
        
        ErrorResponse errorResponse = ErrorResponse.of("TYPE_MISMATCH", message)
                .withPath(getCurrentRequestPath())
                .withTraceId(traceId);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }
    
    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseResult<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        String traceId = generateTraceId();
        log.warn("非法参数异常 [{}]: {}", traceId, e.getMessage());
        
        ResponseResult<Void> result = ResponseResult.<Void>validationError(e.getMessage())
                .withTraceId(traceId);
        
        return ResponseEntity.badRequest().body(result);
    }
    
    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseResult<Void>> handleRuntimeException(RuntimeException e) {
        String traceId = generateTraceId();
        log.error("运行时异常 [{}]: ", traceId, e);
        
        ResponseResult<Void> result = ResponseResult.<Void>error("系统运行异常，请稍后重试")
                .withTraceId(traceId);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    
    /**
     * 处理通用异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseResult<Void>> handleGeneralException(Exception e) {
        String traceId = generateTraceId();
        log.error("系统异常 [{}]: ", traceId, e);
        
        ResponseResult<Void> result = ResponseResult.<Void>error("系统内部错误，请联系管理员")
                .withTraceId(traceId);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
    
    /**
     * 生成追踪ID
     */
    private String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
    
    /**
     * 获取当前请求路径
     */
    private String getCurrentRequestPath() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            return request.getRequestURI();
        } catch (Exception e) {
            return "unknown";
        }
    }
    
    /**
     * 从约束违反中提取字段名
     */
    private String getFieldName(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
}
package com.hina.eaglee.util;

import com.hina.eaglee.response.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 响应工具类
 * 提供便捷的响应构建方法
 */
public class ResponseUtil {

    /**
     * 成功响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> success() {
        return ResponseEntity.ok(ResponseResult.success());
    }

    /**
     * 成功响应带数据
     */
    public static <T> ResponseEntity<ResponseResult<T>> success(T data) {
        return ResponseEntity.ok(ResponseResult.success(data));
    }

    /**
     * 成功响应带消息和数据
     */
    public static <T> ResponseEntity<ResponseResult<T>> success(String message, T data) {
        return ResponseEntity.ok(ResponseResult.success(message, data));
    }

    /**
     * 创建成功响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseResult.success("创建成功", data));
    }

    /**
     * 更新成功响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> updated(T data) {
        return ResponseEntity.ok(ResponseResult.success("更新成功", data));
    }

    /**
     * 删除成功响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> deleted() {
        return ResponseEntity.ok(ResponseResult.success("删除成功", null));
    }

    /**
     * 错误响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> error(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseResult.error(message));
    }

    /**
     * 错误响应带状态码
     */
    public static <T> ResponseEntity<ResponseResult<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(ResponseResult.error(status.value(), message));
    }

    /**
     * 参数验证失败响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> badRequest(String message) {
        return ResponseEntity.badRequest()
                .body(ResponseResult.validationError(message));
    }

    /**
     * 未找到资源响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseResult.notFound(message));
    }

    /**
     * 业务异常响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> businessError(String message) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ResponseResult.businessError(message));
    }

    /**
     * 未授权响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseResult.unauthorized(message));
    }

    /**
     * 禁止访问响应
     */
    public static <T> ResponseEntity<ResponseResult<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ResponseResult.forbidden(message));
    }
}
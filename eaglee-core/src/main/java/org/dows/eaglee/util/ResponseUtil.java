package org.dows.eaglee.util;

import org.dows.eaglee.response.RestResponse;
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
    public static <T> ResponseEntity<RestResponse<T>> success() {
        return ResponseEntity.ok(RestResponse.success());
    }

    /**
     * 成功响应带数据
     */
    public static <T> ResponseEntity<RestResponse<T>> success(T data) {
        return ResponseEntity.ok(RestResponse.success(data));
    }

    /**
     * 成功响应带消息和数据
     */
    public static <T> ResponseEntity<RestResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(RestResponse.success(message, data));
    }

    /**
     * 创建成功响应
     */
    public static <T> ResponseEntity<RestResponse<T>> created(T data) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestResponse.success("创建成功", data));
    }

    /**
     * 更新成功响应
     */
    public static <T> ResponseEntity<RestResponse<T>> updated(T data) {
        return ResponseEntity.ok(RestResponse.success("更新成功", data));
    }

    /**
     * 删除成功响应
     */
    public static <T> ResponseEntity<RestResponse<T>> deleted() {
        return ResponseEntity.ok(RestResponse.success("删除成功", null));
    }

    /**
     * 错误响应
     */
    public static <T> ResponseEntity<RestResponse<T>> error(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(RestResponse.error(message));
    }

    /**
     * 错误响应带状态码
     */
    public static <T> ResponseEntity<RestResponse<T>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(RestResponse.error(status.value(), message));
    }

    /**
     * 参数验证失败响应
     */
    public static <T> ResponseEntity<RestResponse<T>> badRequest(String message) {
        return ResponseEntity.badRequest()
                .body(RestResponse.validationError(message));
    }

    /**
     * 未找到资源响应
     */
    public static <T> ResponseEntity<RestResponse<T>> notFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RestResponse.notFound(message));
    }

    /**
     * 业务异常响应
     */
    public static <T> ResponseEntity<RestResponse<T>> businessError(String message) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(RestResponse.businessError(message));
    }

    /**
     * 未授权响应
     */
    public static <T> ResponseEntity<RestResponse<T>> unauthorized(String message) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(RestResponse.unauthorized(message));
    }

    /**
     * 禁止访问响应
     */
    public static <T> ResponseEntity<RestResponse<T>> forbidden(String message) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(RestResponse.forbidden(message));
    }
}
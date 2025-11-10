package org.dows.eaglee.response;

import cn.hutool.json.JSONUtil;
import org.dows.eaglee.annotation.Skip;
import org.dows.eaglee.util.TraceIdUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

// 指定扫描包
@ControllerAdvice(basePackages = {"org.dows.eaglee.config", "org.dows.eaglee.collect", "org.dows.eaglee.manage", "org.dows.eaglee.metric","org.dows.eaglee.dolphin"})
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    // 判断是否需要处理该返回值
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查是否有 Skip 注解
        if (returnType.hasMethodAnnotation(Skip.class)) {
            return false;
        }
        Class<?> type = returnType.getParameterType();
        return !(type.equals(RestResponse.class) ||
                type.equals(ResponseEntity.class) ||
                type.equals(void.class) ||
                isPrimitiveOrWrapper(type));
    }

    private boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() && !(type == Long.TYPE || type == Boolean.TYPE);
    }

    // 在写入响应体前执行
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // 处理 String 类型（特殊，必须转为 String 才能写入）
        if (body instanceof String) {
            return JSONUtil.toJsonStr(RestResponse.success(body));
        }
        // 其他类型直接包装
        return RestResponse.success(body).withTraceId(TraceIdUtil.generateTraceId());
    }
}

package com.hina.eaglee.notice;

import cn.hutool.core.bean.BeanUtil;
import com.hina.eaglee.dolphin.Uri;
import com.hina.eaglee.util.AnnotationExtractor;
import org.springframework.http.HttpMethod;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface NoticeMessage {

//    String getKey();

//    String getMsgType();

    //    Object getBody();
//
//    Object getHeader();
    default EndpointRequest getEndpoint() {
        Uri annotation = this.getClass().getAnnotation(Uri.class);
        if (annotation == null) {
            throw new IllegalArgumentException("NoticeRequest class must be annotated with @Uri");
        }
        String endpoint = annotation.value();
        HttpMethod httpMethod = null;
        if (endpoint.startsWith("get ")) {
            httpMethod = HttpMethod.GET;
            endpoint = endpoint.substring(4);
        } else if (endpoint.startsWith("post ")) {
            httpMethod = HttpMethod.POST;
            endpoint = endpoint.substring(5);
        } else if (endpoint.startsWith("put ")) {
            httpMethod = HttpMethod.PUT;
            endpoint = endpoint.substring(4);
        } else if (endpoint.startsWith("delete ")) {
            httpMethod = HttpMethod.DELETE;
            endpoint = endpoint.substring(7);
        }
        if (httpMethod == null) {
            throw new IllegalArgumentException("httpMethod not found: " + endpoint);
        }
        // 提取url参数
        //Map<String, String> urlParams = UrlParamExtractor.extractParameters(endpoint);

        EndpointRequest endpointRequest = new EndpointRequest();
        endpointRequest.setHttpMethod(httpMethod);
        //noticeRequestEntity.setEndpoint(endpoint);
        //Map<String, Object> stringObjectMap = BeanUtil.beanToMap(this);
        Map<Class<? extends Annotation>, Map<String, Object>> classMapMap = AnnotationExtractor.extractFiledValueByAnnotations(this);
        // 处理url追加参数
        StringBuilder uriParams = new StringBuilder();
        classMapMap.get(UriParam.class).forEach((k, v) -> {
            uriParams.append(k).append("=").append(v).append("&");
        });
        String uriParamStr = uriParams.deleteCharAt(uriParams.length() - 1).toString();
        endpoint += "?" + uriParamStr;
        endpointRequest.setEndpoint(endpoint);
        // todo 统一处理uri 中的path参数
        classMapMap.get(PathParam.class);


        // 获取body参数对象
        Map<String, Object> body = BeanUtil.beanToMap(this);
        // 填充header  header.forEach(endpointRequest::addHeader);
        classMapMap.get(UriHeader.class).forEach(endpointRequest::addHeader);
        // 填充body参数对象
        body.forEach(endpointRequest::addBody);
        return endpointRequest;
    }

    default <T extends NoticeMessage> T toRequestEntity(Class<T> noticeRequestClass) {
        return noticeRequestClass.cast(this);
    }
}

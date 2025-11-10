package org.dows.eaglee.exchange;

import cn.hutool.core.bean.BeanUtil;
import org.dows.eaglee.dolphin.Uri;
import org.dows.eaglee.notice.PathParam;
import org.dows.eaglee.notice.UriHeader;
import org.dows.eaglee.notice.UriParam;
import org.dows.eaglee.util.AnnotationExtractor;
import org.springframework.http.HttpMethod;

import java.lang.annotation.Annotation;
import java.util.Map;

public interface ExchangeMessage {

    //    String getKey();
//    String getMsgType();
//    Object getBody();
//    Object getHeader();
    default ExchangeRequest getRequest() {
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

        ExchangeRequest exchangeRequest = new ExchangeRequest();
        exchangeRequest.setHttpMethod(httpMethod);
        exchangeRequest.setEndpoint(endpoint);
        //Map<String, Object> stringObjectMap = BeanUtil.beanToMap(this);
        Map<Class<? extends Annotation>, Map<String, Object>> classMapMap = AnnotationExtractor.extractFiledValueByAnnotations(this);
        // 处理url追加参数

        Map<String, Object> paramMap = classMapMap.get(UriParam.class);
        if (paramMap != null) {
            StringBuilder uriParams = new StringBuilder();
            paramMap.forEach((k, v) -> {
                uriParams.append(k).append("=").append(v).append("&");
            });
            String uriParamStr = uriParams.deleteCharAt(uriParams.length() - 1).toString();
            endpoint += "?" + uriParamStr;
            exchangeRequest.setEndpoint(endpoint);
        }

        // todo 统一处理uri 中的path参数
        Map<String, Object> pathMap = classMapMap.get(PathParam.class);
        if (pathMap != null) {
            for (String k : pathMap.keySet()) {
                endpoint = endpoint.replace("{" + k + "}", pathMap.get(k).toString());
            }
            exchangeRequest.setEndpoint(endpoint);
        }

        // 获取body参数对象
        Map<String, Object> body = BeanUtil.beanToMap(this);
        // 填充header  header.forEach(endpointRequest::addHeader);
        Map<String, Object> headerMap = classMapMap.get(UriHeader.class);
        if (headerMap != null) {
            headerMap.forEach(exchangeRequest::addHeader);
        }
        // 填充body参数对象
        body.forEach(exchangeRequest::addBody);
        return exchangeRequest;
    }

    default <T extends ExchangeMessage> T toRequestEntity(Class<T> noticeRequestClass) {
        return noticeRequestClass.cast(this);
    }
}

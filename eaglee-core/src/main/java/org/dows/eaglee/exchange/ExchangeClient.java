package org.dows.eaglee.exchange;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ExchangeClient {

    private final RestTemplate restTemplate;

    public <T> T exchange(ExchangeEntity message, Class<T> responseType) {
        log.info("exchange message: {}", message);
        ExchangeRequest endpoint = message.getRequest();

        HttpHeaders headers = new HttpHeaders();
//        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            // 针对不同 HTTP 方法采用不同的传参方式
            if (endpoint.getHttpMethod() == HttpMethod.GET) {
                // GET 请求：将参数转换为 URL 查询参数
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint.getEndpoint());
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(message);
                // 添加查询参数
                stringObjectMap.forEach(builder::queryParam);
                // 这里需要将 dolphinRequest 对象的属性转换为查询参数
                HttpEntity<?> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        endpoint.getHttpMethod(),
                        requestEntity,
                        String.class
                );
                String body = response.getBody();
                log.info("notice response: {}", body);
                return BeanUtil.toBean(body, responseType);
            } else {
                HttpEntity<?> requestEntity = new HttpEntity<>(message, headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        endpoint.getEndpoint(),
                        endpoint.getHttpMethod(),
                        requestEntity,
                        String.class
                );
                String body = response.getBody();
                log.info("notice response: {}", body);
                return BeanUtil.toBean(body, responseType);
            }
        } catch (RestClientException e) {
            log.error("Failed to rerun process instance", e);
            throw new RuntimeException("Failed to rerun process instance", e);
        }
    }


    /**
     * 流式调用
     * @param message
     * @param responseType
     * @return
     * @param <T>
     */
    public <T> T streamExchange(ExchangeEntity message, Class<T> responseType) {
        log.info("exchange message: {}", message);
        ExchangeRequest endpoint = message.getRequest();

        HttpHeaders headers = new HttpHeaders();
//        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            // 针对不同 HTTP 方法采用不同的传参方式
            if (endpoint.getHttpMethod() == HttpMethod.GET) {
                // GET 请求：将参数转换为 URL 查询参数
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint.getEndpoint());
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(message);
                // 添加查询参数
                stringObjectMap.forEach(builder::queryParam);
                // 这里需要将 dolphinRequest 对象的属性转换为查询参数
                HttpEntity<?> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        endpoint.getHttpMethod(),
                        requestEntity,
                        String.class
                );
                String body = response.getBody();
                log.info("notice response: {}", body);
                return BeanUtil.toBean(body, responseType);
            } else {
                HttpEntity<?> requestEntity = new HttpEntity<>(message, headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        endpoint.getEndpoint(),
                        endpoint.getHttpMethod(),
                        requestEntity,
                        String.class
                );
                String body = response.getBody();
                log.info("notice response: {}", body);
                return BeanUtil.toBean(body, responseType);
            }
        } catch (RestClientException e) {
            log.error("Failed to rerun process instance", e);
            throw new RuntimeException("Failed to rerun process instance", e);
        }
    }

}

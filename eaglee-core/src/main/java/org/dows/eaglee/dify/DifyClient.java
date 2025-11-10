package org.dows.eaglee.dify;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.eaglee.exchange.ExchangeEntity;
import org.dows.eaglee.exchange.ExchangeRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class DifyClient {
    private final RestTemplate restTemplate;

    private final DifyProperties difyProperties;

    private final ObjectMapper objectMapper ;

    public <T> T exchange(ExchangeEntity message, Class<T> responseType) {

        log.info("notice message: {}", message);
        ExchangeRequest exchangeRequest = message.getRequest();

        HttpHeaders headers = new HttpHeaders();
        //headers.set("Authorization", "Bearer " + difyProperties.getChat().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        exchangeRequest.getHeaders().forEach((key, value) -> headers.add(key, value.toString()));

        try {
            // 针对不同 HTTP 方法采用不同的传参方式
            if (exchangeRequest.getHttpMethod() == HttpMethod.GET) {
                // GET 请求：将参数转换为 URL 查询参数
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(exchangeRequest.getEndpoint());
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(message);
                // 添加查询参数
                stringObjectMap.forEach(builder::queryParam);
                // 这里需要将 dolphinRequest 对象的属性转换为查询参数
                HttpEntity<?> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        exchangeRequest.getHttpMethod(),
                        requestEntity,
                        String.class
                );
                String body = response.getBody();
                log.info("notice response: {}", body);
                return JSONUtil.parseObj(body).toBean(responseType);
            } else {
                HttpEntity<?> requestEntity = new HttpEntity<>(message, headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        exchangeRequest.getEndpoint(),
                        exchangeRequest.getHttpMethod(),
                        requestEntity,
                        String.class
                );
                String body = response.getBody();
                log.info("notice response: {}", body);
                try {
                    return objectMapper.readValue(body, responseType);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                //return JSONUtil.parseObj(body).toBean(responseType);
            }
        } catch (RestClientException e) {
            log.error("Failed to rerun process instance", e);
            throw new RuntimeException("Failed to rerun process instance", e);
        }

        /*// 获取 endpoint 和 HTTP 方法
        log.info("chat message: {}", message);
        EndpointRequest endpoint = difyProperties.getChat();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + difyProperties.getChat().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            // 针对不同 HTTP 方法采用不同的传参方式
            if (endpoint.getHttpMethod() == HttpMethod.GET) {
            }
        }*/
    }
}

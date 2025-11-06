package com.hina.eaglee.dolphin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class DolphinClient {
    //private final WebClient webClient;
    private final RestTemplate restTemplate;
    private final DolphinProperties dolphinProperties;


    public String exchange(DolphinRequest dolphinRequest) {
        log.info("dolphin exchange message: {}", dolphinRequest);
        String endpoint = StrUtil.lowerFirst(dolphinRequest.getClass().getSimpleName()
                .replace("Request", ""));
        endpoint = String.format(dolphinProperties.getEndpoints().get(endpoint), dolphinRequest.getProjectCode());
        if (StrUtil.isBlank(endpoint)) {
            throw new IllegalArgumentException("endpoint not found: " + endpoint);
        }

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
        // 方法名直接映射
        String token = dolphinProperties.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            // 针对不同 HTTP 方法采用不同的传参方式
            if (httpMethod == HttpMethod.GET) {
                // GET 请求：将参数转换为 URL 查询参数
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
                Map<String, Object> stringObjectMap = BeanUtil.beanToMap(dolphinRequest);
                // 添加查询参数
                stringObjectMap.forEach(builder::queryParam);
                // 这里需要将 dolphinRequest 对象的属性转换为查询参数
                HttpEntity<?> requestEntity = new HttpEntity<>(headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        builder.toUriString(),
                        httpMethod,
                        requestEntity,
                        String.class
                );
                return response.getBody();
            } else {
                HttpEntity<?> requestEntity = new HttpEntity<>(dolphinRequest, headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        endpoint,
                        httpMethod,
                        requestEntity,
                        String.class
                );
                return response.getBody();
            }
        } catch (RestClientException e) {
            log.error("Failed to rerun process instance", e);
            throw new RuntimeException("Failed to rerun process instance", e);
        }
    }

    /**
     * 通过http接口获取指定工作流下的所有任务定义
     *
     * @param projectCode 工作流ID
     * @return 任务定义列表
     */
    public List<DolphinTaskDefinition> getTaskDefinitionByWorkflowId(Long projectCode) {
        // 方法名直接映射
        String endpoint = dolphinProperties.getEndpoints().get("getTaskDefinitionByWorkflowId");
        String url = dolphinProperties.getHost() + endpoint;
        String token = dolphinProperties.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("workflowIdentifier", projectCode);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<List<DolphinTaskDefinition>> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<DolphinTaskDefinition>>() {
                    }
            );
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Failed to get task definitions for workflow: {}", projectCode, e);
            throw new RuntimeException("Failed to fetch task definitions from Dolphin API", e);
        }
    }
}

package com.hina.eaglee.cluster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hina.eaglee.monitor.MonitorSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;


/**
 * -- 获取任务明细
 * http://cdh85-39:8088/ws/v1/cluster/apps?state=RUNNING&queue=root.xy_yarn_pool.production
 * -- 根据任务ID获取
 * http://cdh85-39:8088/ws/v1/cluster/apps/application_1744287866674_1425593
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class YarnClient {

    private final ClusterProperties clusterProperties;
    //todo private final WebClient webClient;
    private final RestTemplate restTemplate;

    private final ObjectMapper mapper;

    /**
     * 获取任务列表
     * http://cdh85-39:8088/ws/v1/cluster/apps?state=RUNNING&queue=root.xy_yarn_pool.production
     *
     * @return
     */
    public YarnApps apps(MonitorSetting monitorSetting) {
        // 方法名直接映射
        String endpoint = clusterProperties.getEndpoints().get("apps");
        //String url = clusterProperties.getHost() + endpoint;
        String token = clusterProperties.getToken();

        // 构建请求头，添加Bearer认证token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // 构建查询参数
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("state", monitorSetting.getYarnState())
                .queryParam("queue", monitorSetting.getYarnQueue());
        try {
            ResponseEntity<YarnApps> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<YarnApps>() {
                    }
            );
            return response.getBody();
        } catch (Exception e) {
            // throw new RuntimeException("Failed to fetch YarnApps from Cluster API", e);
            log.info("Failed to fetch YarnApps from Cluster API, Retry convert to String", e);
            // 先获取原始字符串响应
            ResponseEntity<String> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity, String.class
            );
            // 根据响应状态手动处理转换
            if (response.getStatusCode().is2xxSuccessful()) {


                String responseBody = response.getBody();
                // 尝试不同转换.因为返回的数据结构不同，需要根据实际情况进行转换
                try {
                    return mapper.readValue(responseBody, YarnApps.class);
                } catch (Exception e1) {
                    log.warn("Failed to parse response body: {}", e1.getMessage());
                }
                try {
                    Map map = mapper.readValue(responseBody, Map.class);
                    return null;
                } catch (Exception e1) {
                    log.warn("Failed to parse response body: {}", e1.getMessage());
                }
                // 暂时保持原有逻辑，需要您根据实际需求实现手动转换
                return null; // 需要实现手动转换逻辑
            } else {
                // 响应失败，记录日志并返回null或抛出异常
                log.warn("Cluster API returned non-success status: {}", response.getStatusCode());
                return null;
            }
        }
    }

    /**
     * 获取任务详情
     * http://cdh85-39:8088/ws/v1/cluster/apps/application_1744287866674_1425593
     *
     * @param appId
     * @return
     */
    public YarnApp node(String appId) {
        // 方法名直接映射
        try {
            String endpoint = clusterProperties.getEndpoints().get("node") + "/" + appId;
            String token = clusterProperties.getToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<YarnApp> response = restTemplate.exchange(
                    endpoint, HttpMethod.GET, entity, new ParameterizedTypeReference<YarnApp>() {
                    }
            );
            return response.getBody();
        } catch (Exception e) {
            log.warn("Failed to fetch yarn app: {}", e.getMessage());
            return null; // 或返回默认值
        }

    }
}

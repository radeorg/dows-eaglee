package com.hina.eaglee.cluster;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * -- 获取任务明细
 * http://cdh85-39:8088/ws/v1/cluster/apps?state=RUNNING&queue=root.xy_yarn_pool.production
 * -- 根据任务ID获取
 * http://cdh85-39:8088/ws/v1/cluster/apps/application_1744287866674_1425593
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ClusterClient {

    private final ClusterProperties clusterProperties;
    //private final WebClient webClient;
    private final RestTemplate restTemplate;

    /**
     * 获取任务列表
     * http://cdh85-39:8088/ws/v1/cluster/apps?state=RUNNING&queue=root.xy_yarn_pool.production
     *
     * @return
     */
    public YarnApps apps(String state) {
        // 方法名直接映射
        String endpoint = clusterProperties.getEndpoints().get("apps");
        //String url = clusterProperties.getHost() + endpoint;
        String token = clusterProperties.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        /**
         * state=RUNNING&queue=root.xy_yarn_pool.production
         */
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint)
                .queryParam("state", state)
                .queryParam("queue", "root.xy_yarn_pool.production");

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<YarnApps> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<YarnApps>() {
                    }
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch YarnApps from Cluster API", e);
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
        String endpoint = clusterProperties.getEndpoints().get("node");
        String token = clusterProperties.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint + appId);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<YarnApp> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<YarnApp>() {
                    }
            );
            return response.getBody();
        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch YarnApps from Cluster API", e);
        }
    }
}

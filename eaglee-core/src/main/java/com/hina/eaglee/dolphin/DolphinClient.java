package com.hina.eaglee.dolphin;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
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
    private final DolphinProperties dolphinProperties;
    //private final WebClient webClient;
    private final RestTemplate restTemplate;

    private final ThreadPoolTaskScheduler taskScheduler;

    private final Map<String, DolphinEndpoint> dolphinEndpoints;

    @PostConstruct
    protected void init() {
        dolphinProperties.getMonitors().forEach((key, value) -> {
            if (value.isEnable()) {
                DolphinMonitor bean = SpringUtil.getBean(key, DolphinMonitor.class);
                if (bean != null) {
                    /**
                     * 定时任务，每隔1分钟执行一次
                     * 1. 查询DolphinScheduler中的任务实例
                     * 2. 筛选出状态为"运行中"的任务实例
                     * 3. 查询这些任务实例的详细运行状态
                     * 4. 如果运行状态为"失败"，或任务执行时间超过预设的超时时间，则触发告警机制
                     */
                    String cron = value.getCron();
                    log.info("任务: {} 定时任务表达式: {}", key, cron);
                    CronTrigger cronTrigger = new CronTrigger(cron);
                    taskScheduler.schedule(() -> bean.monitor(value), cronTrigger);
                }
            }
        });
    }


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
        //HttpEntity requestEntity = new HttpEntity<>(dolphinRequest, headers);

        try {

            // 针对不同 HTTP 方法采用不同的传参方式
            if (httpMethod == HttpMethod.GET) {
                // GET 请求：将参数转换为 URL 查询参数
                UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint);
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
                // todo 测试用，后续删除
                endpoint = "http://10.0.20.25:12345/dolphinscheduler/projects/18111254772736/executors/execute";
                // POST/PUT/DELETE 请求：使用请求体传参
                RerunProcessInstanceRequest dolphinRequest1 = new RerunProcessInstanceRequest();
                dolphinRequest1.setExecuteType("REPEAT_RUNNING");
                dolphinRequest1.setButtonType("run");
                dolphinRequest1.setProcessInstanceId(128L);
                dolphinRequest1.setIndex(1);
                HttpEntity<DolphinRequest> requestEntity = new HttpEntity<>(dolphinRequest1, headers);
                ResponseEntity<String> response = restTemplate.exchange(
                        endpoint,
                        httpMethod,
                        requestEntity,
                        String.class
                );
                return response.getBody();
            }
            /*ResponseEntity<String> response = restTemplate.exchange(
                    endpoint,
                    httpMethod,
                    requestEntity,
                    String.class
            );*/
        } catch (RestClientException e) {
            log.error("Failed to rerun process instance", e);
            throw new RuntimeException("Failed to rerun process instance", e);
        }
    }

    /*public String exchange(Class<? extends DolphinEndpoint> endpointClass, DolphinRequest requestBody) {
        DolphinEndpoint endpoint = dolphinEndpoints.get(StrUtil.lowerFirst(endpointClass.getSimpleName()));
        if (endpoint != null) {
            return endpoint.exchange(requestBody);
        }
        return null;
    }*/
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

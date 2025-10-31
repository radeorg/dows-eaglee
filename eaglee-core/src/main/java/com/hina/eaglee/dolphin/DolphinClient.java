package com.hina.eaglee.dolphin;

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

@Slf4j
@RequiredArgsConstructor
@Component
public class DolphinClient {
    private final DolphinProperties dolphinProperties;
    //private final WebClient webClient;
    private final RestTemplate restTemplate;

    private final ThreadPoolTaskScheduler taskScheduler;

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

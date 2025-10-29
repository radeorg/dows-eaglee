package com.hina.eaglee.dolphin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
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


    public List<TaskDefinition> getTaskDefinitionByWorkflowId(String workflowIdentifier) {
        // 方法名直接映射
        String endpoint = dolphinProperties.getEndpoints().get("getTaskDefinitionByWorkflowId");
        String url = dolphinProperties.getHost() + endpoint;
        String token = dolphinProperties.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
                .queryParam("workflowIdentifier", workflowIdentifier);

        HttpEntity<?> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<List<TaskDefinition>> response = restTemplate.exchange(
                    builder.toUriString(), HttpMethod.GET, entity,
                    new ParameterizedTypeReference<List<TaskDefinition>>() {
                    }
            );
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Failed to get task definitions for workflow: {}", workflowIdentifier, e);
            throw new RuntimeException("Failed to fetch task definitions from Dolphin API", e);
        }
    }
}

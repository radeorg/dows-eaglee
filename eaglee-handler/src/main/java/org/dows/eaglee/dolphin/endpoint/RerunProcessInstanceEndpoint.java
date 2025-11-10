//package org.dows.eaglee.dolphin.endpoint;
//
//
//import cn.hutool.core.util.StrUtil;
//import dolphin.org.dows.eaglee.DolphinEndpoint;
//import dolphin.org.dows.eaglee.DolphinProperties;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.*;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
///**
// * 重新运行工作流实例
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class RerunProcessInstanceEndpoint implements DolphinEndpoint<RerunProcessInstanceRequest> {
//
//    private final RestTemplate restTemplate;
//    private final DolphinProperties dolphinProperties;
//
//    //http://10.0.20.25:12345/dolphinscheduler/projects/%s/executors/execute
//    @Override
//    public String exchange(RerunProcessInstanceRequest dolphinRequest) {
//        log.info("RerunProcessInstanceEndpoint exchange message: {}", dolphinRequest);
//        String endpoint = StrUtil.lowerFirst(RerunProcessInstanceEndpoint.class.getSimpleName()
//                .replace("Endpoint", ""));
//        endpoint = String.format(dolphinProperties.getEndpoints().get(endpoint), dolphinRequest.getProjectCode());
//        if (StrUtil.isBlank(endpoint)) {
//            throw new IllegalArgumentException("endpoint not found: " + endpoint);
//        }
//
//        if(endpoint.startsWith("get ")){
//            endpoint = endpoint.substring(4);
//        }
//        // 方法名直接映射
//        String token = dolphinProperties.getToken();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("token", token);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<RerunProcessInstanceRequest> requestEntity = new HttpEntity<>(dolphinRequest, headers);
//
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(
//                    endpoint,
//                    HttpMethod.POST,
//                    requestEntity,
//                    String.class
//            );
//            return response.getBody();
//        } catch (RestClientException e) {
//            log.error("Failed to rerun process instance", e);
//            throw new RuntimeException("Failed to rerun process instance", e);
//        }
//    }
//}

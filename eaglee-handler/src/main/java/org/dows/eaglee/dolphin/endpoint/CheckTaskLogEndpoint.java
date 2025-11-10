//package org.dows.eaglee.dolphin.endpoint;
//
//import dolphin.org.dows.eaglee.DolphinEndpoint;
//import dolphin.org.dows.eaglee.DolphinRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
///**
// * 检查dolphin任务日志
// */
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CheckTaskLogEndpoint implements DolphinEndpoint {
//    @Override
//    public String exchange(DolphinRequest dolphinRequest) {
//        log.info("CheckTaskLogEndpoint exchange message: {}", dolphinRequest);
//
//        /*UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endpoint)
//                .queryParam("workflowIdentifier", projectCode);
//
//        HttpEntity<?> entity = new HttpEntity<>(headers);
//        try {
//            ResponseEntity<List<DolphinTaskDefinition>> response = restTemplate.exchange(
//                    builder.toUriString(), HttpMethod.POST, entity,
//                    new ParameterizedTypeReference<List<DolphinTaskDefinition>>() {
//                    }
//            );
//            return response.getBody();
//        } catch (RestClientException e) {
//            log.error("Failed to get task definitions for workflow: {}", projectCode, e);
//            throw new RuntimeException("Failed to fetch task definitions from Dolphin API", e);
//        }*/
//        return "";
//    }
//}

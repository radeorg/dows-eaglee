package com.hina.eaglee.cluster;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "hina.eaglee.cluster")
public class ClusterProperties {

    private List<String> host;
    private String token;
    private Map<String, String> endpoints;
}

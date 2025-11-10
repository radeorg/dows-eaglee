package org.dows.eaglee.dolphin;

import lombok.Data;
import org.dows.eaglee.analysis.AnalysisSetting;
import org.dows.eaglee.monitor.MonitorSetting;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "dows.eaglee.dolphin")
public class DolphinProperties {
    private String host;
    private String token;
    private Map<String, String> endpoints;

    private Map<String, MonitorSetting> monitors;

    private Map<String, AnalysisSetting> analyses;


    private Map<String, NoticeSetting> notices;
}

package com.hina.eaglee.dolphin;

import com.hina.eaglee.analysis.AnalysisSetting;
import com.hina.eaglee.monitor.MonitorSetting;
import com.hina.eaglee.notice.NoticeClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "hina.eaglee.dolphin")
public class DolphinProperties {
    private String host;
    private String token;
    private Map<String, String> endpoints;

    private Map<String, MonitorSetting> monitors;

    private Map<String, AnalysisSetting> analyses;


    private Map<String, NoticeSetting> notices;
}

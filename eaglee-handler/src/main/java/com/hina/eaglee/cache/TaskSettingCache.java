package com.hina.eaglee.cache;

import com.hina.eaglee.dao.TaskSettingDao;
import com.hina.eaglee.pojo.MetricSetting;
import com.hina.eaglee.setting.TaskRuleSetting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskSettingCache {

    private static final Map<String, MetricSetting> metricSettingMap = new ConcurrentHashMap<>();

    private final TaskSettingDao taskSettingDao;
    @NotBlank(message = "节点IP地址不能为空")
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", message = "IP地址格式不正确")
    public MetricSetting getMetricSetting(String ip) {
        return null;
    }


    public TaskRuleSetting getTaskSetting(Long taskCode) {
        return null;
    }

}

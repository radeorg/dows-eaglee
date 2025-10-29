package com.hina.eaglee.cache;

import cn.hutool.setting.Setting;
import com.hina.eaglee.pojo.MetricSetting;
import com.hina.eaglee.pojo.TaskSetting;
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
public class TaskCacheHandler {

    private static final Map<String, MetricSetting> metricSettingMap = new ConcurrentHashMap<>();
    private static final Map<String, TaskSetting> taskSettingMap = new ConcurrentHashMap<>();

    @NotBlank(message = "节点IP地址不能为空")
    @Pattern(regexp = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$", message = "IP地址格式不正确")
    public MetricSetting getMetricSetting(String ip) {
        return null;
    }


    public TaskSetting getTaskSetting(String codeIdentifier) {
        return null;
    }

}

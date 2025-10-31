package com.hina.eaglee.dolphin;

import lombok.Data;

@Data
public class MonitorSetting {
    // 是否启用监控
    private boolean enable;
    // 监控项目编码
    private String projectCode;
    // 监控表达式
    private String cron;
}

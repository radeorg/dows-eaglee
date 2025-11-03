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
    // 需要监听的状态，默认RUNNING
    private String status = "RUNNING";

    // 需要监听的队列，默认root.xy_yarn_pool.production
    private String queue = "root.xy_yarn_pool.production";
}

package com.hina.eaglee.dolphin;

import lombok.Data;

@Data
public class MonitorSetting {
    // 是否启用监控
    private boolean enable;
    // 监控项目编码, 多个项目用逗号分隔, 如：1,2,3
    private String projectCode;
    // 监控表达式
    private String cron;

    private String yarnState = "RUNNING";
    // 需要监听的队列，默认root.xy_yarn_pool.production
    private String yarnQueue = "root.xy_yarn_pool.production";


    // TaskStatus
    // dolphin 需要监听的状态，默认RUNNING，逗号分割，如RUNNING,FINISHED
    private String status = "RUNNING";
    // dolphin 监控间隔，单位：分钟（默认5天）
    private Integer intervalTime = 5 * 24 * 60;

    private boolean testMode;
}

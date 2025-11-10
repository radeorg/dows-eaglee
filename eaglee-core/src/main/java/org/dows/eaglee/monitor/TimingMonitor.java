package org.dows.eaglee.monitor;

import cn.hutool.extra.spring.SpringUtil;
import org.dows.eaglee.dolphin.DolphinProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TimingMonitor {
    private final DolphinProperties dolphinProperties;
    private final ThreadPoolTaskScheduler taskScheduler;

    @PostConstruct
    protected void init() {
        dolphinProperties.getMonitors().forEach((key, value) -> {
            if (value.isEnable()) {
                ResourceMonitor bean = SpringUtil.getBean(key, ResourceMonitor.class);
                if (bean != null) {
                    /**
                     * 定时任务，每隔1分钟执行一次
                     * 1. 查询DolphinScheduler中的任务实例
                     * 2. 筛选出状态为"运行中"的任务实例
                     * 3. 查询这些任务实例的详细运行状态
                     * 4. 如果运行状态为"失败"，或任务执行时间超过预设的超时时间，则触发告警机制
                     */
                    String cron = value.getCron();
                    log.info("任务: {} 定时任务表达式: {}", key, cron);
                    CronTrigger cronTrigger = new CronTrigger(cron);
                    taskScheduler.schedule(() -> bean.monitor(value), cronTrigger);
                }
            }
        });
    }
}

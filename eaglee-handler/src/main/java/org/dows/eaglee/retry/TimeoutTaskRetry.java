package org.dows.eaglee.retry;

import org.dows.eaglee.alert.AlertInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 超时重试
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TimeoutTaskRetry implements TaskRetry {

    @Override
    public void retry(AlertInfo taskAlert) {
        log.info("超时重试，任务：{}", taskAlert.getTaskName());
    }
}

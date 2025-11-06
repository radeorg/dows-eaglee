package com.hina.eaglee.retry;

import com.hina.eaglee.alert.AlertInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 业务通知重试，通知对应的人
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BizExceptionRetry implements TaskRetry {


    @Override
    public void retry(AlertInfo taskAlert) {
        log.info("业务异常重试，任务：{}", taskAlert.getTaskName());
    }
}

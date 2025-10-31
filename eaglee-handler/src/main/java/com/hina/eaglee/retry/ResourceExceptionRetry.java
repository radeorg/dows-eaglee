package com.hina.eaglee.retry;

import com.hina.eaglee.task.TaskRetry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 资源异常重试，自动重试
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ResourceExceptionRetry implements TaskRetry {
    @Override
    public void retry() {
        log.info("资源异常重试");
    }
}

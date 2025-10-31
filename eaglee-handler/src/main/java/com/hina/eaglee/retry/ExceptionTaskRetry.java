package com.hina.eaglee.retry;

import com.hina.eaglee.task.TaskRetry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 异常重试
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ExceptionTaskRetry implements TaskRetry {
    @Override
    public void retry() {

    }
}

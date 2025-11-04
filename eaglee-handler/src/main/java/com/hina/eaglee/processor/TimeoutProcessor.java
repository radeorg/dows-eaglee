package com.hina.eaglee.processor;

import com.hina.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TimeoutProcessor implements StateProcessor {
    @Override
    public void handle(TaskInfo taskInfo) {
        log.info("TimeoutProcessor handle taskInfo: {}", taskInfo);
    }
}

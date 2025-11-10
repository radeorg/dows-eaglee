package org.dows.eaglee.processor;

import org.dows.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TimeoutProcessor implements StateProcessor {
    @Override
    public void handle(TaskInfo taskInfo) {
        //log.info("TimeoutProcessor handle taskInfo: {}", taskInfo);
        log.info("任务超时，任务ID: {}, 任务名称: {}", taskInfo.getYarnApp().getId(), taskInfo.getYarnApp().getName());
    }
}

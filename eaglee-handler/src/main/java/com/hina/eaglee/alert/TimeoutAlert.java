package com.hina.eaglee.alert;


import cn.hutool.json.JSONUtil;
import com.hina.eaglee.processor.StateProcessor;
import com.hina.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class TimeoutAlert implements StateAlert {

    private final Map<String, StateProcessor> taskProcessors;
    @Override
    public void handle(TaskInfo taskInfo) {
        //TaskProcessor taskProcessor = taskProcessors.get(taskInfo.get());
        log.info("任务超时告警：{}", JSONUtil.toJsonStr(taskInfo));
    }
}

package com.hina.eaglee.alert;


import com.hina.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TimeoutAlert implements StateAlert {
    @Override
    public void handle(TaskInfo taskInfo) {

    }
}

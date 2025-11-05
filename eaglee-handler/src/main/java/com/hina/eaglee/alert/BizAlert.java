package com.hina.eaglee.alert;


import com.hina.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BizAlert implements TaskAlert {
    @Override
    public void handle(TaskInfo taskInfo) {

    }
}

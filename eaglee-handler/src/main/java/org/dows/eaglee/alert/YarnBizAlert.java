package org.dows.eaglee.alert;


import org.dows.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class YarnBizAlert implements TaskAlert {
    @Override
    public void handle(TaskInfo taskInfo) {

    }
}

package com.hina.eaglee.alert;

import com.hina.eaglee.status.TaskInfo;

public interface StateAlert {
    void handle(TaskInfo taskInfo);
}

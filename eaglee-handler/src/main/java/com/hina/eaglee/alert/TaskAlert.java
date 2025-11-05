package com.hina.eaglee.alert;

import com.hina.eaglee.status.TaskInfo;

public interface TaskAlert {
    void handle(TaskInfo taskInfo);
}

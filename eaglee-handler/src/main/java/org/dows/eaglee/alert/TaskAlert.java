package org.dows.eaglee.alert;

import org.dows.eaglee.status.TaskInfo;

public interface TaskAlert {
    void handle(TaskInfo taskInfo);
}

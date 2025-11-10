package org.dows.eaglee.processor;

import org.dows.eaglee.status.TaskInfo;

public interface StateProcessor {
    void handle(TaskInfo taskInfo);
}

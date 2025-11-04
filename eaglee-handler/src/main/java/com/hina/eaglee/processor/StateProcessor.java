package com.hina.eaglee.processor;

import com.hina.eaglee.status.TaskInfo;

public interface StateProcessor {
    void handle(TaskInfo taskInfo);
}

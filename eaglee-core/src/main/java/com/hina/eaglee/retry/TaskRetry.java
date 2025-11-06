package com.hina.eaglee.retry;

import com.hina.eaglee.dolphin.ProcessAlert;
import com.hina.eaglee.dolphin.TaskAlert;

public interface TaskRetry /*extends Runnable*/ {
    void retry(TaskAlert taskAlert);

//    default void run() {
//        retry();
//    }
}

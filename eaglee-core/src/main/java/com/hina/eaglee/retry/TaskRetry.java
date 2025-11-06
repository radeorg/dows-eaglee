package com.hina.eaglee.retry;

import com.hina.eaglee.alert.TaskAlert;

public interface TaskRetry /*extends Runnable*/ {
    void retry(TaskAlert taskAlert);

//    default void run() {
//        retry();
//    }
}

package com.hina.eaglee.retry;

import com.hina.eaglee.alert.AlertInfo;

public interface TaskRetry /*extends Runnable*/ {
    void retry(AlertInfo taskAlert);

//    default void run() {
//        retry();
//    }
}

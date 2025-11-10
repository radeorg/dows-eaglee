package org.dows.eaglee.retry;

import org.dows.eaglee.alert.AlertInfo;

public interface TaskRetry /*extends Runnable*/ {
    void retry(AlertInfo taskAlert);

//    default void run() {
//        retry();
//    }
}

package com.hina.eaglee.task;

public interface TaskRetry /*extends Runnable*/ {
    void retry();

    default void run() {
        retry();
    }
}

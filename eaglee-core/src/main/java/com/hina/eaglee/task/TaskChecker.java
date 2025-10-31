package com.hina.eaglee.task;

/**
 * -- 获取任务明细
 * http://cdh85-39:8088/ws/v1/cluster/apps?state=RUNNING&queue=root.xy_yarn_pool.production
 * -- 根据任务ID获取
 * http://cdh85-39:8088/ws/v1/cluster/apps/application_1744287866674_1425593
 */
public interface TaskChecker {
    void check();

    default void run() {
        check();
    }
}

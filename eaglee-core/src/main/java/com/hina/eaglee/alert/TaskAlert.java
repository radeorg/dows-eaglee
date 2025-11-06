package com.hina.eaglee.alert;

import java.time.LocalDateTime;

public interface TaskAlert {

    default Long getProjectCode() {
        return null;
    }
    default Long getProcessId() {
        return null;
    }

    default String getApplicationId() {
        return null;
    }
    /**
     * 任务编码
     */
    default Long getTaskCode() {
        return null;
    }

    /**
     * 任务名称
     */
    default String getTaskName() {
        return null;
    }

    /**
     * 任务类型（如SPARK）
     */
    default String getTaskType() {
        return null;
    }

    /**
     * 任务状态（如FAILURE）
     */
    default String getTaskState() {
        return null;
    }

    /**
     * 任务开始时间
     */
    default LocalDateTime getTaskStartTime() {
        return null;
    }

    /**
     * 任务结束时间
     */
    default LocalDateTime getTaskEndTime() {
        return null;
    }

}

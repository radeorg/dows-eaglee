package org.dows.eaglee.status;

import lombok.Getter;

@Getter
public enum TaskStatus {

    /**
     * dolphin scheduler 任务状态
     * SUBMITTED_SUCCESS(0, "submit success"),
     * RUNNING_EXECUTION(1, "running"),
     * PAUSE(3, "pause"),
     * STOP(5, "stop"),
     * FAILURE(6, "failure"),
     * SUCCESS(7, "success"),
     * NEED_FAULT_TOLERANCE(8, "need fault tolerance"),
     * KILL(9, "kill"),
     * DELAY_EXECUTION(12, "delay execution"),
     * FORCED_SUCCESS(13, "forced success"),
     * DISPATCH(17, "dispatch"),
     *
     * yarn 任务状态
     * NEW
     * NEW_SAVING
     * SUBMITTED
     * ACCEPTED
     * RUNNING
     * FINISHED
     * FAILED
     * KILLED
     */

    NEW(-1),
    NEW_SAVING(-2),
    SUBMITTED(-3),
    ACCEPTED(-4),
    RUNNING(1),
    FAILURE(6),
    FINISHED(7),
    KILLED(9);

    private final Integer value;

    TaskStatus(Integer value) {
        this.value = value;
    }

    public static TaskStatus getByValue(Integer value) {
        for (TaskStatus status : values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }

}

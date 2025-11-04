package com.hina.eaglee.status;

import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.entity.DolphinTaskEntity;
import lombok.Data;

@Data
public class TaskInfo {

    private long projectCode;
    private String projectName;
    /*private int processInstanceId;
    private String processInstanceName;
    private String processCode;
    private String taskType;
    private String taskName;
    private long taskCode;*/
    private String applicationId;
    private long runningTime;
    private double averageDuration;
    private boolean timeout;

    private StateType stateType;
    private YarnApp yarnApp;
    private DolphinTaskEntity dolphinTaskEntity;

}

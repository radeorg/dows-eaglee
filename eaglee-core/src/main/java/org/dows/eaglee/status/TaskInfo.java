package org.dows.eaglee.status;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.dows.eaglee.cluster.YarnApp;
import org.dows.eaglee.dolphin.DolphinTask;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private String s3LogUrl;

    private String dsLogUrl;

    private StateType stateType;
    private YarnApp yarnApp;
    private DolphinTask dolphinTask;
    // 关联人员
    @JsonIgnore
    private final List<String> assignees = new ArrayList<>();

    public TaskInfo addAssignees(String assignee) {
        assignees.add(assignee);
        return this;
    }

    /**
     * 获取关联人员
     *
     * @return 关联人员
     */
    @JsonProperty("assignees")
    public String getAssignees() {
        if (assignees.isEmpty()) {
            return null;
        }
        // 执行用户
        String executorName = dolphinTask.getExecutorName();
        if (StrUtil.isNotBlank(executorName)) {
            assignees.add(executorName);
        }
        return assignees.stream()
                .map(s -> "@" + s)
                .collect(Collectors.joining(","));
    }
}

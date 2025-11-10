package org.dows.eaglee.dolphin;

import lombok.Data;

@Data
public class DolphinTaskDefinition {

    private String taskName;
    private Long taskCode;
    private String taskType;
    private Long projectCode;
    private String projectName;
}

package com.hina.eaglee.dolphin;

import lombok.Data;

@Data
public class ProcessAlert {


    /**
     * 项目编码
     */
    private Long projectCode;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 所有者
     */
    private String owner;

    /**
     * 流程ID
     */
    private Long processId;

    /**
     * 流程定义编码
     */
    private Long processDefinitionCode;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程类型（如START_PROCESS）
     */
    private String processType;

    /**
     * 流程状态（如SUCCESS）
     */
    private String processState;

    /**
     * 修改人
     */
    private String modifyBy;
}

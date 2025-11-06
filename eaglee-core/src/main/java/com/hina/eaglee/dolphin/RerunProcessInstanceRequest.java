package com.hina.eaglee.dolphin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 重新运行流程实例请求
 */
@Uri("/dolphinscheduler/projects/{projectCode}/executors/execute")
@Data
public class RerunProcessInstanceRequest implements DolphinRequest {

    @Schema(description = "项目编码")
    private Long projectCode;
    private Integer index;
    @Schema(description = "流程实例ID")
    private Long processInstanceId;
    @Schema(description = "执行类型(REPEAT_RUNNING)")
    private String executeType;
    @Schema(description = "按钮类型(run)")
    private String buttonType;

}

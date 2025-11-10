package org.dows.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "t_ds_task_relation", dataSource = "dolphinscheduler")
@Schema(description = "调度任务关系")
public class DolphinTaskRelationEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "主键ID")
    private Integer id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "项目编码")
    private Long projectCode;

    @Schema(description = "流程定义编码")
    private Long processDefinitionCode;

    @Schema(description = "流程定义版本")
    private Integer processDefinitionVersion;

    @Schema(description = "前置任务编码")
    private Long preTaskCode;

    @Schema(description = "前置任务版本")
    private Integer preTaskVersion;

    @Schema(description = "后置任务编码")
    private Long postTaskCode;

    @Schema(description = "后置任务版本")
    private Integer postTaskVersion;

    @Schema(description = "条件类型")
    private Byte conditionType;

    @Schema(description = "条件参数")
    private String conditionParams;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}

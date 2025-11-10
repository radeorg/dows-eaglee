package org.dows.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "t_ds_process_definition", dataSource = "dolphinscheduler")
public class DolphinProcessDefinitionEntity {

    @Id(keyType = KeyType.Auto)
    @Schema(description = "流程定义ID")
    private Integer id;
    @Schema(description = "流程定义编码")
    private Long code;
    @Schema(description = "流程定义名称")
    private String name;
    @Schema(description = "流程定义版本")
    private Integer version;
    @Schema(description = "流程定义描述")
    private String description;
    @Schema(description = "项目编码")
    private Long projectCode;
    @Schema(description = "发布状态")
    private Integer releaseState;
    @Schema(description = "用户ID")
    private Integer userId;
    @Schema(description = "全局参数")
    private String globalParams;
    @Schema(description = "标志位")
    private Integer flag;
    @Schema(description = "节点位置信息")
    private String locations;
    @Schema(description = "告警组ID")
    private Integer warningGroupId;
    @Schema(description = "超时时间")
    private Integer timeout;
    @Schema(description = "执行类型")
    private Integer executionType;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


}

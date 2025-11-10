package org.dows.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table(value = "t_ds_project",dataSource = "dolphinscheduler")
@Schema(description = "调度项目")
public class DolphinProjectEntity {
    @Id(keyType = KeyType.Auto)
    @Schema(description = "项目ID")
    private Integer id;

    @Schema(description = "项目名称")
    private String name;

    @Schema(description = "项目编码")
    private Long code;

    @Schema(description = "项目描述")
    private String description;

    @Schema(description = "用户ID")
    private Integer userId;

    @Schema(description = "状态标识")
    private Byte flag;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


}

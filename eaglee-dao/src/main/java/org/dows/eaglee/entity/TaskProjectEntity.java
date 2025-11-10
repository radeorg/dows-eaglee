package org.dows.eaglee.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务项目实体类
 * 
 * @author eaglee-system
 */
@Data
@Table("task_project")
@Schema(description = "任务项目")
public class TaskProjectEntity {
    
    @Id(keyType = KeyType.Auto)
    @Schema(description = "任务项目ID")
    private Long taskProjectId;
    
    @Schema(description = "项目名称")
    private String projectName;
    
    @Schema(description = "流程编码")
    private String processCode;
    
    @Schema(description = "项目标识")
    private String projectIdentifier;
    
    @Schema(description = "任务数量")
    private Integer taskCount;
    
    @Schema(description = "状态：0-未完成，1-已完成")
    private Integer state;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "创建时间")
    private LocalDateTime ct;
    
    @Schema(description = "更新时间")
    private LocalDateTime ut;
    
    @Schema(description = "创建者ID")
    private Long cid;
    
    @Schema(description = "是否删除")
    private Boolean deleted;

    @Schema(description = "乐观锁版本")
    private Integer version;
}
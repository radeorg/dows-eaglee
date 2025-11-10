package org.dows.eaglee.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务项目分页查询请求类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "流程实例分页查询请求")
public class TaskProcessPageRequest {
    
    @Min(value = 1, message = "页码必须大于0")
    @Schema(description = "页码", example = "1")
    private Integer pageNumber = 1;
    
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    @Schema(description = "每页大小", example = "10")
    private Integer pageSize = 10;


//    @Schema(description = "项目名称（模糊查询）", example = "用户管理")
//    private String projectName;
//    @Schema(description = "项目标识", example = "user-management")
//    private String projectIdentifier;

    @Schema(description = "流程实例名称（模糊查询）")
    private String processInstanceName;

    @Schema(description = "时间范围-天", example = "10")
    private Integer intervalDay;
    @Schema(description = "时间范围-分钟", example = "10")
    private Integer intervalMinutes;
    
    @Schema(description = "状态：0-未完成，1-已完成", example = "0")
    private Integer state;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "开始时间范围-开始", example = "2025-10-30 09:15:36")
    private LocalDateTime startTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "结束时间范围-结束", example = "2025-10-30 09:15:36")
    private LocalDateTime endTime;


    @JsonIgnore
    private Integer offset;

    public Integer getOffset() {
        return this.offset = (this.pageNumber - 1) * this.pageSize;
    }
}
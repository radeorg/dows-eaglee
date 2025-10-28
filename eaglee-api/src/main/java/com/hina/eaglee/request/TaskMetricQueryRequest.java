package com.hina.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "任务度量查询请求")
public class TaskMetricQueryRequest {


    @Schema(description = "时间单位：1=分钟，2=小时，3=天")
    public Integer timeUnit;

    @Schema(description = "开始时间范围-开始")
    private LocalDateTime startTime;

    @Schema(description = "结束时间范围-结束")
    private LocalDateTime endTime;
}

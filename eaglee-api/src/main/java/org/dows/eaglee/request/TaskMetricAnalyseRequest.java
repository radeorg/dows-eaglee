package org.dows.eaglee.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "任务度量分析请求")
public class TaskMetricAnalyseRequest {
    @Schema(description = "资源项（cpu,mem,disk,net）")
    private List<String> resources;

}

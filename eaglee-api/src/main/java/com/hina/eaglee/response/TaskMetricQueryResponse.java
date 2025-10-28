package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "任务度量查询请求")
public class TaskMetricQueryResponse {

    @Schema(description = "节点IP地址", example = "192.168.1.100")
    private String ip;

    @Schema(description = "CPU使用量", example = "50")
    private Integer cpuUsage;

    @Schema(description = "内存使用量", example = "40")
    private Integer memUsage;

    @Schema(description = "磁盘使用量", example = "20")
    private Integer diskUsage;

    @Schema(description = "网络使用量", example = "10")
    private Integer netUsage;
}

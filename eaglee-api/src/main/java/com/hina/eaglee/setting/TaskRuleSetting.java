package com.hina.eaglee.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "任务规则配置")
public class TaskRuleSetting {

    @Schema(description = "规则名", example = "150")
    public String ruleName;

    @Schema(description = "规则描述", example = "150")
    public String ruleDesc;

    @Schema(description = "超时告警阈值(%)", example = "150")
    public Integer timeoutThreshold;

    @Schema(description = "最小运行次数", example = "3")
    public Integer minRunTimes;

    @Schema(description = "是否启用超时告警", example = "true")
    public Boolean enableTimeoutAlarm;

    @Schema(description = "告警级别：警告/错误/严重", example = "警告")
    public String alarmLevel;

    @Schema(description = "异常类别：资源类/业务类", example = "业务类")
    public String exceptionType;

    @Schema(description = "重试间隔(s)", example = "6000")
    public Integer retryInterval;

    @Schema(description = "所属项目标识", example = "proj_xxx")
    public String projectIdentifier;

    @Schema(description = "重试次数", example = "3")
    public Integer retryTimes;

    @Schema(description = "任务的统计基数(默认3次),计算超时阈值时，需要根据任务的统计基数来计算(前三次运行时间的平均值为基数)", example = "3")
    public Integer taskCardinalCount = 3;
}
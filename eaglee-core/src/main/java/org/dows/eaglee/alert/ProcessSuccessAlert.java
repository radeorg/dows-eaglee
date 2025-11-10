package org.dows.eaglee.alert;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 流程实例执行成功请求实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessSuccessAlert extends ProcessAlert{

    //----------------------------------

    /**
     * 是否恢复执行（如NO）
     */
    private String recovery;

    /**
     * 运行次数
     */
    private Integer runTimes;

    /**
     * 流程开始时间
     */
    private LocalDateTime processStartTime;

    /**
     * 流程结束时间
     */
    private LocalDateTime processEndTime;

    /**
     * 流程执行主机（IP:端口）
     */
    private String processHost;

    /**
     * 流程持续时间
     */
    private String processDuration;
}
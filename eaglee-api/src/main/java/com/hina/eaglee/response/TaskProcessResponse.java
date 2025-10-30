package com.hina.eaglee.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务项目响应类
 * 
 * @author eaglee-system
 */
@Data
@Schema(description = "任务项目响应")
public class TaskProcessResponse {


    @Schema(description = "流程实例ID")
    private Integer processInstanceId;

    @Schema(description = "流程实例名称（模糊查询）")
    private String processInstanceName;

    @Schema(description = "任务数量", example = "5")
    private Integer taskCount;
    
    @Schema(description = "状态：0-未完成，1-已完成", example = "0")
    private Integer state;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "结束时间")
    private LocalDateTime endTime;


//    @Schema(description = "任务项目ID", example = "1")
//    private Long taskProjectId;
//
//    @Schema(description = "项目名称", example = "用户管理系统")
//    private String projectName;
//
//    @Schema(description = "流程编码", example = "USER_MGMT_001")
//    private String processCode;
//
//    @Schema(description = "项目标识", example = "user-management")
//    private String projectIdentifier;

//    @Schema(description = "项目耗时（毫秒）", example = "3600000")
//    private Long duration;
//
//    @Schema(description = "创建时间")
//    private LocalDateTime ct;
//
//    @Schema(description = "更新时间")
//    private LocalDateTime ut;
//
//    @Schema(description = "创建者ID", example = "1001")
//    private Long cid;

//    /**
//     * 获取状态描述
//     */
//    public String getStateDesc() {
//        if (state == null) {
//            return "未知";
//        }
//        return switch (state) {
//            case 0 -> "未完成";
//            case 1 -> "已完成";
//            default -> "未知";
//        };
//    }
//
//    /**
//     * 计算项目耗时
//     */
//    public Long getDuration() {
//        if (startTime != null && endTime != null) {
//            return java.time.Duration.between(startTime, endTime).toMillis();
//        }
//        return null;
//    }
}
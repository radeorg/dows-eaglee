package org.dows.eaglee.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 告警信息表实体类（对应t_ds_alert表）
 */
@Data
@Table(value = "t_ds_alert", dataSource = "dolphinscheduler")
@Schema(description = "Dolphin告警信息")
public class DolphinAlertEntity {

    /**
     * 主键ID
     */
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 告警标题
     */
    private String title;

    /**
     * 签名（sign=sha1(content)）
     */
    private String sign = "";

    /**
     * 消息内容（可为邮件或短信：邮件存JSON map，短信存字符串）
     */
    private String content;

    /**
     * 告警状态（0：等待执行；1：成功；2：失败）
     */
    private Integer alertStatus = 0;

    /**
     * 告警类型（1：流程成功；2：流程/任务失败）
     */
    private Integer warningType = 2;

    /**
     * 告警日志
     */
    private String log;

    /**
     * 告警组ID
     */
    private Integer alertgroupId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 项目编码
     */
    private Long projectCode;

    /**
     * 流程定义编码
     */
    private Long processDefinitionCode;

    /**
     * 流程实例ID
     */
    private Integer processInstanceId;

    /**
     * 告警方式类型（如邮件、短信等具体类型标识）
     */
    private Integer alertType;
}
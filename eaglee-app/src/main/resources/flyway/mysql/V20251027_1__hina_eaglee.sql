-- 数据库：dows_eaglee
CREATE DATABASE IF NOT EXISTS `dows_eaglee` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `dows_eaglee`;

-- 1. 任务项目表
CREATE TABLE IF NOT EXISTS `task_project` (
                                              `task_project_id`     BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              `project_name`        VARCHAR(255) NOT NULL COMMENT '项目名（业务系统定义）',
    `process_code`        VARCHAR(255) NOT NULL COMMENT 'DS中流程编码',
    `project_identifier`  VARCHAR(255) NOT NULL COMMENT '项目标识',
    `task_count`          INT          NOT NULL DEFAULT 0 COMMENT '任务数',
    `state`               TINYINT      NOT NULL DEFAULT 0 COMMENT '0:未完成，1:已完成',
    `start_time`          DATETIME     NULL COMMENT '实际开始时间（由节点反向更新）',
    `end_time`            DATETIME     NULL COMMENT '实际结束时间（由节点反向更新）',
    `deleted`             TINYINT      NOT NULL DEFAULT 0,
    `version`             INT          NOT NULL DEFAULT 0,
    INDEX `idx_project_identifier` (`project_identifier`),
    INDEX `idx_state` (`state`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4 COMMENT ='任务项目';

-- 2. 任务实例表
CREATE TABLE IF NOT EXISTS `task_instance` (
                                               `task_instance_id`  BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               `task_project_id`   BIGINT       NOT NULL,
                                               `task_name`         VARCHAR(255) NOT NULL COMMENT 'DS中任务编码',
    `task_identifier`   VARCHAR(255) NOT NULL COMMENT '同一类任务标识',
    `application_id`    VARCHAR(255) NULL,
    `process_name`      VARCHAR(255) NULL,
    `reason`            VARCHAR(500) NULL,
    `avg_time`          BIGINT       NOT NULL DEFAULT 0 COMMENT '平均耗时（毫秒）',
    `elapsed_time`      BIGINT       NOT NULL DEFAULT 0 COMMENT '当前耗时（毫秒）=now-start_time',
    `duration`          BIGINT       NOT NULL DEFAULT 0 COMMENT '实际时长（毫秒）',
    `retried`           INT          NOT NULL DEFAULT 0,
    `state`             INT          NOT NULL,
    `start_time`        DATETIME     NOT NULL,
    `end_time`          DATETIME     NULL,
    `ct`                DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ut`                DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `cid`               BIGINT       NULL,
    `deleted`           TINYINT      NOT NULL DEFAULT 0,
    `version`           INT          NOT NULL DEFAULT 0,
    INDEX `idx_task_project_id` (`task_project_id`),
    INDEX `idx_task_identifier` (`task_identifier`),
    INDEX `idx_start_time` (`start_time`),
    CONSTRAINT `fk_instance_project` FOREIGN KEY (`task_project_id`) REFERENCES `task_project` (`task_project_id`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4 COMMENT ='任务实例';

-- 3. 任务计数器表（实时累加）
CREATE TABLE IF NOT EXISTS `task_counter` (
                                              `task_counter_id`    BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              `project_identifier` VARCHAR(255) NOT NULL,
    `task_identifier`    VARCHAR(255) NOT NULL,
    `expend_total`       BIGINT       NOT NULL DEFAULT 0 COMMENT '总耗时（毫秒）',
    `task_count`         BIGINT       NOT NULL DEFAULT 0 COMMENT '任务实例数量',
    `deleted`            TINYINT      NOT NULL DEFAULT 0,
    `version`            INT          NOT NULL DEFAULT 0,
    UNIQUE KEY `uk_project_task` (`project_identifier`, `task_identifier`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4 COMMENT ='任务计数器（动态同步更新）';

-- 4. 任务运行时（秒级）
CREATE TABLE IF NOT EXISTS `task_runtime` (
                                              `task_runtime_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              `ip`              VARCHAR(45) NOT NULL,
    `cpu_usage`       INT         NOT NULL,
    `mem_usage`       INT         NOT NULL,
    `disk_usage`      INT         NOT NULL,
    `net_usage`       INT         NOT NULL,
    `ct`              DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted`         TINYINT     NOT NULL DEFAULT 0,
    `version`         INT         NOT NULL DEFAULT 0,
    INDEX `idx_ip_ct` (`ip`, `ct`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4 COMMENT ='任务运行时（秒级，<=60且被60整除）';

-- 5. 任务度量（分钟及以上）
CREATE TABLE IF NOT EXISTS `task_metric` (
                                             `task_metric_id`     BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             `ip`                 VARCHAR(45) NOT NULL,
    `time_unit`          INT         NOT NULL COMMENT '时间单位：1=分钟，2=小时，3=天',
    `cpu_total_usage`    INT         NOT NULL,
    `mem_total_usage`    INT         NOT NULL,
    `disk_total_usage`   INT         NOT NULL,
    `net_total_usage`    INT         NOT NULL,
    `cpu_total_minute`   INT         NOT NULL,
    `mem_total_minute`   INT         NOT NULL,
    `disk_total_minute`  INT         NOT NULL,
    `net_total_minute`   INT         NOT NULL,
    `cpu_total_hour`     INT         NOT NULL,
    `mem_total_hour`     INT         NOT NULL,
    `disk_total_hour`    INT         NOT NULL,
    `net_total_hour`     INT         NOT NULL,
    `ct`                 DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted`            TINYINT     NOT NULL DEFAULT 0,
    `version`            INT         NOT NULL DEFAULT 0,
    INDEX `idx_ip_timeUnit` (`ip`, `time_unit`, `ct`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4 COMMENT ='任务度量（分钟及以上）';

-- 6. 任务配置表
CREATE TABLE IF NOT EXISTS `task_config` (
                                             `task_config_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             `key`            VARCHAR(255) NOT NULL,
    `description`    VARCHAR(500) NULL,
    `data_type`      VARCHAR(50)  NULL,
    `tag`            VARCHAR(50)  NULL,
    `options`        VARCHAR(500) NULL,
    `ct`             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ut`             DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `cid`            BIGINT       NULL,
    `uid`            BIGINT       NULL,
    `deleted`        TINYINT      NOT NULL DEFAULT 0,
    `version`        INT          NOT NULL DEFAULT 0,
    UNIQUE KEY `uk_key` (`key`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4 COMMENT ='任务配置';

-- 7. 任务规则表
CREATE TABLE IF NOT EXISTS `task_rule` (
                                           `task_rule_id`     BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           `rule_name`        VARCHAR(255) NOT NULL,
    `config_json`      TEXT         NOT NULL,
    `reference_type`   INT          NOT NULL COMMENT '0:项目，1:任务',
    `reference_id`     BIGINT       NOT NULL,
    `ct`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `ut`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `cid`              BIGINT       NULL,
    `uid`              BIGINT       NULL,
    `deleted`          TINYINT      NOT NULL DEFAULT 0,
    `version`          INT          NOT NULL DEFAULT 0,
    INDEX `idx_reference` (`reference_type`, `reference_id`)
    ) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4 COMMENT ='任务规则';
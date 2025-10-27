-- 鹰眼监控系统数据库初始化脚本
-- 版本: V1.0.0
-- 描述: 创建初始数据库表结构

-- 设置字符集和排序规则
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================
-- 1. 任务项目表 (task_project)
-- ============================
DROP TABLE IF EXISTS `task_project`;
CREATE TABLE `task_project` (
  `task_project_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务项目ID',
  `project_name` varchar(255) NOT NULL COMMENT '项目名称',
  `process_code` varchar(100) NOT NULL COMMENT '流程编码',
  `project_identifier` varchar(100) NOT NULL COMMENT '项目标识',
  `task_count` int NOT NULL DEFAULT '0' COMMENT '任务数量',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-未完成，1-已完成',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cid` bigint DEFAULT NULL COMMENT '创建者ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`task_project_id`),
  UNIQUE KEY `uk_project_identifier` (`project_identifier`),
  KEY `idx_process_code` (`process_code`),
  KEY `idx_state` (`state`),
  KEY `idx_ct` (`ct`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务项目表';

-- ============================
-- 2. 任务实例表 (task_instance)
-- ============================
DROP TABLE IF EXISTS `task_instance`;
CREATE TABLE `task_instance` (
  `task_instance_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务实例ID',
  `task_project_id` bigint NOT NULL COMMENT '任务项目ID',
  `task_name` varchar(255) NOT NULL COMMENT '任务名称',
  `task_identifier` varchar(100) NOT NULL COMMENT '任务标识',
  `application_id` varchar(100) DEFAULT NULL COMMENT '应用ID',
  `process_name` varchar(255) DEFAULT NULL COMMENT '流程实例名称',
  `reason` text COMMENT '原因',
  `avg_time` bigint DEFAULT NULL COMMENT '平均耗时（毫秒）',
  `elapsed_time` bigint DEFAULT NULL COMMENT '当前耗时（毫秒）',
  `duration` bigint DEFAULT NULL COMMENT '时长（毫秒）',
  `retried` int NOT NULL DEFAULT '0' COMMENT '重试次数',
  `state` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-运行中，1-成功，2-失败',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cid` bigint DEFAULT NULL COMMENT '创建者ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`task_instance_id`),
  KEY `idx_task_project_id` (`task_project_id`),
  KEY `idx_task_identifier` (`task_identifier`),
  KEY `idx_application_id` (`application_id`),
  KEY `idx_state` (`state`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_ct` (`ct`),
  KEY `idx_deleted` (`deleted`),
  CONSTRAINT `fk_task_instance_project` FOREIGN KEY (`task_project_id`) REFERENCES `task_project` (`task_project_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务实例表';

-- ============================
-- 3. 任务计数器表 (task_counter)
-- ============================
DROP TABLE IF EXISTS `task_counter`;
CREATE TABLE `task_counter` (
  `task_counter_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务计数器ID',
  `task_identifier` varchar(100) NOT NULL COMMENT '任务标识',
  `total_count` int NOT NULL DEFAULT '0' COMMENT '总执行次数',
  `success_count` int NOT NULL DEFAULT '0' COMMENT '成功次数',
  `failure_count` int NOT NULL DEFAULT '0' COMMENT '失败次数',
  `total_time` bigint NOT NULL DEFAULT '0' COMMENT '总耗时（毫秒）',
  `avg_time` bigint NOT NULL DEFAULT '0' COMMENT '平均耗时（毫秒）',
  `min_time` bigint DEFAULT NULL COMMENT '最小耗时（毫秒）',
  `max_time` bigint DEFAULT NULL COMMENT '最大耗时（毫秒）',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`task_counter_id`),
  UNIQUE KEY `uk_task_identifier` (`task_identifier`),
  KEY `idx_ct` (`ct`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务计数器表';

-- ============================
-- 4. 任务运行时数据表 (task_runtime)
-- ============================
DROP TABLE IF EXISTS `task_runtime`;
CREATE TABLE `task_runtime` (
  `task_runtime_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务运行时ID',
  `ip` varchar(45) NOT NULL COMMENT '节点IP地址',
  `cpu_usage` int NOT NULL DEFAULT '0' COMMENT 'CPU使用量（百分比）',
  `mem_usage` int NOT NULL DEFAULT '0' COMMENT '内存使用量（百分比）',
  `disk_usage` int NOT NULL DEFAULT '0' COMMENT '磁盘使用量（百分比）',
  `net_usage` int NOT NULL DEFAULT '0' COMMENT '网络使用量（百分比）',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`task_runtime_id`),
  KEY `idx_ip` (`ip`),
  KEY `idx_ct` (`ct`),
  KEY `idx_deleted` (`deleted`),
  KEY `idx_ip_ct` (`ip`, `ct`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务运行时数据表';

-- ============================
-- 5. 任务度量数据表 (task_metric)
-- ============================
DROP TABLE IF EXISTS `task_metric`;
CREATE TABLE `task_metric` (
  `task_metric_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务度量ID',
  `ip` varchar(45) NOT NULL COMMENT '节点IP地址',
  `time_unit` varchar(20) NOT NULL COMMENT '时间单位：minute-分钟，hour-小时，day-天',
  `time_value` datetime NOT NULL COMMENT '时间值',
  `cpu_total` bigint NOT NULL DEFAULT '0' COMMENT 'CPU使用总量',
  `mem_total` bigint NOT NULL DEFAULT '0' COMMENT '内存使用总量',
  `disk_total` bigint NOT NULL DEFAULT '0' COMMENT '磁盘使用总量',
  `net_total` bigint NOT NULL DEFAULT '0' COMMENT '网络使用总量',
  `data_points` int NOT NULL DEFAULT '0' COMMENT '数据点数量',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`task_metric_id`),
  UNIQUE KEY `uk_ip_time_unit_value` (`ip`, `time_unit`, `time_value`),
  KEY `idx_ip` (`ip`),
  KEY `idx_time_unit` (`time_unit`),
  KEY `idx_time_value` (`time_value`),
  KEY `idx_ct` (`ct`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务度量数据表';

-- ============================
-- 6. 任务配置表 (task_config)
-- ============================
DROP TABLE IF EXISTS `task_config`;
CREATE TABLE `task_config` (
  `task_config_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务配置ID',
  `config_name` varchar(255) NOT NULL COMMENT '配置名称',
  `config_key` varchar(100) NOT NULL COMMENT '配置键',
  `config_value` text NOT NULL COMMENT '配置值',
  `config_desc` text COMMENT '配置描述',
  `config_type` varchar(50) NOT NULL DEFAULT 'string' COMMENT '配置类型：string-字符串，number-数字，boolean-布尔值，json-JSON对象',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用：0-禁用，1-启用',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cid` bigint DEFAULT NULL COMMENT '创建者ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`task_config_id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_config_type` (`config_type`),
  KEY `idx_enabled` (`enabled`),
  KEY `idx_ct` (`ct`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务配置表';

-- ============================
-- 7. 任务规则表 (task_rule)
-- ============================
DROP TABLE IF EXISTS `task_rule`;
CREATE TABLE `task_rule` (
  `task_rule_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务规则ID',
  `rule_name` varchar(255) NOT NULL COMMENT '规则名称',
  `rule_type` varchar(50) NOT NULL COMMENT '规则类型：monitor-监控规则，alert-告警规则，collection-采集规则',
  `application_type` varchar(20) NOT NULL COMMENT '应用类型：project-项目级，task-任务级',
  `rule_condition` text NOT NULL COMMENT '规则条件（JSON格式）',
  `rule_action` text NOT NULL COMMENT '规则动作（JSON格式）',
  `priority` int NOT NULL DEFAULT '0' COMMENT '优先级（数字越大优先级越高）',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用：0-禁用，1-启用',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cid` bigint DEFAULT NULL COMMENT '创建者ID',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
  PRIMARY KEY (`task_rule_id`),
  KEY `idx_rule_type` (`rule_type`),
  KEY `idx_application_type` (`application_type`),
  KEY `idx_priority` (`priority`),
  KEY `idx_enabled` (`enabled`),
  KEY `idx_ct` (`ct`),
  KEY `idx_deleted` (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务规则表';

-- ============================
-- 8. 创建分区表（用于大数据量的运行时数据）
-- ============================

-- 为task_runtime表创建按月分区（可选，适用于大数据量场景）
-- ALTER TABLE task_runtime PARTITION BY RANGE (YEAR(ct) * 100 + MONTH(ct)) (
--     PARTITION p202401 VALUES LESS THAN (202402),
--     PARTITION p202402 VALUES LESS THAN (202403),
--     PARTITION p202403 VALUES LESS THAN (202404),
--     PARTITION p202404 VALUES LESS THAN (202405),
--     PARTITION p202405 VALUES LESS THAN (202406),
--     PARTITION p202406 VALUES LESS THAN (202407),
--     PARTITION p202407 VALUES LESS THAN (202408),
--     PARTITION p202408 VALUES LESS THAN (202409),
--     PARTITION p202409 VALUES LESS THAN (202410),
--     PARTITION p202410 VALUES LESS THAN (202411),
--     PARTITION p202411 VALUES LESS THAN (202412),
--     PARTITION p202412 VALUES LESS THAN (202501),
--     PARTITION p_future VALUES LESS THAN MAXVALUE
-- );

-- ============================
-- 9. 创建视图
-- ============================

-- 任务项目统计视图
CREATE OR REPLACE VIEW v_task_project_stats AS
SELECT 
    tp.task_project_id,
    tp.project_name,
    tp.process_code,
    tp.project_identifier,
    tp.task_count,
    tp.state,
    tp.start_time,
    tp.end_time,
    COALESCE(stats.running_count, 0) AS running_tasks,
    COALESCE(stats.success_count, 0) AS success_tasks,
    COALESCE(stats.failure_count, 0) AS failure_tasks,
    CASE 
        WHEN tp.end_time IS NOT NULL AND tp.start_time IS NOT NULL 
        THEN TIMESTAMPDIFF(SECOND, tp.start_time, tp.end_time) * 1000
        WHEN tp.start_time IS NOT NULL 
        THEN TIMESTAMPDIFF(SECOND, tp.start_time, NOW()) * 1000
        ELSE NULL 
    END AS total_duration,
    tp.ct,
    tp.ut
FROM task_project tp
LEFT JOIN (
    SELECT 
        task_project_id,
        SUM(CASE WHEN state = 0 THEN 1 ELSE 0 END) AS running_count,
        SUM(CASE WHEN state = 1 THEN 1 ELSE 0 END) AS success_count,
        SUM(CASE WHEN state = 2 THEN 1 ELSE 0 END) AS failure_count
    FROM task_instance 
    WHERE deleted = 0
    GROUP BY task_project_id
) stats ON tp.task_project_id = stats.task_project_id
WHERE tp.deleted = 0;

-- 任务性能统计视图
CREATE OR REPLACE VIEW v_task_performance_stats AS
SELECT 
    tc.task_identifier,
    tc.total_count,
    tc.success_count,
    tc.failure_count,
    ROUND(tc.success_count * 100.0 / NULLIF(tc.total_count, 0), 2) AS success_rate,
    tc.avg_time,
    tc.min_time,
    tc.max_time,
    tc.total_time,
    tc.ct,
    tc.ut
FROM task_counter tc
WHERE tc.deleted = 0;

-- 系统资源使用统计视图
CREATE OR REPLACE VIEW v_system_resource_stats AS
SELECT 
    ip,
    DATE(ct) AS stat_date,
    COUNT(*) AS data_points,
    ROUND(AVG(cpu_usage), 2) AS avg_cpu_usage,
    MAX(cpu_usage) AS max_cpu_usage,
    ROUND(AVG(mem_usage), 2) AS avg_mem_usage,
    MAX(mem_usage) AS max_mem_usage,
    ROUND(AVG(disk_usage), 2) AS avg_disk_usage,
    MAX(disk_usage) AS max_disk_usage,
    ROUND(AVG(net_usage), 2) AS avg_net_usage,
    MAX(net_usage) AS max_net_usage
FROM task_runtime
WHERE deleted = 0
GROUP BY ip, DATE(ct);

SET FOREIGN_KEY_CHECKS = 1;

-- 添加注释说明
SELECT 'Database schema created successfully!' AS message;
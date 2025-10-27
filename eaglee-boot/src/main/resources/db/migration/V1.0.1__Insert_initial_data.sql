-- 鹰眼监控系统初始化数据脚本
-- 版本: V1.0.1
-- 描述: 插入系统初始化数据

-- 设置字符集
SET NAMES utf8mb4;

-- ============================
-- 1. 插入系统配置数据
-- ============================

-- 数据采集相关配置
INSERT INTO `task_config` (`config_name`, `config_key`, `config_value`, `config_desc`, `config_type`, `enabled`, `cid`) VALUES
('默认采集间隔', 'collection.default.interval', '30', '系统默认的数据采集间隔，单位：秒', 'number', 1, 1),
('最大采集间隔', 'collection.max.interval', '60', '允许的最大采集间隔，单位：秒', 'number', 1, 1),
('采集重试次数', 'collection.retry.times', '3', '采集失败时的重试次数', 'number', 1, 1),
('采集重试间隔', 'collection.retry.interval', '1000', '采集重试的间隔时间，单位：毫秒', 'number', 1, 1),
('采集批量大小', 'collection.batch.size', '100', '批量保存数据的大小', 'number', 1, 1),
('采集超时时间', 'collection.timeout', '30', '单次采集的超时时间，单位：秒', 'number', 1, 1);

-- 度量计算相关配置
INSERT INTO `task_config` (`config_name`, `config_key`, `config_value`, `config_desc`, `config_type`, `enabled`, `cid`) VALUES
('度量计算间隔', 'metric.calculation.interval', '5', '度量数据计算的间隔时间，单位：分钟', 'number', 1, 1),
('数据保留天数', 'metric.retention.days', '30', '度量数据的保留天数', 'number', 1, 1),
('清理过期数据间隔', 'metric.cleanup.interval', '24', '清理过期数据的间隔时间，单位：小时', 'number', 1, 1),
('聚合线程池大小', 'metric.aggregation.thread.pool.size', '5', '度量数据聚合计算的线程池大小', 'number', 1, 1);

-- 任务管理相关配置
INSERT INTO `task_config` (`config_name`, `config_key`, `config_value`, `config_desc`, `config_type`, `enabled`, `cid`) VALUES
('任务超时时间', 'task.timeout', '30', '任务执行的超时时间，单位：分钟', 'number', 1, 1),
('任务清理间隔', 'task.cleanup.interval', '24', '清理已完成任务的间隔时间，单位：小时', 'number', 1, 1),
('任务状态检查间隔', 'task.status.check.interval', '5', '任务状态检查的间隔时间，单位：分钟', 'number', 1, 1),
('最大重试次数', 'task.max.retry.times', '3', '任务执行失败时的最大重试次数', 'number', 1, 1);

-- 系统性能相关配置
INSERT INTO `task_config` (`config_name`, `config_key`, `config_value`, `config_desc`, `config_type`, `enabled`, `cid`) VALUES
('性能监控开关', 'performance.monitoring.enabled', 'true', '是否启用性能监控功能', 'boolean', 1, 1),
('慢查询阈值', 'performance.slow.query.threshold', '1000', '慢查询的阈值时间，单位：毫秒', 'number', 1, 1),
('SQL日志开关', 'performance.sql.logging.enabled', 'false', '是否启用SQL执行日志', 'boolean', 1, 1);

-- 缓存相关配置
INSERT INTO `task_config` (`config_name`, `config_key`, `config_value`, `config_desc`, `config_type`, `enabled`, `cid`) VALUES
('缓存类型', 'cache.type', 'caffeine', '缓存实现类型：redis, caffeine, none', 'string', 1, 1),
('默认缓存过期时间', 'cache.default.ttl', '30', '默认缓存过期时间，单位：分钟', 'number', 1, 1),
('最大缓存条目数', 'cache.max.size', '10000', '内存缓存的最大条目数', 'number', 1, 1);

-- 告警相关配置
INSERT INTO `task_config` (`config_name`, `config_key`, `config_value`, `config_desc`, `config_type`, `enabled`, `cid`) VALUES
('CPU使用率告警阈值', 'alert.cpu.threshold', '80', 'CPU使用率告警阈值，单位：百分比', 'number', 1, 1),
('内存使用率告警阈值', 'alert.memory.threshold', '85', '内存使用率告警阈值，单位：百分比', 'number', 1, 1),
('磁盘使用率告警阈值', 'alert.disk.threshold', '90', '磁盘使用率告警阈值，单位：百分比', 'number', 1, 1),
('任务执行时间告警阈值', 'alert.task.duration.threshold', '300000', '任务执行时间告警阈值，单位：毫秒', 'number', 1, 1);

-- ============================
-- 2. 插入系统规则数据
-- ============================

-- 监控规则
INSERT INTO `task_rule` (`rule_name`, `rule_type`, `application_type`, `rule_condition`, `rule_action`, `priority`, `enabled`, `cid`) VALUES
('CPU使用率监控规则', 'monitor', 'project', 
'{"metric": "cpu_usage", "operator": "gt", "threshold": 80, "duration": 300}', 
'{"type": "alert", "level": "warning", "message": "CPU使用率超过80%"}', 
10, 1, 1),

('内存使用率监控规则', 'monitor', 'project', 
'{"metric": "mem_usage", "operator": "gt", "threshold": 85, "duration": 300}', 
'{"type": "alert", "level": "warning", "message": "内存使用率超过85%"}', 
10, 1, 1),

('磁盘使用率监控规则', 'monitor', 'project', 
'{"metric": "disk_usage", "operator": "gt", "threshold": 90, "duration": 300}', 
'{"type": "alert", "level": "critical", "message": "磁盘使用率超过90%"}', 
20, 1, 1),

('任务执行超时监控规则', 'monitor', 'task', 
'{"metric": "duration", "operator": "gt", "threshold": 300000}', 
'{"type": "alert", "level": "warning", "message": "任务执行时间超过5分钟"}', 
15, 1, 1);

-- 告警规则
INSERT INTO `task_rule` (`rule_name`, `rule_type`, `application_type`, `rule_condition`, `rule_action`, `priority`, `enabled`, `cid`) VALUES
('任务失败告警规则', 'alert', 'task', 
'{"event": "task_failed", "retry_count": {"operator": "gte", "value": 3}}', 
'{"type": "notification", "channels": ["email", "webhook"], "template": "task_failure"}', 
30, 1, 1),

('系统资源紧张告警规则', 'alert', 'project', 
'{"conditions": [{"metric": "cpu_usage", "operator": "gt", "value": 90}, {"metric": "mem_usage", "operator": "gt", "value": 90}], "logic": "or"}', 
'{"type": "notification", "channels": ["sms", "email"], "template": "resource_critical"}', 
50, 1, 1),

('任务堆积告警规则', 'alert', 'project', 
'{"metric": "pending_tasks", "operator": "gt", "threshold": 100, "duration": 600}', 
'{"type": "notification", "channels": ["webhook"], "template": "task_backlog"}', 
25, 1, 1);

-- 采集规则
INSERT INTO `task_rule` (`rule_name`, `rule_type`, `application_type`, `rule_condition`, `rule_action`, `priority`, `enabled`, `cid`) VALUES
('默认系统资源采集规则', 'collection', 'project', 
'{"schedule": "*/30 * * * * *", "targets": ["cpu", "memory", "disk", "network"]}', 
'{"collector": "system_resource", "interval": 30, "timeout": 10}', 
5, 1, 1),

('高频CPU监控采集规则', 'collection', 'project', 
'{"schedule": "*/10 * * * * *", "condition": {"cpu_usage": {"operator": "gt", "value": 70}}}', 
'{"collector": "cpu_detail", "interval": 10, "timeout": 5}', 
15, 1, 1),

('任务性能数据采集规则', 'collection', 'task', 
'{"events": ["task_start", "task_end", "task_error"]}', 
'{"collector": "task_performance", "metrics": ["duration", "memory_usage", "cpu_time"]}', 
10, 1, 1);

-- ============================
-- 3. 插入示例数据（用于演示和测试）
-- ============================

-- 示例任务项目
INSERT INTO `task_project` (`project_name`, `process_code`, `project_identifier`, `task_count`, `state`, `start_time`, `cid`) VALUES
('用户数据处理项目', 'USER_DATA_PROC', 'user-data-processing-001', 5, 0, NOW(), 1),
('订单同步处理项目', 'ORDER_SYNC_PROC', 'order-sync-processing-001', 3, 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), 1),
('报表生成项目', 'REPORT_GEN', 'report-generation-001', 8, 0, DATE_SUB(NOW(), INTERVAL 30 MINUTE), 1);

-- 示例任务实例
INSERT INTO `task_instance` (`task_project_id`, `task_name`, `task_identifier`, `application_id`, `process_name`, `state`, `start_time`, `end_time`, `duration`, `cid`) VALUES
(1, '用户信息验证', 'user-validation', 'app-001', '用户数据处理流程', 1, DATE_SUB(NOW(), INTERVAL 1 HOUR), DATE_SUB(NOW(), INTERVAL 50 MINUTE), 600000, 1),
(1, '用户数据清洗', 'user-data-cleaning', 'app-001', '用户数据处理流程', 1, DATE_SUB(NOW(), INTERVAL 50 MINUTE), DATE_SUB(NOW(), INTERVAL 40 MINUTE), 600000, 1),
(1, '用户数据转换', 'user-data-transform', 'app-001', '用户数据处理流程', 0, DATE_SUB(NOW(), INTERVAL 40 MINUTE), NULL, NULL, 1),
(2, '订单数据同步', 'order-sync', 'app-002', '订单同步处理流程', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR), 3600000, 1),
(3, '日报表生成', 'daily-report', 'app-003', '报表生成流程', 1, DATE_SUB(NOW(), INTERVAL 30 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE), 600000, 1);

-- 示例任务计数器
INSERT INTO `task_counter` (`task_identifier`, `total_count`, `success_count`, `failure_count`, `total_time`, `avg_time`, `min_time`, `max_time`) VALUES
('user-validation', 150, 145, 5, 90000000, 600000, 300000, 1200000),
('user-data-cleaning', 150, 148, 2, 90000000, 600000, 400000, 900000),
('user-data-transform', 120, 115, 5, 84000000, 700000, 500000, 1500000),
('order-sync', 80, 78, 2, 288000000, 3600000, 3000000, 4200000),
('daily-report', 30, 30, 0, 18000000, 600000, 450000, 800000);

-- 示例运行时数据（最近1小时的数据）
INSERT INTO `task_runtime` (`ip`, `cpu_usage`, `mem_usage`, `disk_usage`, `net_usage`, `ct`) VALUES
('192.168.1.100', 45, 60, 70, 20, DATE_SUB(NOW(), INTERVAL 60 MINUTE)),
('192.168.1.100', 50, 62, 70, 25, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
('192.168.1.100', 55, 65, 71, 30, DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
('192.168.1.100', 48, 63, 71, 22, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('192.168.1.100', 52, 64, 72, 28, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
('192.168.1.100', 47, 61, 72, 24, DATE_SUB(NOW(), INTERVAL 10 MINUTE)),
('192.168.1.101', 35, 55, 65, 15, DATE_SUB(NOW(), INTERVAL 60 MINUTE)),
('192.168.1.101', 38, 57, 65, 18, DATE_SUB(NOW(), INTERVAL 50 MINUTE)),
('192.168.1.101', 42, 59, 66, 22, DATE_SUB(NOW(), INTERVAL 40 MINUTE)),
('192.168.1.101', 40, 58, 66, 20, DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
('192.168.1.101', 44, 60, 67, 25, DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
('192.168.1.101', 39, 56, 67, 19, DATE_SUB(NOW(), INTERVAL 10 MINUTE));

-- 示例度量数据（按小时聚合）
INSERT INTO `task_metric` (`ip`, `time_unit`, `time_value`, `cpu_total`, `mem_total`, `disk_total`, `net_total`, `data_points`) VALUES
('192.168.1.100', 'hour', DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 HOUR), '%Y-%m-%d %H:00:00'), 3000, 3780, 4260, 1470, 60),
('192.168.1.101', 'hour', DATE_FORMAT(DATE_SUB(NOW(), INTERVAL 1 HOUR), '%Y-%m-%d %H:00:00'), 2400, 3420, 3960, 1140, 60),
('192.168.1.100', 'day', DATE_FORMAT(CURDATE(), '%Y-%m-%d 00:00:00'), 72000, 90720, 102240, 35280, 1440),
('192.168.1.101', 'day', DATE_FORMAT(CURDATE(), '%Y-%m-%d 00:00:00'), 57600, 82080, 95040, 27360, 1440);

-- ============================
-- 4. 创建系统用户（如果需要用户管理功能）
-- ============================

-- 注意：这里只是示例，实际项目中可能需要单独的用户管理表
-- CREATE TABLE IF NOT EXISTS `sys_user` (
--   `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
--   `username` varchar(50) NOT NULL COMMENT '用户名',
--   `password` varchar(255) NOT NULL COMMENT '密码（加密）',
--   `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
--   `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
--   `real_name` varchar(100) DEFAULT NULL COMMENT '真实姓名',
--   `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
--   `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
--   `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
--   `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
--   PRIMARY KEY (`user_id`),
--   UNIQUE KEY `uk_username` (`username`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 插入默认管理员用户（密码：admin123，实际使用时应该加密）
-- INSERT INTO `sys_user` (`username`, `password`, `email`, `real_name`, `status`) VALUES
-- ('admin', '$2a$10$7JB720yubVSOfvVWbGRCy.VRac8jKkRjkOYjQADHPkmOqiWw1sMTW', 'admin@eaglee.com', '系统管理员', 1);

-- 添加完成提示
SELECT 'Initial data inserted successfully!' AS message;

-- 显示数据统计
SELECT 
    'task_config' AS table_name, COUNT(*) AS record_count FROM task_config WHERE deleted = 0
UNION ALL
SELECT 
    'task_rule' AS table_name, COUNT(*) AS record_count FROM task_rule WHERE deleted = 0
UNION ALL
SELECT 
    'task_project' AS table_name, COUNT(*) AS record_count FROM task_project WHERE deleted = 0
UNION ALL
SELECT 
    'task_instance' AS table_name, COUNT(*) AS record_count FROM task_instance WHERE deleted = 0
UNION ALL
SELECT 
    'task_counter' AS table_name, COUNT(*) AS record_count FROM task_counter WHERE deleted = 0
UNION ALL
SELECT 
    'task_runtime' AS table_name, COUNT(*) AS record_count FROM task_runtime WHERE deleted = 0
UNION ALL
SELECT 
    'task_metric' AS table_name, COUNT(*) AS record_count FROM task_metric WHERE deleted = 0;
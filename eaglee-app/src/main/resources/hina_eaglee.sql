/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 90300 (9.3.0)
 Source Host           : localhost:13306
 Source Schema         : hina_eaglee

 Target Server Type    : MySQL
 Target Server Version : 90300 (9.3.0)
 File Encoding         : 65001

 Date: 29/10/2025 09:44:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for task_config
-- ----------------------------
DROP TABLE IF EXISTS `task_config`;
CREATE TABLE `task_config`  (
  `task_config_id` bigint NOT NULL AUTO_INCREMENT,
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `data_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `options` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cid` bigint NULL DEFAULT NULL,
  `uid` bigint NULL DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT 0,
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`task_config_id`) USING BTREE,
  UNIQUE INDEX `uk_key`(`key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_counter
-- ----------------------------
DROP TABLE IF EXISTS `task_counter`;
CREATE TABLE `task_counter`  (
  `task_counter_id` bigint NOT NULL AUTO_INCREMENT,
  `project_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `expend_total` bigint NOT NULL DEFAULT 0 COMMENT '总耗时（毫秒）',
  `task_count` bigint NOT NULL DEFAULT 0 COMMENT '任务实例数量',
  `deleted` tinyint NOT NULL DEFAULT 0,
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`task_counter_id`) USING BTREE,
  UNIQUE INDEX `uk_project_task`(`project_identifier` ASC, `task_identifier` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务计数器（动态同步更新）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_instance
-- ----------------------------
DROP TABLE IF EXISTS `task_instance`;
CREATE TABLE `task_instance`  (
  `task_instance_id` bigint NOT NULL AUTO_INCREMENT,
  `task_project_id` bigint NOT NULL,
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'DS中任务编码',
  `task_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '同一类任务标识',
  `application_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avg_time` bigint NOT NULL DEFAULT 0 COMMENT '平均耗时（毫秒）',
  `elapsed_time` bigint NOT NULL DEFAULT 0 COMMENT '当前耗时（毫秒）=now-start_time',
  `duration` bigint NOT NULL DEFAULT 0 COMMENT '实际时长（毫秒）',
  `retried` int NOT NULL DEFAULT 0,
  `state` int NOT NULL,
  `start_time` datetime NOT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cid` bigint NULL DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT 0,
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`task_instance_id`) USING BTREE,
  INDEX `idx_task_project_id`(`task_project_id` ASC) USING BTREE,
  INDEX `idx_task_identifier`(`task_identifier` ASC) USING BTREE,
  INDEX `idx_start_time`(`start_time` ASC) USING BTREE,
  CONSTRAINT `fk_instance_project` FOREIGN KEY (`task_project_id`) REFERENCES `task_project` (`task_project_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务实例' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_metric
-- ----------------------------
DROP TABLE IF EXISTS `task_metric`;
CREATE TABLE `task_metric`  (
  `task_metric_id` bigint NOT NULL AUTO_INCREMENT,
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `time_unit` int NOT NULL COMMENT '时间单位：1=分钟，2=小时，3=天',
  `cpu_total_usage` int NOT NULL,
  `mem_total_usage` int NOT NULL,
  `disk_total_usage` int NOT NULL,
  `net_total_usage` int NOT NULL,
  `cpu_total_minute` int NOT NULL,
  `mem_total_minute` int NOT NULL,
  `disk_total_minute` int NOT NULL,
  `net_total_minute` int NOT NULL,
  `cpu_total_hour` int NOT NULL,
  `mem_total_hour` int NOT NULL,
  `disk_total_hour` int NOT NULL,
  `net_total_hour` int NOT NULL,
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`task_metric_id`) USING BTREE,
  INDEX `idx_ip_timeUnit`(`ip` ASC, `time_unit` ASC, `ct` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务度量（分钟及以上）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_project
-- ----------------------------
DROP TABLE IF EXISTS `task_project`;
CREATE TABLE `task_project`  (
  `task_project_id` bigint NOT NULL AUTO_INCREMENT,
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目名（业务系统定义）',
  `process_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'DS中流程编码',
  `project_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目标识',
  `task_count` int NOT NULL DEFAULT 0 COMMENT '任务数',
  `state` tinyint NOT NULL DEFAULT 0 COMMENT '0:未完成，1:已完成',
  `start_time` datetime NULL DEFAULT NULL COMMENT '实际开始时间（由节点反向更新）',
  `end_time` datetime NULL DEFAULT NULL COMMENT '实际结束时间（由节点反向更新）',
  `deleted` tinyint NOT NULL DEFAULT 0,
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`task_project_id`) USING BTREE,
  INDEX `idx_project_identifier`(`project_identifier` ASC) USING BTREE,
  INDEX `idx_state`(`state` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务项目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_rule
-- ----------------------------
DROP TABLE IF EXISTS `task_rule`;
CREATE TABLE `task_rule`  (
  `task_rule_id` bigint NOT NULL AUTO_INCREMENT,
  `rule_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `config_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `reference_type` int NOT NULL COMMENT '0:项目，1:任务',
  `reference_id` bigint NOT NULL,
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cid` bigint NULL DEFAULT NULL,
  `uid` bigint NULL DEFAULT NULL,
  `deleted` tinyint NOT NULL DEFAULT 0,
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`task_rule_id`) USING BTREE,
  INDEX `idx_reference`(`reference_type` ASC, `reference_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务规则' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_runtime
-- ----------------------------
DROP TABLE IF EXISTS `task_runtime`;
CREATE TABLE `task_runtime`  (
  `task_runtime_id` bigint NOT NULL AUTO_INCREMENT,
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `cpu_usage` int NOT NULL,
  `mem_usage` int NOT NULL,
  `disk_usage` int NOT NULL,
  `net_usage` int NOT NULL,
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint NOT NULL DEFAULT 0,
  `version` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`task_runtime_id`) USING BTREE,
  INDEX `idx_ip_ct`(`ip` ASC, `ct` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务运行时（秒级，<=60且被60整除）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for task_setting
-- ----------------------------
DROP TABLE IF EXISTS `task_setting`;
CREATE TABLE `task_setting`  (
  `task_setting_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务设置ID',
  `task_rule_id` bigint NOT NULL COMMENT '任务规则ID',
  `project_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '项目标识',
  `task_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '同一类任务的标识',
  `code_identifier` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'code标识码（projectIdentifier_taskIdentifier）',
  `ct` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ut` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `cid` bigint NULL DEFAULT NULL COMMENT '创建者ID',
  `uid` bigint NULL DEFAULT NULL COMMENT '更新者ID',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '删除标记',
  `version` int NOT NULL DEFAULT 0 COMMENT '乐观锁',
  PRIMARY KEY (`task_setting_id`) USING BTREE,
  UNIQUE INDEX `uk_code_identifier`(`code_identifier` ASC) USING BTREE,
  INDEX `idx_task_rule_id`(`task_rule_id` ASC) USING BTREE,
  INDEX `idx_project_task`(`project_identifier` ASC, `task_identifier` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务设置' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- 数据库：dows_eaglee（PostgreSQL 版本）
-- 如使用自定义 schema，可把 public 替换为 target_schema

-- 1. 任务项目表
CREATE TABLE IF NOT EXISTS task_project
(
    task_project_id     BIGSERIAL PRIMARY KEY,
    project_name        VARCHAR(255) NOT NULL,
    process_code        VARCHAR(255) NOT NULL,
    project_identifier  VARCHAR(255) NOT NULL,
    task_count          INTEGER      NOT NULL DEFAULT 0,
    state               SMALLINT     NOT NULL DEFAULT 0,
    start_time          TIMESTAMP    NULL,
    end_time            TIMESTAMP    NULL,
    deleted             SMALLINT     NOT NULL DEFAULT 0,
    version             INTEGER      NOT NULL DEFAULT 0
    );
CREATE INDEX IF NOT EXISTS idx_project_identifier ON task_project (project_identifier);
CREATE INDEX IF NOT EXISTS idx_state ON task_project (state);
COMMENT ON TABLE task_project IS '任务项目';
COMMENT ON COLUMN task_project.start_time IS '实际开始时间（由节点反向更新）';
COMMENT ON COLUMN task_project.end_time   IS '实际结束时间（由节点反向更新）';

-- 2. 任务实例表
CREATE TABLE IF NOT EXISTS task_instance
(
    task_instance_id  BIGSERIAL PRIMARY KEY,
    task_project_id   BIGINT       NOT NULL,
    task_name         VARCHAR(255) NOT NULL,
    task_identifier   VARCHAR(255) NOT NULL,
    application_id    VARCHAR(255) NULL,
    process_name      VARCHAR(255) NULL,
    reason            VARCHAR(500) NULL,
    avg_time          BIGINT       NOT NULL DEFAULT 0,
    elapsed_time      BIGINT       NOT NULL DEFAULT 0,
    duration          BIGINT       NOT NULL DEFAULT 0,
    retried           INTEGER      NOT NULL DEFAULT 0,
    state             INTEGER      NOT NULL,
    start_time        TIMESTAMP    NOT NULL,
    end_time          TIMESTAMP    NULL,
    ct                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ut                TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cid               BIGINT       NULL,
    deleted           SMALLINT     NOT NULL DEFAULT 0,
    version           INTEGER      NOT NULL DEFAULT 0
    );
CREATE INDEX IF NOT EXISTS idx_task_project_id ON task_instance (task_project_id);
CREATE INDEX IF NOT EXISTS idx_task_identifier ON task_instance (task_identifier);
CREATE INDEX IF NOT EXISTS idx_start_time ON task_instance (start_time);
ALTER TABLE task_instance
    ADD CONSTRAINT fk_instance_project
        FOREIGN KEY (task_project_id) REFERENCES task_project (task_project_id);
COMMENT ON TABLE task_instance IS '任务实例';
COMMENT ON COLUMN task_instance.avg_time     IS '平均耗时（毫秒）';
COMMENT ON COLUMN task_instance.elapsed_time IS '当前耗时（毫秒）=now-start_time';

-- 3. 任务计数器表（实时累加）
CREATE TABLE IF NOT EXISTS task_counter
(
    task_counter_id    BIGSERIAL PRIMARY KEY,
    project_identifier VARCHAR(255) NOT NULL,
    task_identifier    VARCHAR(255) NOT NULL,
    expend_total       BIGINT       NOT NULL DEFAULT 0,
    task_count         BIGINT       NOT NULL DEFAULT 0,
    deleted            SMALLINT     NOT NULL DEFAULT 0,
    version            INTEGER      NOT NULL DEFAULT 0,
    UNIQUE (project_identifier, task_identifier)
    );
COMMENT ON TABLE task_counter IS '任务计数器（动态同步更新）';

-- 4. 任务运行时（秒级）
CREATE TABLE IF NOT EXISTS task_runtime
(
    task_runtime_id BIGSERIAL PRIMARY KEY,
    ip              INET         NOT NULL,
    cpu_usage       INTEGER      NOT NULL,
    mem_usage       INTEGER      NOT NULL,
    disk_usage      INTEGER      NOT NULL,
    net_usage       INTEGER      NOT NULL,
    ct              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted         SMALLINT     NOT NULL DEFAULT 0,
    version         INTEGER      NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_ip_ct ON task_runtime (ip, ct);
COMMENT ON TABLE task_runtime IS '任务运行时（秒级，<=60且被60整除）';

-- 5. 任务度量（分钟及以上）
CREATE TABLE IF NOT EXISTS task_metric
(
    task_metric_id     BIGSERIAL PRIMARY KEY,
    ip                 INET         NOT NULL,
    time_unit          INTEGER      NOT NULL,
    cpu_total_usage    INTEGER      NOT NULL,
    mem_total_usage    INTEGER      NOT NULL,
    disk_total_usage   INTEGER      NOT NULL,
    net_total_usage    INTEGER      NOT NULL,
    cpu_total_minute   INTEGER      NOT NULL,
    mem_total_minute   INTEGER      NOT NULL,
    disk_total_minute  INTEGER      NOT NULL,
    net_total_minute   INTEGER      NOT NULL,
    cpu_total_hour     INTEGER      NOT NULL,
    mem_total_hour     INTEGER      NOT NULL,
    disk_total_hour    INTEGER      NOT NULL,
    net_total_hour     INTEGER      NOT NULL,
    ct                 TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted            SMALLINT     NOT NULL DEFAULT 0,
    version            INTEGER      NOT NULL DEFAULT 0
);
CREATE INDEX IF NOT EXISTS idx_ip_time_unit ON task_metric (ip, time_unit, ct);
COMMENT ON TABLE task_metric IS '任务度量（分钟及以上）';
COMMENT ON COLUMN task_metric.time_unit IS '时间单位：1=分钟，2=小时，3=天';

-- 6. 任务配置表
CREATE TABLE IF NOT EXISTS task_config
(
    task_config_id BIGSERIAL PRIMARY KEY,
    key            VARCHAR(255) NOT NULL,
    description    VARCHAR(500) NULL,
    data_type      VARCHAR(50)  NULL,
    tag            VARCHAR(50)  NULL,
    options        VARCHAR(500) NULL,
    ct             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ut             TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cid            BIGINT       NULL,
    uid            BIGINT       NULL,
    deleted        SMALLINT     NOT NULL DEFAULT 0,
    version        INTEGER      NOT NULL DEFAULT 0,
    UNIQUE (key)
    );
COMMENT ON TABLE task_config IS '任务配置';

-- 7. 任务规则表
CREATE TABLE IF NOT EXISTS task_rule
(
    task_rule_id    BIGSERIAL PRIMARY KEY,
    rule_name       VARCHAR(255) NOT NULL,
    config_json     TEXT         NOT NULL,
    reference_type  INTEGER      NOT NULL,
    reference_id    BIGINT       NOT NULL,
    ct              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ut              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cid             BIGINT       NULL,
    uid             BIGINT       NULL,
    deleted         SMALLINT     NOT NULL DEFAULT 0,
    version         INTEGER      NOT NULL DEFAULT 0
    );
CREATE INDEX IF NOT EXISTS idx_reference ON task_rule (reference_type, reference_id);
COMMENT ON TABLE task_rule IS '任务规则';
COMMENT ON COLUMN task_rule.reference_type IS '0:项目，1:任务';
# 数据库初始化说明

## 概述

本目录包含鹰眼监控系统的数据库初始化脚本和相关文档。

## 目录结构

```
db/
├── migration/          # Flyway迁移脚本
│   ├── V1.0.0__Create_initial_schema.sql    # 创建初始表结构
│   └── V1.0.1__Insert_initial_data.sql      # 插入初始数据
└── setup/             # 数据库设置脚本
    ├── create_database.sql                   # 创建数据库和用户
    └── README.md                            # 本文档
```

## 使用方法

### 1. 创建数据库

首先使用管理员权限连接MySQL，执行数据库创建脚本：

```bash
mysql -u root -p < src/main/resources/db/setup/create_database.sql
```

### 2. 使用Flyway进行数据库迁移

#### 方法一：使用Maven插件

```bash
# 查看迁移状态
mvn flyway:info

# 执行迁移
mvn flyway:migrate

# 验证迁移
mvn flyway:validate

# 清理数据库（仅开发环境）
mvn flyway:clean
```

#### 方法二：使用Spring Boot自动迁移

启动应用时，Spring Boot会自动执行Flyway迁移：

```bash
java -jar eaglee-boot-1.0.0-SNAPSHOT.jar
```

### 3. 环境配置

#### 开发环境

```yaml
spring:
  profiles:
    active: dev
  datasource:
    url: jdbc:mysql://localhost:3306/eaglee_dev
    username: eaglee_dev
    password: eaglee_dev
```

#### 测试环境

```yaml
spring:
  profiles:
    active: test
  datasource:
    url: jdbc:h2:mem:eaglee_test
    driver-class-name: org.h2.Driver
```

#### 生产环境

```yaml
spring:
  profiles:
    active: prod
  datasource:
    url: jdbc:mysql://mysql-server:3306/eaglee_prod
    username: eaglee_prod
    password: ${DB_PASSWORD}
```

## 数据库表结构

### 核心业务表

1. **task_project** - 任务项目表
   - 存储任务项目的基本信息
   - 包含项目状态、开始结束时间等

2. **task_instance** - 任务实例表
   - 存储具体的任务执行实例
   - 记录任务执行状态和性能数据

3. **task_counter** - 任务计数器表
   - 统计任务执行次数和性能指标
   - 用于计算平均耗时等度量数据

### 监控数据表

4. **task_runtime** - 任务运行时数据表
   - 存储系统资源使用情况
   - 按秒级采集CPU、内存、磁盘、网络数据

5. **task_metric** - 任务度量数据表
   - 存储聚合后的度量数据
   - 按分钟、小时、天三个维度统计

### 配置管理表

6. **task_config** - 任务配置表
   - 存储系统配置参数
   - 支持动态配置更新

7. **task_rule** - 任务规则表
   - 存储监控规则和告警规则
   - 支持项目级和任务级规则

## 索引策略

### 主要索引

- 主键索引：所有表都有自增主键
- 唯一索引：防止重复数据
- 复合索引：优化常用查询

### 性能优化

- 时间字段索引：优化时间范围查询
- 状态字段索引：优化状态过滤查询
- 软删除索引：优化逻辑删除查询

## 数据保留策略

### 运行时数据

- 保留周期：30天
- 清理策略：定时任务清理过期数据
- 归档策略：重要数据归档到度量表

### 度量数据

- 分钟级数据：保留7天
- 小时级数据：保留30天
- 天级数据：保留1年

### 业务数据

- 任务项目：永久保留（支持逻辑删除）
- 任务实例：保留90天
- 配置数据：永久保留

## 备份策略

### 开发环境

- 无需备份
- 可随时重建

### 测试环境

- 每日备份
- 保留7天

### 生产环境

- 每日全量备份
- 每小时增量备份
- 保留30天全量备份
- 保留7天增量备份

## 监控指标

### 数据库性能

- 连接数使用率
- 查询响应时间
- 慢查询统计
- 锁等待时间

### 存储使用

- 表空间使用率
- 索引使用效率
- 数据增长趋势

## 故障排查

### 常见问题

1. **连接失败**
   - 检查数据库服务状态
   - 验证连接参数
   - 确认用户权限

2. **迁移失败**
   - 检查Flyway配置
   - 验证SQL语法
   - 查看错误日志

3. **性能问题**
   - 分析慢查询日志
   - 检查索引使用情况
   - 优化查询语句

### 日志位置

- 应用日志：`logs/eaglee.log`
- 数据库日志：MySQL错误日志
- Flyway日志：控制台输出

## 版本管理

### 迁移脚本命名规范

- 格式：`V{版本号}__{描述}.sql`
- 示例：`V1.0.0__Create_initial_schema.sql`
- 版本号递增，不可重复

### 变更流程

1. 创建迁移脚本
2. 本地测试验证
3. 提交代码审查
4. 部署到测试环境
5. 验证功能正常
6. 部署到生产环境

## 联系方式

如有问题，请联系：

- 开发团队：dev@dows.com
- 运维团队：ops@dows.com
- 项目经理：pm@dows.com
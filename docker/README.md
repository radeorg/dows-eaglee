# 鹰眼监控系统 Docker 部署指南

## 概述

本目录包含鹰眼监控系统的Docker容器化部署配置，支持开发、测试和生产环境的一键部署。

## 目录结构

```
docker/
├── config/                 # 应用配置文件
├── mysql/                  # MySQL配置
│   ├── conf.d/
│   │   └── my.cnf         # MySQL配置文件
│   └── init/
│       └── 01-create-databases.sql  # 数据库初始化脚本
├── redis/                  # Redis配置
│   └── redis.conf         # Redis配置文件
├── prometheus/             # Prometheus监控配置
│   └── prometheus.yml     # Prometheus配置文件
├── grafana/               # Grafana可视化配置
│   └── provisioning/      # Grafana自动配置
├── scripts/               # 部署脚本
│   └── deploy.sh         # 部署脚本
└── README.md             # 本文档
```

## 快速开始

### 1. 环境准备

确保已安装以下软件：

- Docker (>= 20.10)
- Docker Compose (>= 2.0)

### 2. 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑环境变量文件
vim .env
```

### 3. 启动服务

```bash
# 构建并启动所有服务
docker-compose up -d

# 或使用部署脚本
./docker/scripts/deploy.sh start -d
```

### 4. 验证部署

```bash
# 查看服务状态
docker-compose ps

# 查看应用日志
docker-compose logs -f eaglee-app

# 访问应用
curl http://localhost:8080/eaglee/actuator/health
```

## 服务组件

### 核心服务

| 服务名 | 端口 | 描述 |
|--------|------|------|
| eaglee-app | 8080 | 鹰眼监控系统主应用 |
| eaglee-mysql | 3306 | MySQL数据库 |
| eaglee-redis | 6379 | Redis缓存 |

### 监控服务（可选）

| 服务名 | 端口 | 描述 |
|--------|------|------|
| eaglee-prometheus | 9091 | Prometheus监控 |
| eaglee-grafana | 3000 | Grafana可视化 |

## 部署模式

### 开发环境

```bash
# 设置开发环境
export SPRING_PROFILES_ACTIVE=dev

# 启动开发环境服务
docker-compose up -d eaglee-app eaglee-mysql eaglee-redis
```

### 测试环境

```bash
# 设置测试环境
export SPRING_PROFILES_ACTIVE=test

# 启动测试环境服务
docker-compose up -d
```

### 生产环境

```bash
# 设置生产环境
export SPRING_PROFILES_ACTIVE=prod

# 启动生产环境服务（包含监控）
docker-compose --profile monitoring up -d
```

## 配置说明

### 环境变量配置

主要环境变量说明：

```bash
# 应用配置
SPRING_PROFILES_ACTIVE=prod    # 环境配置
APP_PORT=8080                  # 应用端口
MANAGEMENT_PORT=9090           # 管理端口

# 数据库配置
DB_NAME=eaglee_prod           # 数据库名
DB_USERNAME=eaglee_prod       # 数据库用户名
DB_PASSWORD=secure_password   # 数据库密码

# Redis配置
CACHE_TYPE=redis              # 缓存类型
REDIS_PASSWORD=               # Redis密码

# 资源限制
MEMORY_LIMIT=2G               # 内存限制
CPU_LIMIT=2.0                 # CPU限制
```

### 数据持久化

数据卷映射：

```yaml
volumes:
  - ./data/logs:/app/logs           # 应用日志
  - ./data/mysql:/var/lib/mysql     # MySQL数据
  - ./data/redis:/data              # Redis数据
```

### 网络配置

服务间通信使用内部网络：

```yaml
networks:
  eaglee-network:
    driver: bridge
    ipam:
      config:
        - subnet: 172.20.0.0/16
```

## 运维操作

### 日志管理

```bash
# 查看所有服务日志
docker-compose logs

# 查看特定服务日志
docker-compose logs -f eaglee-app

# 查看最近100行日志
docker-compose logs --tail=100 eaglee-app
```

### 数据备份

```bash
# 使用部署脚本备份
./docker/scripts/deploy.sh backup

# 手动备份MySQL
docker-compose exec eaglee-mysql mysqldump -u root -p --all-databases > backup.sql

# 手动备份Redis
docker-compose exec eaglee-redis redis-cli BGSAVE
```

### 数据恢复

```bash
# 使用部署脚本恢复
./docker/scripts/deploy.sh restore /path/to/backup

# 手动恢复MySQL
docker-compose exec -T eaglee-mysql mysql -u root -p < backup.sql
```

### 服务扩缩容

```bash
# 扩展应用实例
docker-compose up -d --scale eaglee-app=3

# 限制资源使用
docker-compose up -d --memory=1g --cpus=1.0
```

### 健康检查

```bash
# 检查服务健康状态
docker-compose ps

# 查看健康检查日志
docker inspect --format='{{json .State.Health}}' eaglee-app

# 手动健康检查
curl http://localhost:8080/eaglee/actuator/health
```

## 监控告警

### Prometheus监控

访问地址：http://localhost:9091

主要监控指标：
- 应用性能指标
- JVM内存使用
- 数据库连接池
- HTTP请求统计

### Grafana可视化

访问地址：http://localhost:3000
默认账号：admin/admin

预配置仪表板：
- 应用概览
- JVM监控
- 数据库监控
- 系统资源监控

## 故障排查

### 常见问题

1. **容器启动失败**
   ```bash
   # 查看容器日志
   docker-compose logs eaglee-app
   
   # 检查容器状态
   docker-compose ps
   ```

2. **数据库连接失败**
   ```bash
   # 检查数据库服务
   docker-compose exec eaglee-mysql mysql -u root -p -e "SHOW DATABASES;"
   
   # 检查网络连接
   docker-compose exec eaglee-app nc -zv eaglee-mysql 3306
   ```

3. **内存不足**
   ```bash
   # 查看资源使用
   docker stats
   
   # 调整内存限制
   export MEMORY_LIMIT=4G
   docker-compose up -d
   ```

4. **磁盘空间不足**
   ```bash
   # 清理未使用的镜像
   docker system prune -f
   
   # 清理日志文件
   docker-compose exec eaglee-app find /app/logs -name "*.log" -mtime +7 -delete
   ```

### 性能优化

1. **JVM调优**
   ```bash
   export JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC"
   ```

2. **数据库优化**
   - 调整MySQL配置参数
   - 优化索引和查询
   - 配置读写分离

3. **缓存优化**
   - 调整Redis内存配置
   - 优化缓存策略
   - 配置集群模式

## 安全配置

### 网络安全

```yaml
# 限制端口暴露
ports:
  - "127.0.0.1:8080:8080"  # 只绑定本地接口

# 使用内部网络
networks:
  - eaglee-network
```

### 访问控制

```bash
# 设置数据库密码
export DB_PASSWORD="$(openssl rand -base64 32)"

# 设置Redis密码
export REDIS_PASSWORD="$(openssl rand -base64 32)"
```

### 数据加密

```yaml
# 启用SSL
environment:
  - DB_SSL=true
  - REDIS_TLS=true
```

## 升级部署

### 滚动更新

```bash
# 构建新镜像
docker-compose build eaglee-app

# 滚动更新
docker-compose up -d --no-deps eaglee-app
```

### 蓝绿部署

```bash
# 启动新版本
docker-compose -f docker-compose.blue.yml up -d

# 切换流量
# 更新负载均衡器配置

# 停止旧版本
docker-compose -f docker-compose.green.yml down
```

## 联系方式

如有问题，请联系：

- 开发团队：dev@dows.com
- 运维团队：ops@dows.com
- 技术支持：support@dows.com
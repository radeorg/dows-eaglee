#!/bin/bash

# 鹰眼监控系统 Docker 容器启动脚本

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
}

log_debug() {
    if [[ "${DEBUG}" == "true" ]]; then
        echo -e "${BLUE}[DEBUG]${NC} $(date '+%Y-%m-%d %H:%M:%S') - $1"
    fi
}

# 显示启动信息
show_banner() {
    cat << 'EOF'
 ______            _           
|  ____|          | |          
| |__   __ _  __ _| | ___  ___ 
|  __| / _` |/ _` | |/ _ \/ _ \
| |___| (_| | (_| | |  __/  __/
|______\__,_|\__, |_|\___|\___|
              __/ |            
             |___/             

鹰眼监控系统 v1.0.0
企业级任务监控和性能分析平台
EOF
}

# 等待数据库连接
wait_for_database() {
    local host="${DB_HOST:-localhost}"
    local port="${DB_PORT:-3306}"
    local timeout="${DB_WAIT_TIMEOUT:-60}"
    
    log_info "等待数据库连接: ${host}:${port}"
    
    local count=0
    while ! nc -z "${host}" "${port}"; do
        if [ ${count} -ge ${timeout} ]; then
            log_error "数据库连接超时: ${host}:${port}"
            exit 1
        fi
        log_debug "等待数据库连接... (${count}/${timeout})"
        sleep 1
        count=$((count + 1))
    done
    
    log_info "数据库连接成功: ${host}:${port}"
}

# 等待Redis连接（如果启用）
wait_for_redis() {
    if [[ "${CACHE_TYPE}" == "redis" ]]; then
        local host="${REDIS_HOST:-localhost}"
        local port="${REDIS_PORT:-6379}"
        local timeout="${REDIS_WAIT_TIMEOUT:-30}"
        
        log_info "等待Redis连接: ${host}:${port}"
        
        local count=0
        while ! nc -z "${host}" "${port}"; do
            if [ ${count} -ge ${timeout} ]; then
                log_warn "Redis连接超时，将使用本地缓存: ${host}:${port}"
                export CACHE_TYPE=caffeine
                break
            fi
            log_debug "等待Redis连接... (${count}/${timeout})"
            sleep 1
            count=$((count + 1))
        done
        
        if nc -z "${host}" "${port}"; then
            log_info "Redis连接成功: ${host}:${port}"
        fi
    fi
}

# 设置JVM参数
setup_jvm_options() {
    # 基础JVM参数
    local jvm_opts="${JAVA_OPTS:-}"
    
    # 内存设置
    local heap_size="${HEAP_SIZE:-1024m}"
    local metaspace_size="${METASPACE_SIZE:-256m}"
    
    # GC设置
    local gc_opts="-XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxGCPauseMillis=200"
    
    # 监控和调试参数
    local monitoring_opts=""
    if [[ "${ENABLE_JMX}" == "true" ]]; then
        monitoring_opts="${monitoring_opts} -Dcom.sun.management.jmxremote"
        monitoring_opts="${monitoring_opts} -Dcom.sun.management.jmxremote.port=${JMX_PORT:-9999}"
        monitoring_opts="${monitoring_opts} -Dcom.sun.management.jmxremote.authenticate=false"
        monitoring_opts="${monitoring_opts} -Dcom.sun.management.jmxremote.ssl=false"
    fi
    
    # 性能调优参数
    local performance_opts="-server -XX:+OptimizeStringConcat -XX:+UseStringDeduplication"
    
    # 错误处理参数
    local error_opts="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/app/logs/"
    
    # 组合所有参数
    export JAVA_OPTS="${jvm_opts} -Xms${heap_size} -Xmx${heap_size} -XX:MetaspaceSize=${metaspace_size} ${gc_opts} ${monitoring_opts} ${performance_opts} ${error_opts}"
    
    log_info "JVM参数: ${JAVA_OPTS}"
}

# 设置应用参数
setup_app_options() {
    # Spring Boot参数
    local spring_opts=""
    
    # 配置文件位置
    if [[ -f "/app/config/application.yml" ]]; then
        spring_opts="${spring_opts} --spring.config.additional-location=file:/app/config/"
        log_info "使用外部配置文件: /app/config/application.yml"
    fi
    
    # 日志配置
    spring_opts="${spring_opts} --logging.file.path=/app/logs"
    
    # 时区设置
    export TZ="${TZ:-Asia/Shanghai}"
    spring_opts="${spring_opts} --spring.jackson.time-zone=${TZ}"
    
    export SPRING_OPTS="${spring_opts}"
    log_info "Spring Boot参数: ${SPRING_OPTS}"
}

# 创建必要的目录
create_directories() {
    local dirs=("/app/logs" "/app/data" "/app/temp")
    
    for dir in "${dirs[@]}"; do
        if [[ ! -d "${dir}" ]]; then
            mkdir -p "${dir}"
            log_info "创建目录: ${dir}"
        fi
    done
}

# 检查磁盘空间
check_disk_space() {
    local min_space="${MIN_DISK_SPACE:-1048576}" # 1GB in KB
    local available_space=$(df /app | tail -1 | awk '{print $4}')
    
    if [[ ${available_space} -lt ${min_space} ]]; then
        log_warn "磁盘空间不足: ${available_space}KB 可用，建议至少 ${min_space}KB"
    else
        log_info "磁盘空间检查通过: ${available_space}KB 可用"
    fi
}

# 显示系统信息
show_system_info() {
    log_info "系统信息:"
    log_info "  - 主机名: $(hostname)"
    log_info "  - 操作系统: $(uname -s) $(uname -r)"
    log_info "  - Java版本: $(java -version 2>&1 | head -n 1)"
    log_info "  - 内存信息: $(free -h | grep Mem | awk '{print $2 " 总计, " $3 " 已用, " $7 " 可用"}')"
    log_info "  - CPU信息: $(nproc) 核心"
    log_info "  - 时区: ${TZ}"
    log_info "  - 环境: ${SPRING_PROFILES_ACTIVE:-default}"
}

# 信号处理
handle_signal() {
    log_info "接收到停止信号，正在优雅关闭..."
    if [[ -n "${APP_PID}" ]]; then
        kill -TERM "${APP_PID}"
        wait "${APP_PID}"
    fi
    log_info "应用已停止"
    exit 0
}

# 主函数
main() {
    # 显示启动横幅
    show_banner
    
    # 设置信号处理
    trap handle_signal SIGTERM SIGINT
    
    # 显示系统信息
    show_system_info
    
    # 创建必要目录
    create_directories
    
    # 检查磁盘空间
    check_disk_space
    
    # 等待依赖服务
    if [[ "${WAIT_FOR_DB}" != "false" ]]; then
        wait_for_database
    fi
    
    if [[ "${WAIT_FOR_REDIS}" == "true" ]]; then
        wait_for_redis
    fi
    
    # 设置JVM和应用参数
    setup_jvm_options
    setup_app_options
    
    # 启动应用
    log_info "启动鹰眼监控系统..."
    log_info "命令: $*"
    
    # 执行传入的命令
    exec "$@" ${SPRING_OPTS} &
    APP_PID=$!
    
    log_info "应用已启动，PID: ${APP_PID}"
    
    # 等待应用进程
    wait "${APP_PID}"
    local exit_code=$?
    
    log_info "应用已退出，退出码: ${exit_code}"
    exit ${exit_code}
}

# 如果脚本被直接执行
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi
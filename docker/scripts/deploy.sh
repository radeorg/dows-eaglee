#!/bin/bash

# 鹰眼监控系统 Docker 部署脚本

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 配置变量
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "${SCRIPT_DIR}/../.." && pwd)"
ENV_FILE="${PROJECT_ROOT}/.env"
COMPOSE_FILE="${PROJECT_ROOT}/docker-compose.yml"

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

# 显示帮助信息
show_help() {
    cat << EOF
鹰眼监控系统 Docker 部署脚本

用法: $0 [选项] [命令]

命令:
  build       构建Docker镜像
  start       启动服务
  stop        停止服务
  restart     重启服务
  logs        查看日志
  status      查看服务状态
  clean       清理资源
  backup      备份数据
  restore     恢复数据
  update      更新服务

选项:
  -e, --env FILE      指定环境变量文件 (默认: .env)
  -f, --file FILE     指定docker-compose文件 (默认: docker-compose.yml)
  -p, --profile NAME  指定配置文件 (dev|test|prod|monitoring)
  -d, --detach        后台运行
  -v, --verbose       详细输出
  -h, --help          显示帮助信息

示例:
  $0 build                    # 构建镜像
  $0 start -d                 # 后台启动服务
  $0 start --profile monitoring  # 启动包含监控的服务
  $0 logs eaglee-app          # 查看应用日志
  $0 backup                   # 备份数据
  $0 clean                    # 清理所有资源

EOF
}

# 检查依赖
check_dependencies() {
    local deps=("docker" "docker-compose")
    
    for dep in "${deps[@]}"; do
        if ! command -v "${dep}" &> /dev/null; then
            log_error "依赖 ${dep} 未安装"
            exit 1
        fi
    done
    
    log_info "依赖检查通过"
}

# 检查环境文件
check_env_file() {
    if [[ ! -f "${ENV_FILE}" ]]; then
        if [[ -f "${PROJECT_ROOT}/.env.example" ]]; then
            log_warn "环境文件不存在，从示例文件创建"
            cp "${PROJECT_ROOT}/.env.example" "${ENV_FILE}"
            log_info "请编辑 ${ENV_FILE} 文件配置环境变量"
        else
            log_error "环境文件和示例文件都不存在"
            exit 1
        fi
    fi
    
    log_info "环境文件检查通过: ${ENV_FILE}"
}

# 创建数据目录
create_data_directories() {
    local dirs=(
        "${PROJECT_ROOT}/data/logs"
        "${PROJECT_ROOT}/data/app"
        "${PROJECT_ROOT}/data/mysql"
        "${PROJECT_ROOT}/data/mysql-logs"
        "${PROJECT_ROOT}/data/redis"
        "${PROJECT_ROOT}/data/prometheus"
        "${PROJECT_ROOT}/data/grafana"
    )
    
    for dir in "${dirs[@]}"; do
        if [[ ! -d "${dir}" ]]; then
            mkdir -p "${dir}"
            log_info "创建目录: ${dir}"
        fi
    done
    
    # 设置权限
    chmod -R 755 "${PROJECT_ROOT}/data"
    log_info "数据目录创建完成"
}

# 构建镜像
build_images() {
    log_info "开始构建Docker镜像..."
    
    cd "${PROJECT_ROOT}"
    
    # 构建应用镜像
    docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" build ${VERBOSE:+--progress=plain} eaglee-app
    
    log_info "Docker镜像构建完成"
}

# 启动服务
start_services() {
    log_info "开始启动服务..."
    
    cd "${PROJECT_ROOT}"
    
    # 构建命令
    local cmd="docker-compose --env-file ${ENV_FILE} -f ${COMPOSE_FILE}"
    
    if [[ -n "${PROFILE}" ]]; then
        cmd="${cmd} --profile ${PROFILE}"
    fi
    
    cmd="${cmd} up"
    
    if [[ "${DETACH}" == "true" ]]; then
        cmd="${cmd} -d"
    fi
    
    log_info "执行命令: ${cmd}"
    eval "${cmd}"
    
    if [[ "${DETACH}" == "true" ]]; then
        log_info "服务已在后台启动"
        show_status
    fi
}

# 停止服务
stop_services() {
    log_info "开始停止服务..."
    
    cd "${PROJECT_ROOT}"
    
    local cmd="docker-compose --env-file ${ENV_FILE} -f ${COMPOSE_FILE}"
    
    if [[ -n "${PROFILE}" ]]; then
        cmd="${cmd} --profile ${PROFILE}"
    fi
    
    cmd="${cmd} down"
    
    log_info "执行命令: ${cmd}"
    eval "${cmd}"
    
    log_info "服务已停止"
}

# 重启服务
restart_services() {
    log_info "重启服务..."
    stop_services
    sleep 2
    start_services
}

# 查看日志
show_logs() {
    local service="${1:-}"
    
    cd "${PROJECT_ROOT}"
    
    local cmd="docker-compose --env-file ${ENV_FILE} -f ${COMPOSE_FILE} logs"
    
    if [[ -n "${service}" ]]; then
        cmd="${cmd} ${service}"
    fi
    
    if [[ "${FOLLOW}" == "true" ]]; then
        cmd="${cmd} -f"
    fi
    
    eval "${cmd}"
}

# 查看状态
show_status() {
    log_info "服务状态:"
    
    cd "${PROJECT_ROOT}"
    
    docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" ps
    
    echo
    log_info "容器资源使用情况:"
    docker stats --no-stream --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}\t{{.NetIO}}\t{{.BlockIO}}"
}

# 清理资源
clean_resources() {
    log_warn "这将删除所有容器、镜像和数据卷，是否继续? (y/N)"
    read -r response
    
    if [[ "${response}" =~ ^[Yy]$ ]]; then
        log_info "开始清理资源..."
        
        cd "${PROJECT_ROOT}"
        
        # 停止并删除容器
        docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" down -v --remove-orphans
        
        # 删除镜像
        docker rmi $(docker images "dows/eaglee-*" -q) 2>/dev/null || true
        
        # 清理未使用的资源
        docker system prune -f
        
        log_info "资源清理完成"
    else
        log_info "取消清理操作"
    fi
}

# 备份数据
backup_data() {
    local backup_dir="${PROJECT_ROOT}/backups/$(date +%Y%m%d_%H%M%S)"
    
    log_info "开始备份数据到: ${backup_dir}"
    
    mkdir -p "${backup_dir}"
    
    # 备份MySQL数据
    if docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" ps eaglee-mysql | grep -q "Up"; then
        log_info "备份MySQL数据..."
        docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" exec -T eaglee-mysql \
            mysqldump -u root -p"${MYSQL_ROOT_PASSWORD}" --all-databases > "${backup_dir}/mysql_backup.sql"
    fi
    
    # 备份Redis数据
    if docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" ps eaglee-redis | grep -q "Up"; then
        log_info "备份Redis数据..."
        docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" exec -T eaglee-redis \
            redis-cli BGSAVE
        sleep 5
        docker cp $(docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" ps -q eaglee-redis):/data/dump.rdb "${backup_dir}/"
    fi
    
    # 备份应用数据
    if [[ -d "${PROJECT_ROOT}/data" ]]; then
        log_info "备份应用数据..."
        tar -czf "${backup_dir}/app_data.tar.gz" -C "${PROJECT_ROOT}" data/
    fi
    
    log_info "数据备份完成: ${backup_dir}"
}

# 恢复数据
restore_data() {
    local backup_dir="${1}"
    
    if [[ -z "${backup_dir}" ]]; then
        log_error "请指定备份目录"
        exit 1
    fi
    
    if [[ ! -d "${backup_dir}" ]]; then
        log_error "备份目录不存在: ${backup_dir}"
        exit 1
    fi
    
    log_warn "这将覆盖现有数据，是否继续? (y/N)"
    read -r response
    
    if [[ "${response}" =~ ^[Yy]$ ]]; then
        log_info "开始恢复数据从: ${backup_dir}"
        
        # 恢复MySQL数据
        if [[ -f "${backup_dir}/mysql_backup.sql" ]]; then
            log_info "恢复MySQL数据..."
            docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" exec -T eaglee-mysql \
                mysql -u root -p"${MYSQL_ROOT_PASSWORD}" < "${backup_dir}/mysql_backup.sql"
        fi
        
        # 恢复Redis数据
        if [[ -f "${backup_dir}/dump.rdb" ]]; then
            log_info "恢复Redis数据..."
            docker cp "${backup_dir}/dump.rdb" $(docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" ps -q eaglee-redis):/data/
            docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" restart eaglee-redis
        fi
        
        # 恢复应用数据
        if [[ -f "${backup_dir}/app_data.tar.gz" ]]; then
            log_info "恢复应用数据..."
            tar -xzf "${backup_dir}/app_data.tar.gz" -C "${PROJECT_ROOT}"
        fi
        
        log_info "数据恢复完成"
    else
        log_info "取消恢复操作"
    fi
}

# 更新服务
update_services() {
    log_info "开始更新服务..."
    
    # 拉取最新镜像
    cd "${PROJECT_ROOT}"
    docker-compose --env-file "${ENV_FILE}" -f "${COMPOSE_FILE}" pull
    
    # 重新构建应用镜像
    build_images
    
    # 重启服务
    restart_services
    
    log_info "服务更新完成"
}

# 解析命令行参数
parse_args() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -e|--env)
                ENV_FILE="$2"
                shift 2
                ;;
            -f|--file)
                COMPOSE_FILE="$2"
                shift 2
                ;;
            -p|--profile)
                PROFILE="$2"
                shift 2
                ;;
            -d|--detach)
                DETACH="true"
                shift
                ;;
            -v|--verbose)
                VERBOSE="true"
                DEBUG="true"
                shift
                ;;
            -h|--help)
                show_help
                exit 0
                ;;
            build|start|stop|restart|logs|status|clean|backup|restore|update)
                COMMAND="$1"
                shift
                ;;
            *)
                if [[ -z "${COMMAND}" ]]; then
                    log_error "未知参数: $1"
                    show_help
                    exit 1
                else
                    ARGS+=("$1")
                    shift
                fi
                ;;
        esac
    done
}

# 主函数
main() {
    # 解析参数
    parse_args "$@"
    
    # 检查命令
    if [[ -z "${COMMAND}" ]]; then
        log_error "请指定命令"
        show_help
        exit 1
    fi
    
    # 检查依赖
    check_dependencies
    
    # 检查环境文件
    check_env_file
    
    # 创建数据目录
    create_data_directories
    
    # 执行命令
    case "${COMMAND}" in
        build)
            build_images
            ;;
        start)
            start_services
            ;;
        stop)
            stop_services
            ;;
        restart)
            restart_services
            ;;
        logs)
            FOLLOW="true"
            show_logs "${ARGS[0]}"
            ;;
        status)
            show_status
            ;;
        clean)
            clean_resources
            ;;
        backup)
            backup_data
            ;;
        restore)
            restore_data "${ARGS[0]}"
            ;;
        update)
            update_services
            ;;
        *)
            log_error "未知命令: ${COMMAND}"
            show_help
            exit 1
            ;;
    esac
}

# 如果脚本被直接执行
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi
package com.hina.eaglee.sql;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hina.eaglee.cache.TaskSettingHandler;
import com.hina.eaglee.dao.TaskMetricDao;
import com.hina.eaglee.dao.TaskRuntimeDao;
import com.hina.eaglee.entity.TaskMetricEntity;
import com.hina.eaglee.entity.TaskRuntimeEntity;
import com.hina.eaglee.exception.BusinessException;
import com.hina.eaglee.request.TaskMetricAnalyseRequest;
import com.hina.eaglee.request.TaskMetricQueryRequest;
import com.hina.eaglee.request.TaskRuntimePageRequest;
import com.hina.eaglee.request.TaskRuntimeSaveRequest;
import com.hina.eaglee.response.TaskMetricQueryResponse;
import com.hina.eaglee.response.TaskRuntimeResponse;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;


@Slf4j
@RequiredArgsConstructor
@Component
public class TaskMetricHandler {

    private final TaskMetricDao taskMetricDao;
    private final TaskRuntimeDao taskRuntimeDao;

    private final TaskSettingHandler taskCacheHandler;

    private final ThreadPoolExecutor threadPoolExecutor;


    /**
     * 保存运行时节点指标并汇总指标数据（累计）
     * 读取配置的统计规则,进行处理
     *
     * @param request
     * @return
     */
    public Long save(TaskRuntimeSaveRequest request) {
        log.info("保存运行时节点指标：{}", JSONUtil.toJsonStr(request));

        // 针对节点统计指标数据
        TaskRuntimeEntity taskRuntimeEntity = new TaskRuntimeEntity();
        //BeanUtils.copyProperties(request, taskRuntimeEntity);
        taskRuntimeEntity.setIp(request.getIp());
        taskRuntimeEntity.setCpuUsage(request.getCpuUsage());
        taskRuntimeEntity.setMemUsage(request.getMemUsage());
        taskRuntimeEntity.setDiskUsage(request.getDiskUsage());
        taskRuntimeEntity.setNetUsage(request.getNetUsage());
        taskRuntimeEntity.setHostTime(request.getHostTime());
        if (taskRuntimeDao.save(taskRuntimeEntity)) {
            log.info("运行时节点指标保存成功，ID: {}", taskRuntimeEntity.getTaskRuntimeId());
            check(request);
            return taskRuntimeEntity.getTaskRuntimeId();
        }
        throw new BusinessException(BusinessException.OPERATION_FAILED, "保存运行时节点指标失败");
    }


    /**
     * 运行时节点指标数据检查
     *
     * @param request
     */
    public void check(TaskRuntimeSaveRequest request) {

//        MetricSetting metricSetting = taskCacheHandler.getMetricSetting(request.getIp());
        String applicationId = request.getApplicationId();
        String processInstanceId = request.getProcessInstanceId();
        String taskInstanceId = request.getTaskInstanceId();
        String codeIdentifier = request.getCodeIdentifier();
        /**
         * todo 1.根据流程实例ID, 查询TaskProject表，找到项目标识
         *      - 根据项目标识, 查询项目配置表，找到项目配置TaskSetting表，找到这类任务的运行时指标统计规则
         *      - 动态计算，触发规则
         */
//        TaskSetting taskSetting = taskCacheHandler.getTaskSetting(codeIdentifier);
//        if (taskSetting != null) {
//            // todo 根据任务配置的触发告警规则，判断是否触发告警，此处通过下线程池提交异步处理
//            threadPoolExecutor.execute(() -> {
//                // todo 告警处理
//
//
//            });
//        }



        /**
         * todo 2.统计指标数据，根据配置的统计规则，进行统计
         */
        /*if (metricSetting != null) {
            Map<MetricUnit, Integer> metricUnitMap = metricSetting.getMetricUnitMap();
            Set<MetricUnit> metricUnits = metricUnitMap.keySet();

            // 获取当前请求的时间，并截断到小时级别
            LocalDateTime minuteAlignedTime = request.getHostTime().truncatedTo(ChronoUnit.MINUTES);
            LocalDateTime hourAlignedTime = request.getHostTime().truncatedTo(ChronoUnit.HOURS);
            LocalDateTime dayAlignedTime = request.getHostTime().truncatedTo(ChronoUnit.DAYS);
            int timeUnit = 0;
            LocalDateTime alignTime = null;
            for (MetricUnit metricUnit : metricUnits) {
                QueryWrapper queryWrapper = QueryWrapper.create().from(TaskMetricEntity.class);
                switch (metricUnit) {
                    case MINUTE -> {
                        // 分钟单位，每分钟 统计一次指标数据
                        queryWrapper.and(TaskMetricEntity::getTimeUnit).eq(MetricUnit.MINUTE.getValue())
                                .and(TaskMetricEntity::getIp).eq(request.getIp())
                                .and(TaskMetricEntity::getTimeValue).eq(minuteAlignedTime);
                        alignTime = minuteAlignedTime;
                        timeUnit = MetricUnit.MINUTE.getValue();
                    }
                    case HOUR -> {
                        // 小时单位，每小时 统计一次指标数据
                        queryWrapper.and(TaskMetricEntity::getTimeUnit).eq(MetricUnit.HOUR.getValue())
                                .and(TaskMetricEntity::getIp).eq(request.getIp())
                                .and(TaskMetricEntity::getTimeValue).eq(hourAlignedTime);
                        alignTime = hourAlignedTime;
                        timeUnit = MetricUnit.HOUR.getValue();
                    }
                    case DAY -> {
                        // 天单位，每天 统计一次指标数据
                        queryWrapper.and(TaskMetricEntity::getTimeUnit).eq(MetricUnit.DAY.getValue())
                                .and(TaskMetricEntity::getIp).eq(request.getIp())
                                .and(TaskMetricEntity::getTimeValue).eq(dayAlignedTime);
                        alignTime = dayAlignedTime;
                        timeUnit = MetricUnit.DAY.getValue();
                    }
                }
                ;
                TaskMetricEntity taskMetricEntity = taskMetricDao.getOne(queryWrapper);
                // 汇总指标数据（累计）
                if (taskMetricEntity != null) {
                    taskMetricEntity.setCpuTotal(taskMetricEntity.getCpuTotal() + request.getCpuUsage());
                    taskMetricEntity.setMemTotal(taskMetricEntity.getMemTotal() + request.getMemUsage());
                    taskMetricEntity.setDiskTotal(taskMetricEntity.getDiskTotal() + request.getDiskUsage());
                    taskMetricEntity.setNetTotal(taskMetricEntity.getNetTotal() + request.getNetUsage());
                    taskMetricDao.updateById(taskMetricEntity);
                } else {
                    taskMetricEntity = new TaskMetricEntity();
                    taskMetricEntity.setIp(request.getIp());
                    taskMetricEntity.setCpuTotal(request.getCpuUsage());
                    taskMetricEntity.setMemTotal(request.getMemUsage());
                    taskMetricEntity.setDiskTotal(request.getDiskUsage());
                    taskMetricEntity.setNetTotal(request.getNetUsage());
                    taskMetricEntity.setTimeUnit(timeUnit);
                    // 对齐 时间单位，分钟、小时、天 统计指标数据时，可使用对齐后的时间作为查询条件
                    taskMetricEntity.setTimeValue(alignTime);
                    taskMetricDao.save(taskMetricEntity);
                }
            }
        }*/
    }

    /**
     * 分页查询运行时节点指标
     *
     * @param request
     * @return
     */
    public Page<TaskRuntimeResponse> page(TaskRuntimePageRequest request) {
        log.info("分页查询指标节点：{}", JSONUtil.toJsonStr(request));
        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskRuntimeEntity.class);
        // 只添加有效的过滤条件，不包括分页参数
        if (StrUtil.isNotBlank(request.getIp())) {
            queryWrapper.and(TaskRuntimeEntity::getIp).like(request.getIp());
        }
        if (request.getCpuUsage() != null) {
            queryWrapper.and(TaskRuntimeEntity::getCpuUsage).ge(request.getCpuUsage());
        }
        if (request.getMemUsage() != null) {
            queryWrapper.and(TaskRuntimeEntity::getMemUsage).ge(request.getMemUsage());
        }
        if (request.getDiskUsage() != null) {
            queryWrapper.and(TaskRuntimeEntity::getDiskUsage).ge(request.getDiskUsage());
        }
        if (request.getNetUsage() != null) {
            queryWrapper.and(TaskRuntimeEntity::getNetUsage).ge(request.getNetUsage());
        }
        if (request.getBeginTime() != null) {
            queryWrapper.and(TaskRuntimeEntity::getHostTime).ge(request.getBeginTime());
        }
        if (request.getEndTime() != null) {
            queryWrapper.and(TaskRuntimeEntity::getHostTime).le(request.getEndTime());
        }
        Page<TaskRuntimeResponse> page = taskRuntimeDao
                .pageAs(Page.of(request.getCurrent(), request.getSize()), queryWrapper, TaskRuntimeResponse.class);

        log.info("任务项目分页查询成功，当前页: {}, 每页大小: {}, 总页数: {}, 总条数: {}",
                page.getPageNumber(), page.getPageSize(), page.getTotalPage(), page.getTotalRow());
        return page;
    }


    /**
     * 汇总运行时节点指标数据（累计计算，均值）
     */
    public List<TaskMetricQueryResponse> aggregate(TaskMetricQueryRequest request) {
        log.info("汇总运行时节点指标数据: {}", JSONUtil.toJsonStr(request));

        QueryWrapper queryWrapper = QueryWrapper.create().from(TaskMetricEntity.class);

        if (request.getTimeUnit() != null) {
            queryWrapper.and(TaskMetricEntity::getTimeUnit).eq(request.getTimeUnit());
        }
        if (request.getStartTime() != null) {
            queryWrapper.and(TaskMetricEntity::getTimeValue).ge(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            queryWrapper.and(TaskMetricEntity::getTimeValue).le(request.getEndTime());
        }
        return taskMetricDao.listAs(queryWrapper, TaskMetricQueryResponse.class);
    }

    /**
     * 分析运行时节点指标数据（统计指标，均值）
     * @param request
     * @return
     */
    public List<TaskMetricQueryResponse> analyse(TaskMetricAnalyseRequest request) {
        return null;
    }
}

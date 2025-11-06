package com.hina.eaglee.dolphin;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hina.eaglee.alert.ProcessAlert;
import com.hina.eaglee.alert.ProcessFailureAlert;
import com.hina.eaglee.alert.ProcessSuccessAlert;
import com.hina.eaglee.analysis.AnalyseResult;
import com.hina.eaglee.analysis.LogAnalysis;
import com.hina.eaglee.dao.DolphinProcessDao;
import com.hina.eaglee.dao.DolphinTaskDao;
import com.hina.eaglee.dao.TaskInstanceDao;
import com.hina.eaglee.dao.TaskProcessDao;
import com.hina.eaglee.entity.DolphinProcessEntity;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.entity.TaskInstanceEntity;
import com.hina.eaglee.entity.TaskProcessEntity;
import com.hina.eaglee.retry.RetryType;
import com.hina.eaglee.retry.TaskRetry;
import com.hina.eaglee.status.TaskStatus;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Component
@RequiredArgsConstructor
public class DolphinAlertHandler {

    private final TaskProcessDao taskProcessDao;
    private final TaskInstanceDao taskInstanceDao;

    private final DolphinTaskDao dolphinTaskDao;

    private final DolphinProcessDao dolphinProcessDao;

    private final LogAnalysis logAnalysis;

    private final ThreadPoolExecutor threadPoolExecutor;

    private final Map<String, TaskRetry> taskRetriers;

    public void collect(String content) {
        log.info("Dolphin告警信息: {}", content);
        if (!JSONUtil.isTypeJSON(content)) {
            throw new RuntimeException("监控通知内容异常 不是JSON数组");
        }
        JSONObject jsonObject = JSONUtil.parseArray(content).getJSONObject(0);
        if (jsonObject == null) {
            log.error("监控通知内容异常 不是JSON数组");
            return;
        }
        /*
         * success:
         * {"projectCode":"11986638487424","projectName":"雷达-统一回溯","owner":"admin","processId":"861765","processDefinitionCode":"19558705647872","processName":"wf_DTGZZS_SSB_202511041306270_19558705646080_sdk_liuxinran-20251104130627871-1-20251104130628720","processType":"START_PROCESS","processState":"SUCCESS","modifyBy":"admin","recovery":"NO","runTimes":"1","processStartTime":"2025-11-04 13:06:28","processEndTime":"2025-11-04 15:30:28","processHost":"172.20.52.87:5678","processDuration":"2小时24分钟"}
         * failure：
         * {"projectCode":"11986638487424","projectName":"雷达-统一回溯","owner":"admin","processId":"820941","processDefinitionCode":"19405397147648","processName":"wf_BCFSYDZ2_202510211624250_19405397145856_sdk_liuxinran-20251021162425208-1-20251021162426037","modifyBy":"admin","taskCode":"19405397144578","taskName":"wf_BCFSYDZ2_202510211624250_19405397144578","taskType":"SPARK","taskState":"FAILURE","taskStartTime":"2025-11-02 21:22:27","taskEndTime":"2025-11-03 02:27:56","taskHost":"172.20.52.89:1234","taskPriority":"medium","logPath":"https://s3-model.hinadt.com/BfXunXinDs/logs/20251021/19405397147648/1/820941/2268906.log","taskDuration":"5小时5分钟29秒","dsPatformUrl":"https://ds.xunxin-ai.com/dolphinscheduler","sparkJarPath":"dolphinscheduler/default/resources/spark/product-regress.jar"}
         */
        String state = null;
        try {
            state = jsonObject.get("processState").toString();
        } catch (Exception e) {
            state = jsonObject.get("taskState").toString();
        }

        if ("SUCCESS".equals(state)) {
            // 告警
            ProcessSuccessAlert processAlert = JSONUtil.toBean(jsonObject, ProcessSuccessAlert.class);
            // 构建实体对象
            TaskProcessEntity taskProcessEntity = buildTaskProcess(processAlert);
            // 设置实体属性
            taskProcessEntity.setProcessStartTime(processAlert.getProcessStartTime());
            taskProcessEntity.setProcessEndTime(processAlert.getProcessEndTime());
            taskProcessEntity.setProcessDuration(processAlert.getProcessDuration());
            taskProcessEntity.setProcessHost(processAlert.getProcessHost());
            // todo ,补齐任务实例列表
            recordWorkflow(buildTaskInstanceEntities(processAlert), taskProcessEntity, processAlert.getProcessId());
        }

        if ("FAILURE".equals(state)) {
            // 记录成功信息
            ProcessFailureAlert processAlert = JSONUtil.toBean(jsonObject, ProcessFailureAlert.class);
            // 根据流程ID查询流程实例
            DolphinProcessEntity dolphinProcessEntity = dolphinProcessDao.getById(processAlert.getProcessId());
            // 构建实体对象
            TaskProcessEntity taskProcessEntity = buildTaskProcess(processAlert);
            taskProcessEntity.setStartTime(dolphinProcessEntity.getStartTime());
            taskProcessEntity.setProcessHost(dolphinProcessEntity.getHost());
            TaskInstanceRecord taskInstanceRecord = buildTaskInstanceEntities(processAlert);
            /*TaskInstanceRecord taskInstanceRecord = */
            recordWorkflow(taskInstanceRecord, taskProcessEntity, processAlert.getProcessId());
            // @AI 分析日志
            threadPoolExecutor.execute(() -> {
                AnalyseResult analyseResult = logAnalysis.analyse(processAlert.getLogPath());
                TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();
                // 设值更新字段（s3日志路径、错误原因）
                taskInstanceEntity.setS3Log(processAlert.getLogPath());
                taskInstanceEntity.setReason(analyseResult.getReason());
                taskInstanceEntity.setExceptionType(analyseResult.getExceptionType());
                QueryWrapper queryWrapper = QueryWrapper.create().from(TaskInstanceEntity.class)
                        .and(TaskInstanceEntity::getProcessInstanceId).eq(processAlert.getProcessId())
                        .and(TaskInstanceEntity::getTaskName).eq(processAlert.getTaskName())
                        .and(TaskInstanceEntity::getTaskCode).eq(processAlert.getTaskCode())
                        .and(TaskInstanceEntity::getTaskType).eq(processAlert.getTaskType())
                        .and(TaskInstanceEntity::getState).eq(TaskStatus.FAILURE.getValue());
                // 根据条件更新任务实例数据
                taskInstanceDao.update(taskInstanceEntity,queryWrapper);
                Integer maxRetryTimes = taskInstanceRecord.failureTaskInstanceEntity.getMaxRetryTimes();
                if (maxRetryTimes != null && maxRetryTimes > 0) {
                    log.info("任务：{} 已经设置静态重试，最大重试次数为：{},不在触发重试机制", processAlert.getTaskName(), maxRetryTimes);
                } else {
                    // 根据异常类型，获取对应的任务重试器，如果存在则执行重试
                    RetryType retryType = RetryType.getByValue(analyseResult.getExceptionType());
                    if (retryType != null) {
                        TaskRetry taskRetry = taskRetriers.get(retryType.name());
                        if (taskRetry != null) {
                            taskRetry.retry(processAlert);
                        }
                    }
                }
            });
        }
    }

    /**
     * 记录工作流信息,包括任务流程和任务实例
     *
     * @param taskInstanceRecord 任务实例记录
     * @param taskProcessEntity  任务流程实体
     * @param processId          流程ID
     */
    private void recordWorkflow(TaskInstanceRecord taskInstanceRecord, TaskProcessEntity taskProcessEntity, Long processId) {
        // 设置流程下的任务数量
        taskProcessEntity.setTaskCount(taskInstanceRecord.allTaskInstanceEntities.size());
        // 查询流程实例是否存在，不存在则保存，存在则更新
        TaskProcessEntity entity = taskProcessDao.getById(processId);
        if (entity == null) {
            // 保存流程实例数据
            taskProcessDao.save(taskProcessEntity);
            // 保存任务实例数据批量
            taskInstanceDao.saveBatch(taskInstanceRecord.allTaskInstanceEntities);
        } else {
            taskProcessDao.updateById(taskProcessEntity);
            taskInstanceDao.updateBatch(taskInstanceRecord.allTaskInstanceEntities);
        }
    }


    record TaskInstanceRecord(TaskInstanceEntity failureTaskInstanceEntity,
                              List<TaskInstanceEntity> allTaskInstanceEntities) {
    }
    /**
     * 基于dolphin查询 构建任务实体列表
     *
     * @param processAlert 流程告警实体
     * @return 任务实例记录
     */
    private TaskInstanceRecord buildTaskInstanceEntities(ProcessAlert processAlert) {
        // 根据当前流程ID查询任务实例列表
        QueryWrapper queryWrapper = QueryWrapper.create().from(DolphinTaskEntity.class)
                .and(DolphinTaskEntity::getProcessInstanceId).eq(processAlert.getProcessId());
        List<DolphinTaskEntity> dolphinTaskEntities = dolphinTaskDao.listAs(queryWrapper, DolphinTaskEntity.class);
        List<TaskInstanceEntity> taskInstanceEntities = new ArrayList<>();
        ProcessFailureAlert processFailureAlert = null;
        if (processAlert instanceof ProcessFailureAlert failureAlert) {
            processFailureAlert = failureAlert;
        }
        TaskInstanceEntity failureTaskInstanceEntity = null;
        for (DolphinTaskEntity dolphinTask : dolphinTaskEntities) {
            TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();
            // 设置状态，按照道理来说，这里的状态应该是和dolphin中一样的，如果不一样是否需要做一下判断修正？
            if(processFailureAlert != null && processFailureAlert.getTaskName().equals(dolphinTask.getName())) {
                taskInstanceEntity.setState(TaskStatus.FAILURE.getValue());
                failureTaskInstanceEntity = taskInstanceEntity;
            } else {
                taskInstanceEntity.setState(dolphinTask.getState());
            }
            taskInstanceEntity.setTaskInstanceId(Long.valueOf(dolphinTask.getId()));
            taskInstanceEntity.setProcessInstanceId(processAlert.getProcessId());
            taskInstanceEntity.setProcessInstanceName(processAlert.getProcessName());
            taskInstanceEntity.setProjectCode(processAlert.getProjectCode());
            taskInstanceEntity.setTaskName(dolphinTask.getName());
            taskInstanceEntity.setTaskCode(dolphinTask.getTaskCode());
            taskInstanceEntity.setAppLink(dolphinTask.getAppLink());
            taskInstanceEntity.setTaskType(dolphinTask.getTaskType());
            taskInstanceEntity.setTaskExecuteType(dolphinTask.getTaskExecuteType());
            taskInstanceEntity.setTaskDefinitionVersion(dolphinTask.getTaskDefinitionVersion());

            taskInstanceEntity.setSubmitTime(dolphinTask.getSubmitTime());
            taskInstanceEntity.setStartTime(dolphinTask.getStartTime());
            taskInstanceEntity.setEndTime(dolphinTask.getEndTime());

            taskInstanceEntity.setRetryTimes(dolphinTask.getRetryTimes());
            // 成功是没有应用ID
            taskInstanceEntity.setApplicationId(dolphinTask.getAppLink());
            //taskInstanceEntity.setElapsedTime(dolphinTask.getTaskDuration());
            taskInstanceEntities.add(taskInstanceEntity);
        }
        return new TaskInstanceRecord(failureTaskInstanceEntity, taskInstanceEntities);
    }


    /**
     * 构建流程实例数据实体
     *
     * @param processAlert 流程告警实体
     * @return 任务流程实体
     */
    private TaskProcessEntity buildTaskProcess(ProcessAlert processAlert) {
        TaskProcessEntity taskProcessEntity = new TaskProcessEntity();
        // 已经流程ID为主键
        taskProcessEntity.setTaskProcessId(processAlert.getProcessId());
        taskProcessEntity.setProcessState(processAlert.getProcessState());
        taskProcessEntity.setProcessName(processAlert.getProcessName());
        taskProcessEntity.setProcessInstanceId(processAlert.getProcessId());
        taskProcessEntity.setProcessDefinitionCode(processAlert.getProcessDefinitionCode());
        taskProcessEntity.setProcessType(processAlert.getProcessType());
        taskProcessEntity.setProjectCode(processAlert.getProjectCode());
        taskProcessEntity.setProjectName(processAlert.getProjectName());
        taskProcessEntity.setOwner(processAlert.getOwner());
        return taskProcessEntity;
    }
}

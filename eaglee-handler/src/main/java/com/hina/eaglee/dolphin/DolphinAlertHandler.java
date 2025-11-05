package com.hina.eaglee.dolphin;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.dao.DolphinProcessDao;
import com.hina.eaglee.dao.DolphinTaskDao;
import com.hina.eaglee.dao.TaskInstanceDao;
import com.hina.eaglee.dao.TaskProcessDao;
import com.hina.eaglee.entity.DolphinProcessEntity;
import com.hina.eaglee.entity.DolphinTaskEntity;
import com.hina.eaglee.entity.TaskInstanceEntity;
import com.hina.eaglee.entity.TaskProcessEntity;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

@Slf4j
@Component
@RequiredArgsConstructor
public class DolphinAlertHandler {

    private final TaskProcessDao taskProcessDao;
    private final TaskInstanceDao taskInstanceDao;

    private final DolphinTaskDao dolphinTaskDao;

    private final DolphinProcessDao dolphinProcessDao;

    // 记录每个任务实例的前三次运行记录，用于计算任务运行时间
    private final Map<String, LinkedBlockingDeque<YarnApp>> taskHistoryMap = new ConcurrentHashMap<>();


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
            List<TaskInstanceEntity> taskInstanceEntities = getTaskInstanceEntities(processAlert);
            // 设置流程下的任务数量
            taskProcessEntity.setTaskCount(taskInstanceEntities.size());
            // 获取流程实例是否存在，不存在则保存，存在则更新
            TaskProcessEntity entity = taskProcessDao.getById(processAlert.getProcessId());
            if (entity == null) {
                // 保存流程实例数据
                taskProcessDao.save(taskProcessEntity);
                // 保存任务实例数据批量
                taskInstanceDao.saveBatch(taskInstanceEntities);
            } else {
                taskProcessDao.updateById(taskProcessEntity);
                taskInstanceDao.updateBatch(taskInstanceEntities);
            }
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
            // @Notice 补齐任务实例列表
            List<TaskInstanceEntity> taskInstanceEntities = getTaskInstanceEntities(processAlert);
            // 设置流程下的任务数量
            taskProcessEntity.setTaskCount(taskInstanceEntities.size());
            // 查询流程实例是否存在，不存在则保存，存在则更新
            TaskProcessEntity entity = taskProcessDao.getById(processAlert.getProcessId());
            if (entity == null) {
                // 保存流程实例数据
                taskProcessDao.save(taskProcessEntity);
                // 保存任务实例数据批量
                taskInstanceDao.saveBatch(taskInstanceEntities);
            } else {
                taskProcessDao.updateById(taskProcessEntity);
                taskInstanceDao.updateBatch(taskInstanceEntities);
            }
        }

    }

    /**
     * 基于dolphin查询 构建任务实体列表
     *
     * @param processAlert
     * @return
     */
    private List<TaskInstanceEntity> getTaskInstanceEntities(ProcessAlert processAlert) {
        String reason = null;
        String taskName = null;
        if (processAlert instanceof ProcessFailureAlert failureAlert) {
            // @AI 分析日志
            taskName = failureAlert.getTaskName();
            reason = analysisReason(failureAlert.getLogPath());
        }
        // 根据当前流程ID查询任务实例列表
        QueryWrapper queryWrapper = QueryWrapper.create().from(DolphinTaskEntity.class)
                .and(DolphinTaskEntity::getProcessInstanceId).eq(processAlert.getProcessId());
        List<DolphinTaskEntity> dolphinTaskEntities = dolphinTaskDao.listAs(queryWrapper, DolphinTaskEntity.class);
        //List<DolphinTaskEntity> dolphinTaskInstances = getDolphinTasks(processAlert);
        List<TaskInstanceEntity> taskInstanceEntities = new ArrayList<>();
        for (DolphinTaskEntity dolphinTask : dolphinTaskEntities) {
            TaskInstanceEntity taskInstanceEntity = new TaskInstanceEntity();
            // 如果任务名相同，说明任务报错，则记录错误原因
            if(dolphinTask.getName().equals(taskName)){
                taskInstanceEntity.setReason(reason);
            }
            // 设置ID
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
            taskInstanceEntity.setState(dolphinTask.getState());
            taskInstanceEntity.setSubmitTime(dolphinTask.getSubmitTime());
            taskInstanceEntity.setStartTime(dolphinTask.getStartTime());
            taskInstanceEntity.setEndTime(dolphinTask.getEndTime());

            taskInstanceEntity.setRetryTimes(dolphinTask.getRetryTimes());
            // 成功是没有应用ID
            taskInstanceEntity.setApplicationId(dolphinTask.getAppLink());
            //taskInstanceEntity.setElapsedTime(dolphinTask.getTaskDuration());
            taskInstanceEntities.add(taskInstanceEntity);
        }
        return taskInstanceEntities;
    }


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


    /**
     * 分析原因,ai分析
     *
     * @param logPath
     * @return
     */
    public String analysisReason(String logPath) {
        return "原因分析中...";
    }
}

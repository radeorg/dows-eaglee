package com.hina.eaglee.dolphin;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.hina.eaglee.mapper.DolphinQueryMapper;
import com.hina.eaglee.request.TaskInstancePageRequest;
import com.hina.eaglee.request.TaskProcessPageRequest;
import com.hina.eaglee.response.TaskInstanceResponse;
import com.hina.eaglee.response.TaskProcessResponse;
import com.hina.eaglee.response.TaskProjectResponse;
import com.mybatisflex.core.datasource.DataSourceKey;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DolphinQueryHandler {

    private final DolphinQueryMapper mapper;



    public List<TaskProjectResponse> listDolphinProject(String projectName) {
        return mapper.findProjectByProjectName(projectName);
    }

    public Page<TaskProcessResponse> pageDolphinProjectInstance(TaskProcessPageRequest request) {
        Page<TaskProcessResponse> page = Page.of(request.getPageNumber(), request.getPageSize());
        page.setTotalRow(mapper.queryProcessInstanceForCount(request));
        page.setRecords(mapper.queryProcessInstanceForPage(request));
        return page;
    }


    public List<TaskInstanceResponse> listDolphinTaskInstanceByProcessInstanceId(Long  processInstanceId) {
        return mapper.listDolphinTaskInstanceByProcessInstanceId(processInstanceId);
    }

    public Page<TaskInstanceResponse> pageDolphinTaskInstance(TaskInstancePageRequest request) {
        Page<TaskInstanceResponse> page = Page.of(request.getPageNumber(), request.getPageSize());
        page.setTotalRow(mapper.queryTaskInstanceForCount(request));
        page.setRecords(mapper.queryTaskInstanceForPage(request));
        return page;
    }

    String queryTaskInstanceCountSql = """
            SELECT
                COUNT(*) AS total
            FROM t_ds_task_instance ti
            WHERE ti.start_time >= DATE_SUB(NOW(), INTERVAL %d DAY)
            """;

    /**
     * 任务的持续时间：耗时/持续时间 = 当前时间-任务体提交时间（submit_time）
     * 任务的平均耗时：平均耗时的逻辑为相同的task_code定义对对应的N个任务实例的提交时间和任务结束时间的平均值
     * 注意：这里的平均耗时是指任务实例的耗时，不是任务定义的耗时
     *
     */
    String queryTaskInstanceSql = """
            SELECT
                ti.id AS taskInstanceId,
                ti.name AS taskName,
                ti.task_type AS taskType,
                ti.process_instance_name AS processInstanceName,
                ti.state AS taskState,
                ti.start_time AS startTime,
                ti.submit_time AS submitTime,
                ti.end_time AS endTime,
                ti.retry_times AS retryTimes,
                CASE
                    WHEN ti.end_time IS NOT NULL THEN TIMESTAMPDIFF(SECOND, ti.submit_time, ti.end_time)
                    ELSE TIMESTAMPDIFF(SECOND, ti.submit_time, NOW())
                END AS duration,
                task_avg.avg_duration AS avgDuration
            FROM t_ds_task_instance ti
            LEFT JOIN (
                SELECT
                    task_code,
                    ROUND(AVG(TIMESTAMPDIFF(SECOND, submit_time, end_time))) AS avg_duration
                FROM t_ds_task_instance
                WHERE start_time >= DATE_SUB(NOW(), INTERVAL %${period} DAY)
                  AND end_time IS NOT NULL
                  AND submit_time IS NOT NULL
                  AND state = 7
                GROUP BY task_code
            ) task_avg ON ti.task_code = task_avg.task_code
            WHERE ti.start_time >= DATE_SUB(NOW(), INTERVAL ${period} DAY) ${otherCondition}
            ORDER BY ti.start_time DESC
            LIMIT ${offset}, ${pageSize}
            """;

    public Page<TaskInstanceResponse> page(Integer startTime, int pageNumber, int pageSize) {
        try {
            // 计算偏移量
            int offset = (pageNumber - 1) * pageSize;
            DataSourceKey.use("dolphinscheduler");
            // 1. 查询总条数
            String countSql = String.format(queryTaskInstanceCountSql, startTime);
            long total = Db.selectCount(countSql);
            String sql = String.format(queryTaskInstanceSql, startTime, offset, pageSize);

            List<Row> rows = Db.selectListBySql(sql);
            List<TaskInstanceResponse> taskInstanceResponses = new ArrayList<>();
            for (Row row : rows) {
                TaskInstanceResponse taskInstance = new TaskInstanceResponse();
                BeanUtil.mapToBean(row, TaskInstanceResponse.class, true, CopyOptions.create().ignoreNullValue());
                taskInstanceResponses.add(taskInstance);
            }
            Page<TaskInstanceResponse> page = Page.of(pageNumber, pageSize, total);
            page.setRecords(taskInstanceResponses);
            return page;
        } finally {
            DataSourceKey.clear();
        }
    }


    public List<TaskInstanceResponse> listTaskByProcessInstanceId(Long processInstanceId) {
        return mapper.listTaskByProcessInstanceId(processInstanceId);
    }
}

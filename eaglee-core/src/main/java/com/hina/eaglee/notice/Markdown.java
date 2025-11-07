package com.hina.eaglee.notice;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.status.TaskInfo;
import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;

/**
 * "markdown": {
 * "content": "您的会议室已经预定，稍后会同步到应用上，请在手机上查看"
 * }
 */
public class Markdown {
    private final String content;

    public Markdown(TaskInfo taskInfo) {
        String template = """
                >**${taskName}**  \n
                >应用编号：<font color=\\"info\\">${applicationId}</font>  \n
                >项目编号：<font color=\\"info\\">${projectCode}</font>  \n
                >项目名称：<font color=\\"info\\">${projectName}</font>  \n
                >任务编号：<font color=\\"info\\">${taskCode}</font>  \n
                >任务名称：<font color=\\"info\\">${taskName}</font>  \n
                >任务类型：<font color=\\"info\\">${taskType}</font>  \n
                >任务状态：<font color=\\"info\\">${taskState}</font>  \n
                >开始时间：<font color=\\"warning\\">${startTime}</font>  \n
                >结束时间：<font color=\\"comment\\">${endTime}</font>  \n
                >持续时间：<font color=\\"comment\\">${duration}</font>  \n
                >失败原因：<font color=\\"error\\">${reason}</font>  \n
                >任务日志：<font color=\\"comment\\">[点击查看S3线上日志](${s3LogUrl})</font>  \n
                >关联人员：${assignees}  \n
                """;
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> values = objectMapper.convertValue(taskInfo, Map.class);
        Map<String, Object> additionalValues = new HashMap<>();
        values.forEach((k, v) -> {
            if (v instanceof Map children) {
                additionalValues.putAll(children);
            }
        });
        values.putAll(additionalValues);
        this.content = StringSubstitutor.replace(template, values);
    }

    public String getContent() {
        return content;
    }

}

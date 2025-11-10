package com.hina.eaglee.dolphin;

import com.hina.eaglee.alert.TaskAlertMarkdown;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.notice.NoticeClient;
import com.hina.eaglee.notice.WechatMessage;
import com.hina.eaglee.request.TaskInstancePageRequest;
import com.hina.eaglee.request.TaskProcessPageRequest;
import com.hina.eaglee.response.TaskInstanceResponse;
import com.hina.eaglee.response.TaskProcessResponse;
import com.hina.eaglee.response.TaskProjectResponse;
import com.hina.eaglee.status.TaskInfo;
import com.mybatisflex.core.paginate.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "DolphinScheduler接口")
public class DolphinClientRest {


    private final DolphinQueryHandler dolphinQueryHandler;

    private final NoticeClient noticeClient;

    @Operation(summary = "项目分页")
    @PostMapping("/dolphin/task/notice")
    public void notice(@RequestBody WechatMessage wechatMessage) {
        if (wechatMessage.isTest()) {
            log.info("测试消息，不发送到企业微信");

            wechatMessage = new WechatMessage();

            TaskInfo taskInfo = new TaskInfo();
            YarnApp yarnApp = new YarnApp();
            yarnApp.setId("application_1234567890123456789");
            yarnApp.setName("测试应用");
            yarnApp.setState("RUNNING");

            DolphinTask dolphinTask = new DolphinTask();
            dolphinTask.setExecutorName("test");

            taskInfo.setDsLogUrl("https://ds-model.hinadt.com/logs/20251106/19581179526272/1/868701/2403356.log");
            taskInfo.setS3LogUrl("https://s3-model.hinadt.com/BfXunXinDs/logs/20251106/19581179526272/1/868701/2403356.log");
            taskInfo.addAssignees("hina");
            taskInfo.setProjectCode(123456L);
            taskInfo.setProjectName("测试项目");
            taskInfo.setYarnApp(yarnApp);
            taskInfo.setDolphinTask(dolphinTask);

            wechatMessage.setKey(wechatMessage.getKey());
            wechatMessage.setMarkdown(new TaskAlertMarkdown(taskInfo));
        }
        noticeClient.notice(wechatMessage);
    }


    @Operation(summary = "项目分页")
    @GetMapping("/dolphin/process/page")
    public Page<TaskProcessResponse> pageProject(TaskProcessPageRequest taskProjectPageRequest) {
        return dolphinQueryHandler.pageDolphinProjectInstance(taskProjectPageRequest);
    }

    @Operation(summary = "任务实例列表")
    @GetMapping("/dolphin/process/task")
    public List<TaskInstanceResponse> listTaskByProcessInstanceId(@RequestParam("processInstanceId") Long processInstanceId) {
        return dolphinQueryHandler.listDolphinTaskInstanceByProcessInstanceId(processInstanceId);
    }


    @Operation(summary = "任务分页")
    @GetMapping("/dolphin/task/page")
    public Page<TaskInstanceResponse> pageTask(TaskInstancePageRequest taskInstancePageRequest) {
        return dolphinQueryHandler.pageDolphinTaskInstance(taskInstancePageRequest);
    }

    @Operation(summary = "根据名称查询项目列表")
    @GetMapping("/dolphin/project/list")
    public List<TaskProjectResponse> pageTaskInstance(@RequestParam(value = "projectName", required = false) String projectName) {
        return dolphinQueryHandler.listDolphinProject(projectName);
    }




}

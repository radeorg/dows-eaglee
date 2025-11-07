package com.hina.eaglee.dolphin;

import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.notice.Markdown;
import com.hina.eaglee.notice.NoticeClient;
import com.hina.eaglee.notice.WechatMessage;
import com.hina.eaglee.request.DolphinAlertRequest;
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
    public void notice(@RequestBody WechatMessage chatMessage){
        WechatMessage wechatMessage = new WechatMessage();

        TaskInfo taskInfo = new TaskInfo();
        YarnApp yarnApp = new YarnApp();
        yarnApp.setId("application_1234567890123456789");
        yarnApp.setName("测试应用");
        yarnApp.setState("RUNNING");

        taskInfo.setProjectCode(123456L);
        taskInfo.setProjectName("测试项目");
        taskInfo.setYarnApp(yarnApp);

        wechatMessage.setKey(chatMessage.getKey());
        wechatMessage.setMarkdown(new Markdown(taskInfo));
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

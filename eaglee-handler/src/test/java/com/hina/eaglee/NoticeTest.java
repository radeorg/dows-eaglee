package com.hina.eaglee;

import com.hina.eaglee.alert.TaskAlertMarkdown;
import com.hina.eaglee.cluster.YarnApp;
import com.hina.eaglee.notice.NoticeClient;
import com.hina.eaglee.notice.WechatMessage;
import com.hina.eaglee.status.TaskInfo;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

public class NoticeTest {


    void shouldCreateBusinessExceptionWithCodeAndMessage() {

        RestTemplate restTemplate = new RestTemplate();
        NoticeClient noticeClient = new NoticeClient(restTemplate);
        WechatMessage wechatNoticeRequest = new WechatMessage();
        wechatNoticeRequest.setKey("123");
        wechatNoticeRequest.setMsgtype("text");

        noticeClient.notice(wechatNoticeRequest);
    }



    @Test
    public void testMarkdown() {
        TaskInfo taskInfo = new TaskInfo();
        YarnApp yarnApp = new YarnApp();
        yarnApp.setId("application_1234567890123456789");
        yarnApp.setName("测试应用");
        yarnApp.setState("RUNNING");

        taskInfo.setProjectCode(123456L);
        taskInfo.setProjectName("测试项目");
        taskInfo.setYarnApp(yarnApp);
        /*taskInfo.setTaskCode("123456");
        taskInfo.setTaskType("测试类型");
        taskInfo.setTaskState("测试状态");
        taskInfo.setStartTime("2023-01-01 00:00:00");
        taskInfo.setEndTime("2023-01-01 00:01:00");
        taskInfo.setDuration("1m");
        taskInfo.setReason("测试失败");
        taskInfo.setS3LogUrl("https://s3.cn-north-1.amazonaws.com.cn/hina-eaglee-dev/123456/123456.log");*/
        TaskAlertMarkdown markdown = new TaskAlertMarkdown(taskInfo);
        System.out.println(markdown.getContent());
    }

}

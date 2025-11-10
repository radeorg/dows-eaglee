package org.dows.eaglee.alert;


import cn.hutool.json.JSONUtil;
import org.dows.eaglee.dolphin.DolphinProperties;
import org.dows.eaglee.dolphin.NoticeSetting;
import org.dows.eaglee.notice.NoticeClient;
import org.dows.eaglee.notice.WechatMessage;
import org.dows.eaglee.processor.StateProcessor;
import org.dows.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class YarnTimeoutAlert implements TaskAlert {

    private final DolphinProperties dolphinProperties;

    private final NoticeClient noticeClient;
    // 任务处理器，用于根据任务类型获取对应的处理器，目前不考虑
    private final Map<String, StateProcessor> taskProcessors;
    @Override
    public void handle(TaskInfo taskInfo) {
        log.info("任务超时告警：{}", JSONUtil.toJsonStr(taskInfo));
        NoticeSetting noticeSetting = dolphinProperties.getNotices().get(YarnTimeoutAlert.class.getSimpleName());
        if(!noticeSetting.isEnable()){
            return;
        }
        for (String wechatKey : noticeSetting.getWechatKeys()) {
            WechatMessage wechatMessage = new WechatMessage();
            wechatMessage.setMarkdown(new TaskAlertMarkdown(taskInfo));
            noticeClient.notice(wechatMessage);
            wechatMessage.setToken(wechatKey);
        }
    }
}

package com.hina.eaglee.alert;


import cn.hutool.json.JSONUtil;
import com.hina.eaglee.notice.NoticeClient;
import com.hina.eaglee.notice.Text;
import com.hina.eaglee.notice.WechatMessage;
import com.hina.eaglee.processor.StateProcessor;
import com.hina.eaglee.status.TaskInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class YarnTimeoutAlert implements TaskAlert {

    private final NoticeClient noticeClient;
    // 任务处理器，用于根据任务类型获取对应的处理器，目前不考虑
    private final Map<String, StateProcessor> taskProcessors;
    @Override
    public void handle(TaskInfo taskInfo) {
        log.info("任务超时告警：{}", JSONUtil.toJsonStr(taskInfo));
        WechatMessage wechatMessage = new WechatMessage();
        wechatMessage.setMsgtype("text");
        wechatMessage.setKey("");
        Text text = new Text();
        text.setContent("任务超时告警：" + JSONUtil.toJsonStr(taskInfo));
        wechatMessage.setText(text);
        noticeClient.notice(wechatMessage);
    }
}

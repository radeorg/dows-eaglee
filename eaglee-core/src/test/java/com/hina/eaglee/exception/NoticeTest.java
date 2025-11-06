package com.hina.eaglee.exception;

import com.hina.eaglee.notice.NoticeClient;
import com.hina.eaglee.notice.WechatMessage;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

public class NoticeTest {

    @Test
    void shouldCreateBusinessExceptionWithCodeAndMessage() {

        RestTemplate restTemplate = new RestTemplate();
        NoticeClient noticeClient = new NoticeClient(restTemplate);
        WechatMessage wechatNoticeRequest = new WechatMessage();
        wechatNoticeRequest.setKey("123");
        wechatNoticeRequest.setMsgtype("text");

        noticeClient.notice(wechatNoticeRequest);
    }

}

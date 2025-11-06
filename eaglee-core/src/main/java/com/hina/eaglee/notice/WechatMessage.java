package com.hina.eaglee.notice;

import com.hina.eaglee.dolphin.Uri;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Uri("post https://qyapi.weixin.qq.com/cgi-bin/webhook/send")
@Data
public class WechatMessage implements NoticeMessage {

    // 该注解会将参数名和对应的值追加到请求的 URL 中
    @UriParam("key")
    private String key;

    @Schema(description = "消息类型[text, markdown, news, image, voice, video, file]")
    private String msgtype;

    //@UriBody("text")
    @UriHeader
    private String token;

    @Schema(description = "文本消息")
    private Text text;
    //"UserID1|UserID2|UserID3",
    private String touser;
    //"PartyID1|PartyID2",
    private String toparty;
    //"TagID1 | TagID2",
    private String totag;
    // 1
    private String agentid;
    //0
    private String safe;
    // 0
    private String enable_id_trans;
    //0
    private String enable_duplicate_check;
    //1800
    private String duplicate_check_interval;


}

//package com.hina.eaglee.notice;
//
//
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Component
//public interface NoticeClient {
//    @Post(
//            url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key={key}",
//            headers = {
//                    "Accept-Charset: utf-8",
//                    "Content-Type: application/json"
//            },
//            dataType = "json")
//    void sendWechatMsg(@Var("key") String key, @JSONBody Map<String, Object> body);
//
//
//}
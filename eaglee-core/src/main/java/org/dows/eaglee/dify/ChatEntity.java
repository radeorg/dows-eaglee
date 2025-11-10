package org.dows.eaglee.dify;

import lombok.Data;
import org.dows.eaglee.dolphin.Uri;
import org.dows.eaglee.exchange.ExchangeEntity;
import org.dows.eaglee.notice.UriHeader;

import java.util.Map;


/**
 * curl -X POST 'https://dify.hinadt.com/v1/chat-messages' \
 * --header 'Authorization: Bearer {api_key}' \
 * --header 'Content-Type: application/json' \
 * --data-raw '{
 *   "inputs": {},
 *   "query": "What are the specs of the iPhone 13 Pro Max?",
 *   "response_mode": "streaming",
 *   "conversation_id": "",
 *   "user": "abc-123",
 *   "files": [
 *       {
 *         "type": "image",
 *       "transfer_method": "remote_url",
 *       "url": "https://cloud.dify.ai/logo/logo-site.png"
 *     }
 *   ]
 * }'
 */
@Uri("post https://dify.hinadt.com/v1/chat-messages")
@Data
public class ChatEntity implements ExchangeEntity {

    @UriHeader(value = "Authorization", prefix = "Bearer ")
    private String apiKey;
    //@UriBody("query")
    private String query;
    private Map<String,String> inputs;
    private String user;
    private String responseMode = "streaming";
}

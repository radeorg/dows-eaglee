package org.dows.eaglee.dify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DifyMessage {
    private String event;
    private String task_id;
    private String id;
    private String message_id;
    private String conversation_id;
    private String mode;
    private Answer answer;
    private Metadata metadata;
    private long created_at;
    private String annotation_reply;


}
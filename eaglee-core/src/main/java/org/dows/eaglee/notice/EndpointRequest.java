package org.dows.eaglee.notice;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EndpointRequest implements Serializable {
    @Getter
    @Setter
    @JsonIgnore
    private HttpMethod httpMethod;
    @Getter
    @Setter
    private String endpoint;
    @Getter
    private final Map<String, Object> headers = new HashMap<>();
    @Getter
    private final Map<String, Object> body = new HashMap<>();

    public EndpointRequest addHeader(String key, Object value) {
        headers.put(key, value);
        return this;
    }

    public EndpointRequest addBody(String key, Object value) {
        body.put(key, value);
        return this;
    }
}

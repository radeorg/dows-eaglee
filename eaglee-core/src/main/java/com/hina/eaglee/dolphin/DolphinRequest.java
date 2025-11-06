package com.hina.eaglee.dolphin;

public interface DolphinRequest {
    Long getProjectCode();
    default <T extends DolphinEndpoint> T getEndpoint(Class<T> endpointClass) {
        return endpointClass.cast(this);
    }
}

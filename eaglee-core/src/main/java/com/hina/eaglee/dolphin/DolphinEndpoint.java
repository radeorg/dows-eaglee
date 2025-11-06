package com.hina.eaglee.dolphin;

public interface DolphinEndpoint<T extends DolphinRequest> {
    String exchange(T dolphinRequest);
}

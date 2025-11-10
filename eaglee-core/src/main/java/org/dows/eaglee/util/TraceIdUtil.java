package org.dows.eaglee.util;

import java.util.UUID;

public class TraceIdUtil {

    /**
     * 生成追踪ID
     */
    public static String generateTraceId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}

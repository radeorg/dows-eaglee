package com.hina.eaglee.retry;

import lombok.Getter;

@Getter
public enum RetryType {
    unknownExceptionRetry(-1),
    resourceExceptionRetry(1),
    bizExceptionRetry(2),
    timeoutExceptionRetry(3);

    private int value;

    RetryType(int value) {
        this.value = value;
    }
    public static RetryType getByValue(int value) {
        for (RetryType type : RetryType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        return null;
    }
}

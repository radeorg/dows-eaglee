package org.dows.eaglee.pojo;

import lombok.Getter;

public enum MetricUnit {

    MINUTE(1),
    HOUR(2),
    DAY(3);

    @Getter
    private final int value;

    MetricUnit(int value) {
        this.value = value;
    }


}

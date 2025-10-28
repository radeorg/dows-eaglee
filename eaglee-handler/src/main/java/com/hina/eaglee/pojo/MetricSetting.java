package com.hina.eaglee.pojo;

import lombok.Data;

import java.util.Map;

@Data
public class MetricSetting {

    /**
     *
     * key:采集时间单位（1=分钟，2=小时，3=天）
     * value:采集间隔值（分，时，天）
     */
    private Map<MetricUnit, Integer> metricUnitMap;


}

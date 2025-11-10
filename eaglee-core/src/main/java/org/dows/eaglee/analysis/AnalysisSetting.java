package org.dows.eaglee.analysis;

import lombok.Data;

@Data
public class AnalysisSetting {
    private boolean enable;
    private String model;
    private double temperature;
    private String systemPrompt;
    private String userPrompt;
}

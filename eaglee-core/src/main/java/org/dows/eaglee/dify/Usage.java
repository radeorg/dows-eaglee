package org.dows.eaglee.dify;

import lombok.Data;

@Data
public class Usage {
    private int prompt_tokens;
    private String prompt_unit_price;
    private String prompt_price_unit;
    private String prompt_price;
    private int completion_tokens;
    private String completion_unit_price;
    private String completion_price_unit;
    private String completion_price;
    private int total_tokens;
    private String total_price;
    private String currency;
    private double latency;

}
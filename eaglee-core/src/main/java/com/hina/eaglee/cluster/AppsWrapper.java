package com.hina.eaglee.cluster;

import lombok.Data;

import java.util.List;

@Data
public class AppsWrapper {
    private List<YarnApp> app;
}

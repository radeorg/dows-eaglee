package com.hina.eaglee.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * URL参数提取工具类
 */
public class UrlParamExtractor {
    
    /**
     * 从URL中提取所有查询参数
     * @param url 完整的URL字符串
     * @return 参数名和参数值的映射（保持顺序）
     */
    public static Map<String, String> extractParameters(String url) {
        Map<String, String> params = new LinkedHashMap<>();
        
        if (url == null || url.isEmpty()) {
            return params;
        }
        
        // 查找第一个?的位置
        int questionMarkIndex = url.indexOf('?');
        if (questionMarkIndex == -1) {
            return params; // 没有查询参数
        }
        
        // 提取查询参数字符串（?后面的部分）
        String queryString = url.substring(questionMarkIndex + 1);
        
        // 处理锚点（如果有）
        int anchorIndex = queryString.indexOf('#');
        if (anchorIndex != -1) {
            queryString = queryString.substring(0, anchorIndex);
        }
        
        // 分割参数对
        String[] paramPairs = queryString.split("&");
        
        for (String paramPair : paramPairs) {
            if (paramPair.isEmpty()) {
                continue;
            }
            
            String[] keyValue = paramPair.split("=", 2);
            String key = keyValue[0];
            String value = keyValue.length > 1 ? keyValue[1] : "";
            
            // URL解码（如果需要）
            // value = URLDecoder.decode(value, StandardCharsets.UTF_8);
            
            params.put(key, value);
        }
        
        return params;
    }
    
    /**
     * 提取URL中的特定参数值
     * @param url 完整的URL字符串
     * @param paramName 参数名
     * @return 参数值，如果不存在返回null
     */
    public static String extractParameter(String url, String paramName) {
        Map<String, String> params = extractParameters(url);
        return params.get(paramName);
    }
    
    /**
     * 提取URL中的第一个参数名
     * @param url 完整的URL字符串
     * @return 第一个参数名，如果没有参数返回null
     */
    public static String extractFirstParameterName(String url) {
        Map<String, String> params = extractParameters(url);
        return params.isEmpty() ? null : params.keySet().iterator().next();
    }
    
    /**
     * 提取URL中的第一个参数值
     * @param url 完整的URL字符串
     * @return 第一个参数值，如果没有参数返回null
     */
    public static String extractFirstParameterValue(String url) {
        Map<String, String> params = extractParameters(url);
        return params.isEmpty() ? null : params.values().iterator().next();
    }
    
    /**
     * 检查URL是否包含特定参数
     * @param url 完整的URL字符串
     * @param paramName 参数名
     * @return 是否包含该参数
     */
    public static boolean containsParameter(String url, String paramName) {
        Map<String, String> params = extractParameters(url);
        return params.containsKey(paramName);
    }
    
    /**
     * 获取URL中参数的数量
     * @param url 完整的URL字符串
     * @return 参数数量
     */
    public static int getParameterCount(String url) {
        Map<String, String> params = extractParameters(url);
        return params.size();
    }
}
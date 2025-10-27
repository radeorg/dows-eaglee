package com.hina.eaglee.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 分页响应类
 * @param <T> 数据类型
 */
@Data
@Schema(description = "分页响应")
public class PageResponse<T> {
    
    /**
     * 数据列表
     */
    @Schema(description = "数据列表")
    private List<T> records;
    
    /**
     * 总记录数
     */
    @Schema(description = "总记录数", example = "100")
    private Long total;
    
    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    private Long current;
    
    /**
     * 每页大小
     */
    @Schema(description = "每页大小", example = "10")
    private Long size;
    
    /**
     * 总页数
     */
    @Schema(description = "总页数", example = "10")
    private Long pages;
    
    /**
     * 是否有上一页
     */
    @Schema(description = "是否有上一页", example = "false")
    private Boolean hasPrevious;
    
    /**
     * 是否有下一页
     */
    @Schema(description = "是否有下一页", example = "true")
    private Boolean hasNext;
    
    /**
     * 私有构造函数
     */
    private PageResponse() {}
    
    /**
     * 私有构造函数
     */
    private PageResponse(List<T> records, Long total, Long current, Long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size;
        this.hasPrevious = current > 1;
        this.hasNext = current < pages;
    }
    
    /**
     * 创建分页响应
     */
    public static <T> PageResponse<T> of(List<T> records, Long total, Long current, Long size) {
        return new PageResponse<>(records, total, current, size);
    }
    
    /**
     * 创建空分页响应
     */
    public static <T> PageResponse<T> empty(Long current, Long size) {
        return new PageResponse<>(List.of(), 0L, current, size);
    }
    
    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return records == null || records.isEmpty();
    }
    
    /**
     * 获取记录数量
     */
    public int getRecordCount() {
        return records == null ? 0 : records.size();
    }
}
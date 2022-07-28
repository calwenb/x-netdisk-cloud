package com.wen.commutil.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 统一分页返回VO类
 * 可配合Mybatis-plus分页插件使用
 *
 * @author calwen
 * @since 2022/7/20
 */
@Data
@NoArgsConstructor
public class PageVO<T> {
    private List<T> content;

    private Long page;

    private Integer size;

    private Long total;

    public static <T> PageVO<T> of(List<T> content, Long page, Integer size, Long total) {
        return new PageVO<>(content, page, size, total);
    }

    public PageVO(List<T> content, Long page, Integer size, Long total) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.total = total;
    }
}

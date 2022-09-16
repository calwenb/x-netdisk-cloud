package com.wen.netdisc.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一返回体
 *
 * @author calwen
 * @since 2022/8/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO<T> implements Serializable {
    private Integer code;
    private T data;
    private String message;
}

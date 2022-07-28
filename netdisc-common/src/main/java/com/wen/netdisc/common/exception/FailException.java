package com.wen.netdisc.common.exception;

/**
 * 操作失败的全局异常
 * 非真正异常，用于全局返回前端
 *
 * @author calwen
 * @since 2022/7/27
 */
public class FailException extends RuntimeException {
    public FailException(String message) {
        super(message);
    }
}

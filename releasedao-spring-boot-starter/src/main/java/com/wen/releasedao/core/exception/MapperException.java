package com.wen.releasedao.core.exception;

/**
 * Mapper异常
 *
 * @author calwen
 * @since 2022/8/20
 */
public class MapperException extends RuntimeException {
    public MapperException(String message) {
        super(message);
    }

    public MapperException(String message, Throwable cause) {
        super(message, cause);
    }

}

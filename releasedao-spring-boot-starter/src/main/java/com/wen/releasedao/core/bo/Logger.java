package com.wen.releasedao.core.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * 日志 业务对象（BO）
 *
 * @author calwen
 * @since 2022/8/24
 */
@Data
@Accessors(chain = true)
public class Logger {
    /**
     * sql语句
     */
    private String sql;
    /**
     * PreparedStatement
     */
    private String pstStr;

    /**
     * 获得数据
     */
    private Object data;
    /**
     * 信息
     */
    private String message;

    /**
     * 预编译设值
     */
    private List<Object> values = Collections.emptyList();
    /**
     * 是否成功
     */
    private Boolean success = true;
    /**
     * 异常类
     */
    private String exception;
    /**
     * 异常信息
     */
    private String errorMessage;


}

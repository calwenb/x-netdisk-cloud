package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 为实体类对象指定数据库表名，
 * 若未指定，默认驼峰式转蛇形式
 *
 * @author calwen
 * @since 2022/7/9
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {
    /**
     * 指明表名
     */
    String value();


}

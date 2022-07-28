package com.wen.baseorm.core.annotation;

import java.lang.annotation.*;

/**
 * 为实体类对象指定数据库表名
 *
 * @author calwen
 * @since 2022/7/9
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableName {
    /**
     * 指明表名
     * 必需指定
     */

    String value();


}

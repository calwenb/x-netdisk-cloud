package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 为实体类属性指定数据库字段名
 * 若未指定，默认驼峰式转蛇形式
 *
 * @author calwen
 * @since 2022/7/9
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldName {
    String value() default "";

    boolean exist() default true;
}

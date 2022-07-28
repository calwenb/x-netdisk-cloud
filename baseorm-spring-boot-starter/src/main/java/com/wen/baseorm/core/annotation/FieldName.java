package com.wen.baseorm.core.annotation;

import java.lang.annotation.*;

/**
 * 为实体类属性指定数据库字段名
 *
 * @author calwen
 * @since 2022/7/9
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldName {
    String value() default "";

    boolean exist() default true;
}

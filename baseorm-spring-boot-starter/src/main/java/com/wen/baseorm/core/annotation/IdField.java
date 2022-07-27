package com.wen.baseorm.core.annotation;

import com.wen.baseorm.core.enums.IdTypeEnum;

import java.lang.annotation.*;

/**
 * 为实体类属性指定数据库主键
 *
 * @author calwen
 * @since 2022/7/9
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdField {

    String value() default "";

    IdTypeEnum idType() default IdTypeEnum.AUTO;

}

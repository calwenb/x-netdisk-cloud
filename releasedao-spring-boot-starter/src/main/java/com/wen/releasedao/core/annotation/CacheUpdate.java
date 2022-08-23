package com.wen.releasedao.core.annotation;

import com.wen.releasedao.core.enums.CacheUpdateEnum;

import java.lang.annotation.*;

/**
 * 指定缓存更新 方法开启
 *
 * @author calwen
 * @since 2022/7/18
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheUpdate {
    CacheUpdateEnum value() default CacheUpdateEnum.ID;
}

package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 指定缓存key的 id
 * 弃用
 *
 * @author calwen
 * @since 2022/7/16
 */
@Documented
@Deprecated
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheId {
    String value() default "";

    long expiredTime() default 0;
}

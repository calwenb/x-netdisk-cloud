package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 指定缓存key的 id
 *
 * @author calwen
 * @since 2022/7/16
 */
@Deprecated
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheId {
    String value() default "";

    long expiredTime() default 0;
}

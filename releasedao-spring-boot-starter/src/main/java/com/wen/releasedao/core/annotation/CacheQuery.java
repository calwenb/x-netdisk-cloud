package com.wen.releasedao.core.annotation;


import java.lang.annotation.*;

/**
 * 指定BaseMapper 缓存 开启方法
 *
 * @author calwen
 * @since 2022/7/16
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheQuery {


}

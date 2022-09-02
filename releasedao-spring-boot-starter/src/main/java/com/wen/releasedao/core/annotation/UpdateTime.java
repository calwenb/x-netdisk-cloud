package com.wen.releasedao.core.annotation;

import java.lang.annotation.*;

/**
 * 自动填充更新时间
 *
 * @author calwen
 * @since 2022/8/29
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateTime {
}

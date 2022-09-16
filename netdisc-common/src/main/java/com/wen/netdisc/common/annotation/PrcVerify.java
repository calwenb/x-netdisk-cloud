package com.wen.netdisc.common.annotation;

import java.lang.annotation.*;

/**
 * 验证rpc注解
 * 配合RpcAop食用
 *
 * @author calwen
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrcVerify {
}
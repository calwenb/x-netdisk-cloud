package com.wen.releasedao.core.enums;

/**
 * 缓存类型枚举类
 * 规定了缓存的执行策略
 *
 * @author calwen
 * @since 2022/7/16
 */
@Deprecated
public enum CacheEnum {
    /**
     * 缓存旁路模式，
     * 默认  key=表名:主键名:{id}
     * 主键，默认第一个舒心
     * value= 结果值
     */
    DEFAULT,
    /**
     * 之定义 key
     * 注解的形式指定属性 key
     */
    CUSTOMIZE,
}

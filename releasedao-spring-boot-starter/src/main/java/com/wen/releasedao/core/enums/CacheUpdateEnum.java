package com.wen.releasedao.core.enums;

/**
 * 缓存更新 枚举类
 * 不同的枚举 对应了不同的删除策略
 *
 * @author calwen
 * @since 2022/7/17
 */
public enum CacheUpdateEnum {
    /**
     * 删除主键的数据
     */
    ID,
    /**
     * 删除 主键的数据
     */
    TARGET,
    /**
     * 删除该表全部缓存
     */
    WRAPPER,
    /**
     * 删除该表全部缓存
     */
    BATCH,
    /**
     * 删除 全部缓存
     */
    OTHER
}

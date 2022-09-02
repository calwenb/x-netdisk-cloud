package com.wen.releasedao.core.enums;

/**
 * wrapper操作枚举类
 *
 * @author calwen
 * @since 2022/7/9
 */

public enum OperatEnum {
    /**
     * 首次加入
     */
    HEAD,
    /**
     * 连接词
     */
    AND, OR,
    /**
     * 排序
     */
    ORDER, ORDER_DESC,
    /**
     * 分页
     */
    LIMIT,
    /**
     * 关系运算
     */
    EQ, NOT_EQ, IN, GREATER, LESS, G_EQ, L_EQ,
    /**
     * 模糊查询
     */
    LIKE, LIKE_LEFT, LIKE_RIGHT,
    /**
     * null ?
     */
    IS_NULL, ONT_NULL,


}

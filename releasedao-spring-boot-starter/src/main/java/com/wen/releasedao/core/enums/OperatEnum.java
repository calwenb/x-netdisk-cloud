package com.wen.releasedao.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * wrapper操作枚举类
 *
 * @author calwen
 * @since 2022/7/9
 */

public enum OperatEnum {
    /**
     *
     */
    HEAD,
    /**
     *
     */
    AND,
    /**
     *
     */
    OR,

    /**
     *
     */
    ORDER,
    /**
     *
     */
    ORDER_DESC,
    /**
     *
     */
    LIMIT,

    EQ, EQS, IN,
    /**
     *
     */
    NOT_EQ,
    GREATER,
    LESS,
    G_EQ,
    L_EQ,

    LIKE,
    LIKE_LEFT,
    LIKE_RIGHT,

    IS_NULL,
    ONT_NULL,




}

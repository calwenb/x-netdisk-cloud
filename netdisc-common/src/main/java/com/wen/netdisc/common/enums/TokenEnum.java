package com.wen.netdisc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenEnum {
    TOKEN("TOKEN"),
    //    TOKEN_PREFIX("calwen"),
    HEADER("token"),
    JWT_SECRET("calwendbshagicba");

    private String property;
}

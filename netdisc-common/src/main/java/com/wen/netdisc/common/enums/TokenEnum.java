package com.wen.netdisc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenEnum {
    TOKEN("TOKEN"),
    HEADER("token"),
    JWT_SECRET("calwendbshagicba");

    private String property;
}

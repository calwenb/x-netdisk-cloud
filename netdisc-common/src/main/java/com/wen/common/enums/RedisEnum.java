package com.wen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisEnum {
    TOKEN_PREFIX("token:");
    private String property;
}

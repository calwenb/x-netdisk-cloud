package com.wen.netdisc.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisEnum {
    TOKEN_PREFIX("x-netdisk:token:"),

    TRASH_PREFIX("x-netdisk:trash:"),

    CHUNK_PREFIX("x-netdisk:backup:"),
    FILE_THUMBNAIL_PREFIX("x-netdisk:file:thumbnail:"),
    SMS_Mail_CODE_PREFIX("x-netdisk:smsmail:code:");

    private final String property;
}

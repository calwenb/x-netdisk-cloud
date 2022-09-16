package com.wen.netdisc.common.util;

import java.util.Random;

/**
 * @author calwen
 * @since 2022/9/8
 */
public class NumberUtil {
    public static String createCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            code.append(new Random().nextInt(9));
        }
        return String.valueOf(code);
    }
}

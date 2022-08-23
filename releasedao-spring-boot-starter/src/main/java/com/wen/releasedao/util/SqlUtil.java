package com.wen.releasedao.util;

import com.mysql.cj.util.StringUtils;
import com.wen.releasedao.config.PropertyConfig;

/**
 * 字段工具类
 *
 * @author calwen
 * @since 2022/7/9
 */
public class SqlUtil {
    public static String snakeToCame(String snake) {
        StringBuffer sb = new StringBuffer(snake);
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) == '_') {
                sb.replace(i, i + 2, String.valueOf((char) (sb.charAt(i + 1) - 32)));
            }
        }
        return sb.toString();
    }

    public static String camelToSnake(String camel) {
        //是否驼峰转蛇形
        if (!new PropertyConfig().isCamelCase()) {
            return camel;

        }
        if (StringUtils.isNullOrEmpty(camel)) {
            return "";
        }
        StringBuilder sb = new StringBuilder(camel);
        if (Character.isUpperCase(sb.charAt(0))) {
            sb.replace(0, 1, String.valueOf((char) (sb.charAt(0) + 32)));
        }

        for (int i = 0; i < sb.length(); i++) {
            if (Character.isUpperCase(sb.charAt(i))) {
                sb.replace(i, i + 1, "_" + (char) (sb.charAt(i) + 32));
            }
        }
        return sb.toString();
    }


}

package com.wen.releasedao.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 转换类型工具类
 *
 * @author calwen
 * @since 2022/7/9
 */
public class CastUtil {
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }
}

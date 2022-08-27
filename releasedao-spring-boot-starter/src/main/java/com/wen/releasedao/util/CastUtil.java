package com.wen.releasedao.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    /**
     * 获取泛型类Class对象，不是泛型类则返回null
     **/
    public static Class<?> getObjectClass(Class<?> clazz) {
        Class<?> objectClass = null;
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 0) {
                objectClass = (Class<?>) actualTypeArguments[0];
            }
        }
        return objectClass;
    }
}

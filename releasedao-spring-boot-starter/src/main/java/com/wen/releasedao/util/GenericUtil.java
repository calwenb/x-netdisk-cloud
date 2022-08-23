package com.wen.releasedao.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericUtil {

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

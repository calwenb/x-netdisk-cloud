package com.wen.commutil.utils;


/**
 * NullUtil类
 * 判断参数是否有null，避免nullPointerException
 *
 * @author calwen
 */
public class NullUtil {
    /**
     * 判断是否有空的参数
     * 有，返回true
     *
     * @author calwen
     */
    public static boolean hasNull(Object... params) {
        for (Object o : params) {
            if (o == null || "".equals(o)) {
                return true;
            }
        }
        return false;
    }


}

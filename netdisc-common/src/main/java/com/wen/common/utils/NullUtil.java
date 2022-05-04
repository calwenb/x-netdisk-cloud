package com.wen.common.utils;


/**
 * NullUtil类
 * 判断参数是否有null，避免nullPointerException
 *
 * @author Mr.文
 */
public class NullUtil {
    /**
     * 判断是否有空的参数
     * 有，返回true
     *
     * @author Mr.文
     */
    public static boolean hasNull(Object... params) {
        for (Object o : params) {
            if (o == null || "".equals(o)) {
                return true;
            }
        }
        return false;
    }

    public static String msg() {
        return ResponseUtil.error("参数有空！");
    }
}

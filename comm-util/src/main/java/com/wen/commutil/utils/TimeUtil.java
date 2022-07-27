package com.wen.commutil.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtil类
 * 转换时间格式
 *
 * @author calwen
 */
public class TimeUtil {
    public static String getNow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

}

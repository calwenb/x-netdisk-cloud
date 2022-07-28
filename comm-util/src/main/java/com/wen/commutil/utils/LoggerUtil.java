package com.wen.commutil.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerUtil {

    private static Log log;

    public static void debug(String info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.debug(info);
    }

    public static void info(String info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.info(info);
    }

    public static void warn(String info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.warn(info);
    }

    public static void error(String info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.error(info);
    }


    public static void fatal(String info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.fatal(info);
    }

}

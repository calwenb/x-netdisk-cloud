package com.wen.netdisc.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoggerUtil {

    private static Log log;

    public static void debug(Object info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.debug(info);
    }

    public static void info(Object info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.info(info);
    }

    public static void warn(Object info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.warn(info);
    }

    public static void error(Object info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.error(info);
    }


    public static void fatal(Object info, Class<?> c) {
        log = LogFactory.getLog(c);
        log.fatal(info);
    }

}

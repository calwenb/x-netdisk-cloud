package com.wen.releasedao.core.manager;

import com.wen.releasedao.config.PropertyConfig;
import com.wen.releasedao.core.bo.Logger;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 日志记录 Manager管家
 *
 * @author calwen
 * @since 2022/8/24
 */
public class LoggerManager {
    private static final ThreadLocal<Logger> loggerLocal = new ThreadLocal<>();

    public static Logger getLogger() {
        Logger logger = loggerLocal.get();
        if (logger == null) {
            logger = new Logger();
            loggerLocal.set(logger);
        }
        return logger;
    }

    public static void remove() {
        loggerLocal.remove();
    }


    public static void log(PreparedStatement pst, String sql, List<Object> values) {
        Logger logger = getLogger();
        if (turnOnLogger()) {
            logger.setSql(sql);
            logger.setPstStr(String.valueOf(pst));
            logger.setValues(Optional.ofNullable(values).orElse(Collections.emptyList()));
        }

    }

    public static void log(PreparedStatement pst, String sql, Object[] values) {
        Logger logger = getLogger();
        if (turnOnLogger()) {
            logger.setSql(sql);
            logger.setPstStr(String.valueOf(pst));
            logger.setValues(Arrays.asList(Optional.ofNullable(values).orElse(new Object[]{})));
        }

    }

    public static <T> void logData(T data) {
        Logger logger = getLogger();
        if (turnOnLogger()) {
            logger.setData(data);
        }
    }

    public static void logError(String message, Throwable e) {
        Logger logger = getLogger();
        if (turnOnLogger()) {
            logger.setSuccess(false);
            logger.setMessage(message);
            logger.setException(e.getClass().getName());
            logger.setErrorMessage(e.getMessage());
        }
    }


    public static void display() {
        Logger logger = getLogger();
        if (turnOnLogger()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n++++ ReleaseDao Logger Start ++++ \n");
            formatSql(logger, sb);
            if (logger.getSuccess()) {
                formatResult(logger, sb);
            } else {
                formatError(logger, sb);
            }
            sb.append("\n\n++++ ReleaseDao Logger End ++++ \n");
            System.out.println(sb);
        }
    }


    private static void formatSql(Logger logger, StringBuilder sb) {
        if (logger.getPstStr() == null) {
            sb.append("\n++  no sql is executed  ++");
            return;
        }
        sb.append("\n==>    Sql:  ").append(logger.getSql());
        Optional.ofNullable(logger.getValues()).ifPresent((values) -> {
            sb.append("\n==>  Value:  ");
            for (Object v : values) {
                if (v == null) {
                    continue;
                }
                String className = v.getClass().getSimpleName();
                sb.append(v).append(" (").append(className).append(")   ");
            }
        });
        String pstStr = logger.getPstStr();
        sb.append("\n==> ExeSQL: ").append(pstStr.substring(pstStr.indexOf(':') + 1));
    }


    private static void formatResult(Logger logger, StringBuilder sb) {
        Object data = logger.getData();
        if (data == null) {
            sb.append("\n\n++ No Result ++");
            return;
        }
        sb.append("\n\n++  Result ++");
        if (data instanceof List) {
            List<Object> list = (List) data;
            int row = 0;
            for (Object o : list) {
                sb.append("\n<==  Row ").append(++row).append(":  ").append(o);
            }
            sb.append("\n<==  Total:  ").append(row);
        } else if (data instanceof Integer) {
            sb.append("\n<== Change: ").append(data);
        } else {
            sb.append("\n<==  Row ").append(1).append(":  ").append(data);
            sb.append("\n<==  Total:  ").append(1);
        }
    }

    private static void formatError(Logger logger, StringBuilder sb) {
        sb.append("\n\n++ An exception occurs ++");
        sb.append("\n<==    exception:  ").append(logger.getException());
        sb.append("\n<==      message:  ").append(logger.getMessage());
        sb.append("\n<== errorMessage:  ").append(logger.getErrorMessage());
    }


    /**
     * 是否开启日志功能
     */
    private static boolean turnOnLogger() {
        return PropertyConfig.isLogger();
    }


}

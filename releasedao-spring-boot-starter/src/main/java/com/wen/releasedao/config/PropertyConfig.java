package com.wen.releasedao.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author calwen
 * @since 2022/8/26
 */
@Data
@Configuration
public class PropertyConfig {
    /**
     * 驼峰式转换，默认开启
     */
    private static boolean camelCase;
    /**
     * 日志，默认关闭
     */
    private static boolean logger;
    /**
     * 缓存时间，默认七天
     */
    private static long expiredTime;


    @Value("${releasedao.config.logger:false}")
    public void setLogger(boolean logger) {
        PropertyConfig.logger = logger;
    }

    @Value("${releasedao.config.camel-case:true}")
    public void setCamelCase(boolean camelCase) {
        PropertyConfig.camelCase = camelCase;
    }


    @Value("${releasedao.config.cache-expired-time:604800}")
    public void setExpiredTime(long expiredTime) {
        PropertyConfig.expiredTime = expiredTime;
    }

    public static boolean isLogger() {
        return logger;
    }

    public static boolean isCamelCase() {
        return camelCase;
    }


    public static long getExpiredTime() {
        return expiredTime;
    }


}

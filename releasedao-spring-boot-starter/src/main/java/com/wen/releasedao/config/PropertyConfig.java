package com.wen.releasedao.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Author Administrator
 * @create 2022/7/12 11:53
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "releasedao.config")
public class PropertyConfig {
    /**
     * 默认开启驼峰式转换
     */
    private boolean camelCase = true;
    /**
     * 默认七天的缓存时间
     */
    private long expiredTime = TimeUnit.DAYS.toSeconds(7);
}

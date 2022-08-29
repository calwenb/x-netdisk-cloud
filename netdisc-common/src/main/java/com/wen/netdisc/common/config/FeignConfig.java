package com.wen.netdisc.common.config;

import com.wen.netdisc.common.interceptor.HeaderInterceptor;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * rpc传递配置类
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new HeaderInterceptor();
    }

}
package com.wen.netdisc.user.api.config;

import com.wen.netdisc.common.interceptor.NormInterceptor;
import com.wen.netdisc.user.api.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Token验证拦截器规则配置类
 *
 * @author calwen
 **/

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public NormInterceptor normInterceptor() {
        return new NormInterceptor();
    }

    @Bean
    AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截不符合规范的请求
        registry.addInterceptor(normInterceptor());
        //验证拦截
        registry.addInterceptor(authenticationInterceptor());
    }


}

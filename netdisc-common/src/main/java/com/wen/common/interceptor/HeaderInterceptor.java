package com.wen.common.interceptor;

import com.wen.common.enums.TokenEnum;
import com.wen.commutil.util.LoggerUtil;
import com.wen.commutil.util.NullUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class HeaderInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String token = attributes.getRequest().getHeader(TokenEnum.TOKEN.getProperty());
            if (!NullUtil.hasNull(token)) {
                LoggerUtil.info("feignClient requestHeader  add token:==>" + token, this.getClass());
                requestTemplate.header("token", token);
            }
        }

    }
}
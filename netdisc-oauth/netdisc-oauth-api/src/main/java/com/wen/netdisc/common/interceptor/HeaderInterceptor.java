package com.wen.netdisc.common.interceptor;

import com.wen.commutil.util.LoggerUtil;
import com.wen.commutil.util.NullUtil;
import com.wen.netdisc.common.enums.TokenEnum;
import feign.RequestInterceptor;
import feign.RequestTemplate;
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
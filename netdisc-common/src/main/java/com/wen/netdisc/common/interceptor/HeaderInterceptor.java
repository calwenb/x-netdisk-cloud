package com.wen.netdisc.common.interceptor;

import com.wen.commutil.util.NullUtil;
import com.wen.netdisc.common.enums.TokenEnum;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * rpc传递令牌
 *
 * @author calwen
 * @since 2022/8/29
 */
public class HeaderInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String token = attributes.getRequest().getHeader(TokenEnum.TOKEN.getProperty());
            if (!NullUtil.hasNull(token)) {
                requestTemplate.header(TokenEnum.HEADER.getProperty(), token);
            }
        }

    }
}
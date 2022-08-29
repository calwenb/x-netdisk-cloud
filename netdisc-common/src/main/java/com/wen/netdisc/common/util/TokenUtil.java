package com.wen.netdisc.common.util;

import com.wen.netdisc.common.enums.TokenEnum;
import com.wen.netdisc.common.exception.OauthException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class TokenUtil {

    public static String getHeaderToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes == null ? null : requestAttributes.getRequest();
        if (request == null) {
            throw new OauthException("验证失败");
        }

        return Optional.ofNullable(request.getHeader(TokenEnum.HEADER.getProperty())).orElseThrow(() -> new OauthException("未携带令牌"));
    }

}

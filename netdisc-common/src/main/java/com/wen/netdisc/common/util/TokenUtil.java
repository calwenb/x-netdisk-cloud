package com.wen.netdisc.common.util;

import com.wen.netdisc.common.enums.TokenEnum;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

    public static String headerToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes == null ? null : requestAttributes.getRequest();
        return request.getHeader(TokenEnum.TOKEN.getProperty());
    }

}

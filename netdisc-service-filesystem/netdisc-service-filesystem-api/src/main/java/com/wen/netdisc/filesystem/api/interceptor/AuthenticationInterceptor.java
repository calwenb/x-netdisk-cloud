package com.wen.netdisc.filesystem.api.interceptor;

import com.alibaba.fastjson2.JSON;
import com.wen.netdisc.common.annotation.PassAuth;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.oauth.client.feign.OauthClient;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * token验证拦截器类
 * 用于后端接口的验证，
 * 调用后端接口需携带token进行身份验证，
 * 使用@PassToken注解则跳过拦截器
 *
 * @author calwen
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Resource
    OauthClient oauthClient;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object object) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        // 从 http 请求参数中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有PassToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassAuth.class)) {
            PassAuth passAuth = method.getAnnotation(PassAuth.class);
            if (passAuth.required()) {
                return true;
            }
        }
        if (token == null) {
            ResultVO<String> resultVO = ResultUtil.unauthorized();
            response.getWriter().println(JSON.toJSONString(resultVO));
            return false;
        }
        // 去验证中心验证
        if (oauthClient.verifyToken().getData()) {
            return true;
        }
        ResultVO<String> resultVO = ResultUtil.unauthorized();
        response.getWriter().println(JSON.toJSONString(resultVO));
        return false;
    }


}
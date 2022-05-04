package com.wen.oauth.serivce;

import com.wen.common.pojo.User;

/**
 * TokenService  业务类
 * 1.生成token令牌
 * 2.从请求头获取token令牌
 * 3.解析token令牌成user类
 *
 * @author Mr.文
 */
public interface TokenService {
    String getToken(User user);

    User getTokenUser(String token);
}

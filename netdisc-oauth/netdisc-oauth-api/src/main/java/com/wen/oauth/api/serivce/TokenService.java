package com.wen.oauth.api.serivce;

import com.wen.common.pojo.User;

/**
 * TokenService  业务类
 * 1.生成token令牌
 * 2.从请求头获取token令牌
 * 3.解析token令牌成user类
 *
 * @author calwen
 */
public interface TokenService {
    void saveToken(String token, Integer userType, int hour);

    String saveToken(Integer uid, Integer userType, int hour);

    String getToken(Integer uid);

    int getTokenUserId(String token);

    User getTokenUser(String token);

    boolean verifyToken(String token);

    boolean removeToken(String token);

    Long getExpireTime(String token);

    boolean renew(String token, int hour);


}

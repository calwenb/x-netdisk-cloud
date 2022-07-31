package com.wen.netdisc.oauth.api.serivce;

import com.wen.netdisc.common.pojo.User;

/**
 * TokenService  业务类
 * 1.生成token令牌
 * 2.从请求头获取token令牌
 * 3.解析token令牌成user类
 *
 * @author calwen
 */
public interface TokenService {
    String saveToken(Integer uid, Integer userType, int hour);

    String createToken(Integer uid);

    Integer getTokenUserId();

    User getTokenUser();

    boolean verifyToken();

    boolean removeToken();

    Long getExpireTime();

    boolean renew( Integer hour);


}

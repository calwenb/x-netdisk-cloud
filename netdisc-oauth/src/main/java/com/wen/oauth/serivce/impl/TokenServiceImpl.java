package com.wen.oauth.serivce.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wen.common.pojo.Result;
import com.wen.common.pojo.User;
import com.wen.oauth.serivce.TokenService;
import com.wen.oauth.serivce.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * token 下发
 *
 * @author
 */
@Service

public class TokenServiceImpl implements TokenService {

    @Resource
    UserService userService;

    /**
     * 用user换取token
     *
     * @return
     */
    @Override
    public String getToken(User user) {
        Date start = new Date();
        //一小时有效时间
        long currentTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(360 * 2);
        Date end = new Date(currentTime);

        String token = "";
        token = JWT.create()
                .withAudience(String.valueOf(user.getId()))
                .withIssuedAt(start)
                .withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassWord()));
        return token;
    }

    /**
     * 用token换取user信息
     *
     * @return
     */
    @Override
    public User getTokenUser(String token) {
        String userId = JWT.decode(token).getAudience().get(0);
        Result<User> result = userService.getUserById(userId);
        if (result.getCode() != 200) {
            throw new RuntimeException("请求用户服务接口失败");
        }
        return result.getData();
    }

}
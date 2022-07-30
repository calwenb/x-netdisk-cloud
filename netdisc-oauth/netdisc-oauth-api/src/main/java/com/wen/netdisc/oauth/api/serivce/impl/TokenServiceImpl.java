package com.wen.netdisc.oauth.api.serivce.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wen.netdisc.common.enums.RedisEnum;
import com.wen.netdisc.common.enums.TokenEnum;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.oauth.api.serivce.TokenService;
import com.wen.netdisc.oauth.api.mapper.UserMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/***
 * token 下发
 * @author
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Resource
    UserMapper userMapper;

    @Resource
    RedisTemplate redisTemplate;
    private final static String JWT_SECRET = TokenEnum.JWT_SECRET.getProperty();
    private final static String TOKEN_PREFIX = RedisEnum.TOKEN_PREFIX.getProperty();

    @Override
    public String createToken(Integer uid) {
        return JWT.create()
                .withAudience(String.valueOf(uid))
                .sign(Algorithm.HMAC256(JWT_SECRET));
    }


    @Override
    public Integer getTokenUserId() {
        String token = headerToken();
        String userId = JWT.decode(token).getAudience().get(0);
        return Integer.parseInt(userId);
    }

    @Override
    public User getTokenUser() {
        User user = userMapper.getUserById(this.getTokenUserId());
        if (user != null) {
            user.setPassWord(null);
        }
        return user;
    }


    @Override
    public String saveToken(Integer uid, Integer userType, int hour) {
        String token = this.createToken(uid);
        redisTemplate.opsForValue().set(TOKEN_PREFIX + token, userType, hour, TimeUnit.HOURS);
        return token;
    }

    @Override
    public boolean removeToken() {
        String token = headerToken();
        return redisTemplate.delete(TOKEN_PREFIX + token);
    }

    @Override
    public Long getExpireTime() {
        String token = headerToken();
        return redisTemplate.opsForValue().getOperations().getExpire(TOKEN_PREFIX + token);
    }

    @Override
    public boolean renew(Integer hour) {
        String token = headerToken();
        return redisTemplate.expire(TOKEN_PREFIX + token, hour, TimeUnit.HOURS);
    }


    @Override
    public boolean verifyToken() {
        String token = headerToken();
        Object o = redisTemplate.opsForValue().get(TOKEN_PREFIX + token);
        return o != null;
    }


    private String headerToken() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = requestAttributes == null ? null : requestAttributes.getRequest();
        return request.getHeader("token");
    }


}
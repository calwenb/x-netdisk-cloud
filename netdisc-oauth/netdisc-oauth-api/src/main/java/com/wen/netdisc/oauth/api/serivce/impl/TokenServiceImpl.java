package com.wen.netdisc.oauth.api.serivce.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.wen.netdisc.common.enums.RedisEnum;
import com.wen.netdisc.common.enums.TokenEnum;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.common.util.TokenUtil;
import com.wen.netdisc.oauth.api.serivce.TokenService;
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 验证token，token下发
 *
 * @author calwen
 * @since 2022/8/29
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    BaseMapper baseMapper;

    @Resource
    RedisTemplate<String, Integer> redisTemplate;
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
        String token = TokenUtil.getHeaderToken();
        String userId = JWT.decode(token).getAudience().get(0);
        return Integer.parseInt(userId);
    }

    @Override
    public User getTokenUser() {
        User user = baseMapper.getById(User.class, this.getTokenUserId());
        if (user == null) {
            throw new FailException("获取用户信息失败");
        }
        user.setPassWord(null);
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
        String token = TokenUtil.getHeaderToken();
        return Optional.ofNullable(redisTemplate.delete(TOKEN_PREFIX + token)).orElse(false);
    }

    @Override
    public Long getExpireTime() {
        String token = TokenUtil.getHeaderToken();
        return redisTemplate.opsForValue().getOperations().getExpire(TOKEN_PREFIX + token);
    }

    @Override
    public boolean renew(Integer hour) {
        String token = TokenUtil.getHeaderToken();
        return Optional.ofNullable(redisTemplate.expire(TOKEN_PREFIX + token, hour, TimeUnit.HOURS)).orElse(false);
    }


    @Override
    public boolean verifyToken() {
        String token = TokenUtil.getHeaderToken();
        Object o = redisTemplate.opsForValue().get(TOKEN_PREFIX + token);
        return o != null;
    }


}
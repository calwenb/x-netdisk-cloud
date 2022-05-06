package com.wen.oauth.serivce.fallback;

import com.wen.common.pojo.Result;
import com.wen.common.pojo.User;
import com.wen.oauth.serivce.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFallback implements UserService {
    @Override
    public Result<User> getUserById(String userId) {
        return Result.error("获取用户信息服务失败，服务熔断中...");
    }
}

package com.wen.user.service.fallback;

import com.wen.common.pojo.Result;
import com.wen.common.pojo.User;
import com.wen.user.service.FileStoreService;
import com.wen.user.service.TokenService;
import org.springframework.stereotype.Component;

/**
 * UserFallback类
 * 处理熔断异常
 *
 * @author Mr.文
 */
@Component
public class UserFallback implements FileStoreService, TokenService {

    private Result fallback(String msg) {
        return Result.error(msg + "服务失败，服务熔断中...");
    }

    @Override
    public Result initStore(int userId) {
        return this.fallback("初始化仓库");
    }

    @Override
    public Result getToken(String user) {
        return this.fallback("获得令牌");
    }

    @Override
    public Result<User> getTokenUser(String token) {
        return this.fallback("获得用户信息");
    }
}

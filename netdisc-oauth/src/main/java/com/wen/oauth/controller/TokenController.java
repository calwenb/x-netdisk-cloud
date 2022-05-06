package com.wen.oauth.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.wen.common.pojo.Result;
import com.wen.common.pojo.User;
import com.wen.common.utils.ResponseUtil;
import com.wen.oauth.serivce.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {
    @Resource
    TokenService tokenService;

    @GetMapping("/get_t")
    @SentinelResource(value = "getToken", fallback = "tokenFallback")
    public Result<String> getToken(@RequestParam("user") String user) {
        User userP = JSON.parseObject(user, User.class);
        return Result.success(tokenService.getToken(userP));
    }

    @GetMapping("/get_u")
    @SentinelResource(value = "getTokenUser", fallback = "tokenFallback")
    public Result<User> getTokenUser(@RequestParam("token") String token) {
        return Result.success(tokenService.getTokenUser(token));
    }


    /**
     * token接口通用降级处理方法
     *
     * @param e
     * @return
     */
    public String tokenFallback(Throwable e) {
        String msg = "错误异常，进入熔断中...";
        log.error(e.getMessage());
        return ResponseUtil.error(msg);
    }
}

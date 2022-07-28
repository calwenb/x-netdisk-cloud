/*
package com.wen.oauth.api.controller.api;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.wen.common.pojo.User;
import com.wen.common.utils.ResultUtil;
import com.wen.commutil.vo.ResultVO;
import com.wen.oauth.api.serivce.TokenService;
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
    public ResultVO<String> getToken(@RequestParam("user") String user) {
        User userP = JSON.parseObject(user, User.class);
        return ResultUtil.success(tokenService.getToken(userP));
    }

    @GetMapping("/get_u")
    @SentinelResource(value = "getTokenUser", fallback = "tokenFallback")
    public ResultVO<User> getTokenUser(@RequestParam("token") String token) {
        return ResultUtil.success(tokenService.getTokenUser(token));
    }


    */
/**
     * token接口通用降级处理方法
     *
     * @param e
     * @return
     *//*

    public ResultVO<String> tokenFallback(Throwable e) {
        String msg = "错误异常，进入熔断中...";
        log.error(e.getMessage());
        return ResultUtil.error(msg);
    }
}
*/

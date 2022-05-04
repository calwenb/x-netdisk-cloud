package com.wen.oauth.controller;

import com.alibaba.fastjson.JSON;
import com.wen.common.pojo.Result;
import com.wen.common.pojo.User;
import com.wen.oauth.serivce.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {
    @Autowired
    TokenService tokenService;

    @GetMapping("/get_t")
    public Result<String> getToken(@RequestParam("user") String user) {
        User userP = JSON.parseObject(user, User.class);
        return Result.success(tokenService.getToken(userP));

    }

    @GetMapping("/get_u")
    public Result<User> getTokenUser(@RequestParam("token") String token) {
        return Result.success(tokenService.getTokenUser(token));
    }
}

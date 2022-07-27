package com.wen.filesystem.api.servcie;

import com.wen.common.pojo.Result;
import com.wen.common.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TokenService  业务类
 * 1.生成token令牌
 * 2.从请求头获取token令牌
 * 3.解析token令牌成user类
 * @author Mr.文
 */

@FeignClient(value = "netdisc-oauth", path = "/token")
public interface TokenService {

    @GetMapping("/get_t")
    Result getToken(@RequestParam("user") String user);

    @GetMapping("/get_u")
    Result<User> getTokenUser(@RequestParam("token") String token);
}
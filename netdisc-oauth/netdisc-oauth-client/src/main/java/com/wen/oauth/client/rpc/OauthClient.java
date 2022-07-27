package com.wen.oauth.client.rpc;

import com.wen.common.pojo.User;
import com.wen.commutil.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(value = "netdisc-oauth", path = "/token", fallback = UserFallback.class)
@FeignClient(value = "netdisc-oauth", path = "/tokens")
public interface OauthClient {
    /**
     * 用户id生成token
     */
    @GetMapping
    ResultVO<String> getToken(@RequestParam("uid") Integer uid);

    /**
     * 用token换取user
     */
    @GetMapping("/user")
    ResultVO<User> getUser(@RequestParam("token") String token);

    /**
     * 用token直接换取uid
     */
    @GetMapping("/uid")
    ResultVO<Integer> getUserId(@RequestParam("token") String token);

    @PostMapping("")
    ResultVO<String> saveToken(Integer uid, Integer userType, Integer hour);

    @DeleteMapping("")
    ResultVO<String> removeToken(String token);
}

package com.wen.netdisc.oauth.client.feign;

import com.wen.netdisc.common.pojo.User;
import com.wen.commutil.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author calwen
 * @create 2022/7/28 9:22
 */
public interface OauthResource {
    /**
     * 用户id生成token
     */
    @GetMapping
    ResultVO<String> createToken(@RequestParam("uid") Integer uid);


    /**
     * 用token换取user
     */

    @GetMapping("/user")
    ResultVO<User> getUser();

    /**
     * 用token直接换取uid
     */
    @GetMapping("/uid")
    ResultVO<Integer> getUserId();

    @PostMapping
    ResultVO<String> saveToken(@RequestParam("uid") Integer uid,
                               @RequestParam("userType") Integer userType,
                               @RequestParam("hour") Integer hour);

    @DeleteMapping
    ResultVO<String> removeToken(@RequestParam("token") String token);
}

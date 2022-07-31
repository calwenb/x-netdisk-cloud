package com.wen.netdisc.oauth.api.controller.rpc;

import com.wen.commutil.annotation.PassAuth;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.oauth.api.serivce.TokenService;
import com.wen.netdisc.oauth.client.feign.OauthResource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/rpc/tokens")
public class OauthResourceImpl implements OauthResource {
    @Resource
    TokenService tokenService;

    @Override
    @GetMapping
    public ResultVO<String> createToken(Integer uid) {
        String token = tokenService.createToken(uid);
        return ResultUtil.success(token);
    }

    @Override
    @GetMapping("/user")
    public ResultVO<User> getUser() {
        User user = tokenService.getTokenUser();
        return ResultUtil.success(user);
    }

    @Override
    @GetMapping("/uid")
    public ResultVO<Integer> getUserId() {
        int userId = tokenService.getTokenUserId();
        return ResultUtil.success(userId);
    }

    @PassAuth
    @Override
    @PostMapping
    public ResultVO<String> saveToken(Integer uid, Integer userType, Integer hour) {
        String token = tokenService.saveToken(uid, userType, hour);
        return ResultUtil.success(token);
    }

    @Override
    @DeleteMapping
    public ResultVO<String> removeToken(String token) {
        if (tokenService.removeToken()) {
            return ResultUtil.successDo();
        }
        return ResultUtil.errorDo();
    }

    @Override
    @GetMapping("/verify")
    public ResultVO<Boolean> verifyToken() {
        //已经过验证拦截器，可直接返回true
        return ResultUtil.success(true);
    }


}

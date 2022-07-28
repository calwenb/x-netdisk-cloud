package com.wen.oauth.api.controller.rpc;

import com.wen.common.pojo.User;
import com.wen.common.util.ResultUtil;
import com.wen.commutil.vo.ResultVO;
import com.wen.oauth.api.serivce.TokenService;
import com.wen.oauth.client.rpc.OauthClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/tokens")
public class OathClientImpl implements OauthClient {
    @Resource
    TokenService tokenService;

    @Override
    @GetMapping("")
    public ResultVO<String> getToken(Integer uid) {
        String token = tokenService.getToken(uid);
        return ResultUtil.success(token);
    }

    @Override
    @GetMapping("/user")
    public ResultVO<User> getUser(String token) {
        User user = tokenService.getTokenUser(token);
        return ResultUtil.success(user);
    }

    @Override
    @GetMapping("/uid")
    public ResultVO<Integer> getUserId(String token) {
        int userId = tokenService.getTokenUserId(token);
        return ResultUtil.success(userId);
    }

    @Override
    @PostMapping("")
    public ResultVO<String> saveToken(Integer uid, Integer userType, Integer hour) {
        String token = tokenService.saveToken(uid, userType, hour);
        return ResultUtil.success(token);
    }

    @Override
    public ResultVO<String> removeToken(String token) {
        if (tokenService.removeToken(token)) {
            return ResultUtil.successDo();
        }
        return ResultUtil.errorDo();
    }


}

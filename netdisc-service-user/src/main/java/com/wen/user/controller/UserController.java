package com.wen.user.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.wen.common.annotation.PassToken;
import com.wen.common.pojo.Result;
import com.wen.common.pojo.User;
import com.wen.common.utils.NullUtil;
import com.wen.common.utils.ResponseUtil;
import com.wen.user.service.TokenService;
import com.wen.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * UserController类
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @Resource
    TokenService tokenService;

    @PassToken
    @SentinelResource(value = "login", defaultFallback = "userFallback", blockHandler = "loginRegisterHot")
    @GetMapping("/login")
    public String login(@RequestParam("loginName") String loginName,
                        @RequestParam("password") String password) {
        if (NullUtil.hasNull(loginName, password)) {
            return NullUtil.msg();
        }
        User user = userService.login(loginName, password);
        if (user == null) {
            return ResponseUtil.error("账号或者密码错误");
        }
        System.out.println(user.getUserName() + "登录");
        Result result = tokenService.getToken(JSON.toJSONString(user));
        if (result.getCode() == 200) {
            String token = String.valueOf(result.getData());
            return ResponseUtil.success(token);
        }
        return ResponseUtil.error("登录错误");
    }


    @PassToken
    @SentinelResource(value = "register", defaultFallback = "userFallback", blockHandler = "loginRegisterHot")
    @PostMapping("/register")
    public String register(@RequestParam("loginName") String userName,
                           @RequestParam("loginName") String loginName,
                           @RequestParam("password") String password) {
        if ((NullUtil.hasNull(userName, loginName, password))) {
            return NullUtil.msg();
        }
        Map<String, Object> rs = userService.register(userName, loginName, password);
        if (rs.containsKey("error")) {
            return ResponseUtil.error(rs.get("error").toString());
        }
        Result serviceRs = tokenService.getToken(JSON.toJSONString(rs.get("user")));
        String token = String.valueOf(serviceRs.getData());
        return ResponseUtil.success(token);
    }

    /**
     * 登录限流，防止大量同一账号恶意登录
     */
    public String loginRegisterHot(String loginName, String password, BlockException e) {
        String msg = "账号:" + loginName + " ，请求频繁，请稍后再试...";
        return ResponseUtil.error(msg);
    }

    @SentinelResource(value = "userGlobal", defaultFallback = "userFallback")
    @GetMapping("/getUser")
    public String getUserByToken(@RequestParam("token") String token) {
        if ((NullUtil.hasNull(token))) {
            return NullUtil.msg();
        }
        Object data = tokenService.getTokenUser(token).getData();
        if (data == null) {
            return ResponseUtil.error("错误令牌!!");
        }
        return ResponseUtil.success(JSON.toJSONString(data));
    }

    @SentinelResource(value = "userGlobal", defaultFallback = "userFallback")
    @PutMapping("/updatePassword")
    public String updatePassword(@RequestParam("token") String token,
                                 @RequestParam("password") String password) {
        if ((NullUtil.hasNull(token))) {
            return NullUtil.msg();
        }
        Result<User> result = tokenService.getTokenUser(token);
        User user = result.getData();
        if (user == null) {
            return ResponseUtil.error("错误令牌!!");
        }
        try {
            user.setPassWord(password);
            userService.updateUser(user);
            return ResponseUtil.success(JSON.toJSONString(user));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("修改错误");
        }
    }

    @GetMapping("/get/{userId}")
    public Result<User> getUserById(@PathVariable String userId) {
        User user = userService.getUserById(Integer.parseInt(userId));
        return Result.success(user);
    }

    /**
     * user接口通用降级处理方法
     *
     * @param e
     * @return
     */
    public String userFallback(Throwable e) {
        String msg = "错误异常，进入熔断中...";
        log.error(e.getMessage());
        return ResponseUtil.error(msg);
    }

}

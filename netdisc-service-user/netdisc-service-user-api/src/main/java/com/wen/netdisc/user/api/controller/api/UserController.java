package com.wen.netdisc.user.api.controller.api;

import com.wen.commutil.annotation.PassAuth;
import com.wen.commutil.util.LoggerUtil;
import com.wen.commutil.util.NullUtil;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.exception.OauthException;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.common.util.TokenUtil;
import com.wen.netdisc.filesystem.client.rpc.FilesystemClient;
import com.wen.netdisc.oauth.client.feign.OauthClient;
import com.wen.netdisc.user.api.service.UserService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * UserController类
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    OauthClient oauthClient;
    @Resource
    FilesystemClient filesystemClient;


    @PassAuth
    @PostMapping("/login")
    public ResultVO<String> login(@RequestParam("loginName") String loginName, @RequestParam("password") String password, @RequestParam(value = "remember", defaultValue = "false") boolean remember) {
        String token = userService.login(loginName, password, remember);
        return ResultUtil.success(token);

    }

    @PassAuth
    @PostMapping("/register")
    public ResultVO<String> register(@RequestParam("loginName") String userName, @RequestParam("email") String email, @RequestParam("loginName") String loginName, @RequestParam("password") String password) {
        User user = new User(-1, userName, loginName, password, 2, "", email, "/#", new Date());
        String token = userService.register(user);
        return ResultUtil.success(token);
    }

    @PostMapping("/out-login")
    public ResultVO<String> outLogin() {
        String token = TokenUtil.headerToken();
        if (NullUtil.hasNull(token)) {
            throw new OauthException("未携带token");
        }
        return oauthClient.removeToken(token);
    }

    @GetMapping("/info")
    public ResultVO<User> getUserByToken() {
        return oauthClient.getUser();
    }

    @PutMapping("/password")
    public ResultVO<User> updatePassword(@RequestParam("password") String password) {
        User user = oauthClient.getUser().getData();
        try {
            user.setPassWord(password);
            userService.updateUser(user);
            return ResultUtil.success(user);
        } catch (Exception e) {
            LoggerUtil.error(e.getMessage(), UserController.class);
            throw new FailException("修改失败");
        }
    }

    @PassAuth
    @PostMapping("/send-code")
    public ResultVO<String> sendCode(@RequestParam("loginName") String loginName, @RequestParam("email") String email) {
        if (userService.sendCode(loginName, email)) {
            return ResultUtil.success("发送成功，三分钟内有效");
        }
        return ResultUtil.error("发送失败。输入用户预留邮箱，未预留邮箱暂不支持服务");
    }


    @PassAuth
    @PutMapping("/re-pwd")
    public ResultVO<String> repwd(@RequestParam("loginName") String loginName, @RequestParam("password") String password, @RequestParam("code") String code) {

        if (!userService.verifyCode(loginName, code)) {
            return ResultUtil.error("验证码不正确或已失效");
        }
        if (userService.repwd(loginName, password)) {
            return ResultUtil.success("密码重置成功");
        }
        return ResultUtil.error("密码重置失败");
    }

    @PostMapping("/upload-head")
    public ResultVO<String> uploadHead(@RequestParam("file") MultipartFile file, @RequestParam("userId") Integer userId) {
        if (userService.uploadHead(file, userId)) {
            return ResultUtil.success("头像上传成功");
        }
        return ResultUtil.error("头像上传失败");
    }

    @PutMapping("/{id}")
    public ResultVO<String> updateUser(@PathVariable Integer id, @RequestParam("userName") String userName, @RequestParam("phoneNumber") String phoneNumber, @RequestParam("email") String email) {
        try {
            User user = userService.getUserById(id);
            if (!userName.isEmpty()) {
                user.setUserName(userName);
            }
            if (!phoneNumber.isEmpty()) {
                user.setPhoneNumber(phoneNumber);
            }
            if (!email.isEmpty()) {
                user.setEmail(email);
            }
            userService.updateUser(user);
            return ResultUtil.success("修改信息成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.error("修改信息失败");
    }

    @GetMapping("/avatar")
    public ResponseEntity<InputStreamResource> getAvatar() {
        User user = oauthClient.getUser().getData();
        String avatarPath = user.getAvatar();
        if (avatarPath == null) {
            throw new FailException("未上传头像");
        }
        try {
            return filesystemClient.downloadComm(avatarPath);
        } catch (IOException e) {
            throw new FailException("获取头像失败");
        }
    }

    @PutMapping("/level")
    public ResultVO<String> applyUpLevel() {
        Integer uid = oauthClient.getUserId().getData();
        return userService.applyUpLevel(uid) ? ResultUtil.successDo("申请升级成功，待管理员审批") : ResultUtil.error("请勿多次申请升级");
    }

}

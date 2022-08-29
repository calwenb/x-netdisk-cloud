package com.wen.netdisc.user.api.controller.api;

import com.wen.commutil.annotation.PassAuth;
import com.wen.commutil.util.NullUtil;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.exception.OauthException;
import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.common.util.TokenUtil;
import com.wen.netdisc.filesystem.client.rpc.FilesystemClient;
import com.wen.netdisc.oauth.client.feign.OauthClient;
import com.wen.netdisc.user.api.dto.UserDto;
import com.wen.netdisc.user.api.service.UserService;
import com.wen.netdisc.user.api.util.UserUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

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
    public ResultVO<String> login(@RequestBody UserDto dto) {
        String token = userService.login(dto);
        return ResultUtil.success(token);

    }

    @PassAuth
    @PostMapping("/register")
    public ResultVO<String> register(@RequestBody UserDto dto) {
        String token = userService.register(dto);
        return ResultUtil.success(token);
    }

    @PostMapping("/out-login")
    public ResultVO<String> outLogin() {
        String token = TokenUtil.getHeaderToken();
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
    public ResultVO<User> upPassword(@RequestBody UserDto dto) {
        userService.upPassword(dto);
        return ResultUtil.successDo();

    }

    @PassAuth
    @PutMapping("/re-pwd")
    public ResultVO<String> repwd(@RequestParam("loginName") String loginName, @RequestParam("password") String password, @RequestParam("code") String code) {

        if (!userService.verifyCode(loginName, code)) {
            return ResultUtil.error("验证码不正确或已失效");
        }
        return userService.repwd(loginName, password) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

    @PassAuth
    @PostMapping("/send-code")
    public ResultVO<String> sendCode(@RequestParam("loginName") String loginName, @RequestParam("email") String email) {
        userService.sendCode(loginName, email);
        return ResultUtil.success("发送成功，三分钟内有效");
    }


    @PostMapping("/upload-head")
    public ResultVO<String> uploadHead(@RequestParam("file") MultipartFile file, @RequestParam("userId") Integer userId) {
        boolean b = userService.uploadHead(file, userId);
        return b ? ResultUtil.success("头像上传成功") : ResultUtil.error("头像上传失败");
    }

    @PutMapping("/{id}")
    public ResultVO<String> updateUser(@PathVariable Integer id, @RequestBody UserDto dto) {
        dto.setId(id);
        return userService.updateUser(dto) > 0 ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

    @GetMapping("/avatar")
    public ResponseEntity<InputStreamResource> getAvatar() {
        User user = UserUtil.getUser();
        String path = Optional.ofNullable(user.getAvatar()).orElseThrow(() -> new FailException("未上传头像"));
        try {
            return filesystemClient.downloadComm(path);
        } catch (IOException e) {
            throw new FailException("获取头像失败");
        }
    }

    @PutMapping("/level")
    public ResultVO<String> applyUpLevel() {
        Integer uid = UserUtil.getUid();
        return userService.applyUpLevel(uid) ? ResultUtil.successDo("申请升级成功，待管理员审批") : ResultUtil.error("请勿多次申请升级");
    }

}

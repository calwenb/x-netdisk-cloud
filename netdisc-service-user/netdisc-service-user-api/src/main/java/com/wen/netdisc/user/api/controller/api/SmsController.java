package com.wen.netdisc.user.api.controller.api;

import com.wen.netdisc.common.annotation.PassAuth;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.user.api.service.SmsMailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author calwen
 * @since 2022/9/8
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Resource
    SmsMailService service;

    @PassAuth
    @PostMapping("/send-code")
    public ResultVO<String> sendCode(@RequestParam String phoneNumber) {
        service.sendSmsCode(phoneNumber);
        return ResultUtil.success("发送成功，五分钟内有效");
    }

    @PassAuth
    @PostMapping("/verify-code")
    public ResultVO<String> verifyCode(@RequestParam String phoneNumber, @RequestParam String code) {
        boolean b = service.verifySmsCode(phoneNumber, code);
        return b ? ResultUtil.success("验证码正确") : ResultUtil.error("验证码不正确");
    }

}

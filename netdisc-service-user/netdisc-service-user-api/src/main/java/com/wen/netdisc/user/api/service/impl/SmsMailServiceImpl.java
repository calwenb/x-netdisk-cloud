package com.wen.netdisc.user.api.service.impl;

import com.wen.netdisc.common.enums.RedisEnum;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.util.NumberUtil;
import com.wen.netdisc.user.api.service.SmsMailService;
import com.wen.netdisc.user.api.util.SmsUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author calwen
 * @since 2022/9/8
 */
@Service
public class SmsMailServiceImpl implements SmsMailService {
    @Value("${spring.mail.from}")
    private String from;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendMail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FailException("发送失败");
        }

    }

    @Override
    public void sendSmsCode(String phoneNumber) {
        String key = RedisEnum.SMS_Mail_CODE_PREFIX.getProperty() + phoneNumber;
        Long expire = redisTemplate.opsForValue().getOperations().getExpire(key);
        expire = Optional.ofNullable(expire).orElse(0L);
        long waitTime = expire - TimeUnit.MINUTES.toSeconds(4);
        if (waitTime > 0) {
            throw new FailException("请在 " + waitTime + " 秒后重试");
        }
        String code = NumberUtil.createCode();
        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);
        SmsUtil.send(phoneNumber, code, "5");
    }

    @Override
    public boolean verifySmsCode(String phoneNumber, String inCode) {
        String key = RedisEnum.SMS_Mail_CODE_PREFIX.getProperty() + phoneNumber;
        String code = redisTemplate.opsForValue().get(key);
        return Objects.equals(inCode, code);
    }

}

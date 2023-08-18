package com.wen.netdisc.user.api.service;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/9/8
 */
public interface SmsMailService {
    /**
     * 发送邮件
     *
     * @param to      收件人邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    void sendMail(String to, String subject, String content);

    /**
     * 发送短信验证码
     *
     * @param phoneNumber 手机号码
     */
    void sendSmsCode(String phoneNumber);

    /**
     * 验证短信验证码
     *
     * @param phoneNumber 手机号码
     * @param inCode      输入的验证码
     * @return 验证结果
     */
    boolean verifySmsCode(String phoneNumber, String inCode);
}

package com.wen.netdisc.user.api.service;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/9/8
 */
public interface SmsMailService {
    void sendMail(String to, String subject, String content);

    void sendSmsCode(String phoneNumber);


    boolean verifySmsCode(String phoneNumber, String inCode);
}

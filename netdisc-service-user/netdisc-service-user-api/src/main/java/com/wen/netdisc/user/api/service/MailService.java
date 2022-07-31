package com.wen.netdisc.user.api.service;

public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
}

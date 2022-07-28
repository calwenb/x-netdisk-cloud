package com.wen.common.service;

public interface MailService {
    void sendSimpleMail(String to, String subject, String content);
}

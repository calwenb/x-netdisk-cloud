package com.wen.netdisc.user.api.util;

import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.oauth.client.feign.OauthClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
public class UserUtil {
    static OauthClient oauthClient;
    @Resource

    ApplicationContext context;

    @PostConstruct
    public void init() {
        oauthClient = context.getBean(OauthClient.class);
    }

    public static User getUser() {
        return oauthClient.getUser().getData();
    }

    public static Integer getUid() {
        return oauthClient.getUserId().getData();
    }
}

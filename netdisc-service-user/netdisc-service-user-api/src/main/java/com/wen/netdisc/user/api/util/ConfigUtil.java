package com.wen.netdisc.user.api.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ConfigUtil {

    @Value("${x-netdisc.sms.secret-id}")
    private String SMS_SECRET_ID;

    @Value("${x-netdisc.sms.secret-key}")
    private String SMS_SECRET_KEY;

    @Value("${x-netdisc.sms.endpoin}")
    private String SMS_ENDPOIN;

    @Value("${x-netdisc.sms.app_id}")
    private String SMS_APP_ID;

    @Value("${x-netdisc.sms.template-id}")
    private String SMS_TEMPLATE_ID;

    @Value("${x-netdisc.sms.sign-name}")
    private String SMS_SIGN_NAME;

}

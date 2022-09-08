package com.wen.netdisc.user.api.util;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import com.wen.netdisc.common.exception.FailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author calwen
 * @since 2022/9/8
 */
@Component
@Slf4j
public class SmsUtil {
    private static final String SUCCESS_CODE = "Ok";
    static ConfigUtil configUtil;
    @Resource
    ApplicationContext context;

    @PostConstruct
    public void init() {
        configUtil = context.getBean(ConfigUtil.class);
    }


    public static void send(String phoneNumber, String code, String expiration) {
        try {
            Credential cred = new Credential(configUtil.getSMS_SECRET_ID(), configUtil.getSMS_SECRET_KEY());
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(configUtil.getSMS_ENDPOIN());
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);

            SendSmsRequest req = new SendSmsRequest();
            String[] phoneSet = {phoneNumber};
            req.setPhoneNumberSet(phoneSet);
            req.setSmsSdkAppId(configUtil.getSMS_APP_ID());
            req.setSignName(configUtil.getSMS_SIGN_NAME());
            req.setTemplateId(configUtil.getSMS_TEMPLATE_ID());
            String[] templateParamSet = {code, expiration};
            req.setTemplateParamSet(templateParamSet);
            SendSmsResponse resp = client.SendSms(req);
            SendStatus[] statusSet = resp.getSendStatusSet();
            for (SendStatus status : statusSet) {
                if (!SUCCESS_CODE.equals(status.getCode())) {
                    log.error("腾讯短信服务 : \n" + SendSmsResponse.toJsonString(resp));
                    throw new FailException("发送短信失败");
                }
            }
            log.info("腾讯短信服务 : \n" + SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            throw new FailException("发送短信失败");
        }
    }
}

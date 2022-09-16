package com.wen.netdisc.user.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/9/8
 */
@Data
public class LoginPhoneDto {
    @NotBlank(message = " 电话号码不能为空 ")
    private String phone;
    @NotBlank(message = " 验证码不能为空 ")
    private String code;
}

package com.wen.netdisc.user.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private Integer id;
    private String userName;
    private String loginName;
    private String passWord;
    private Integer userType;
    private String phoneNumber;
    private String email;
    private String avatar;
    private Date registerTime;
    private String newPassWord;
    private Boolean remember;

}

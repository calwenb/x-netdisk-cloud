package com.wen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * User实体类
 * @author Mr.文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String userName;
    private String loginName;
    private String passWord;
    private int userType;
    private String phoneNumber;
    private String email;
    private String avatar;
    private Date registerTime;
}

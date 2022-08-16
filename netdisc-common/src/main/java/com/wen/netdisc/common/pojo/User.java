package com.wen.netdisc.common.pojo;

import com.wen.baseorm.core.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * User实体类
 *
 * @author calwen
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User implements Serializable {
    // @IdField(idType = IdTypeEnum.AUTO)
    private Integer id;
    private String userName;
    private String loginName;
    private String passWord;
    private Integer userType;
    private String phoneNumber;
    private String email;
    private String avatar;
    private Date registerTime;


}

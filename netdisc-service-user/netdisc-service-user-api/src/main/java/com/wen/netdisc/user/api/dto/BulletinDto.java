package com.wen.netdisc.user.api.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/9/6
 */
@Data
public class BulletinDto {
    private Integer id;
    private String title;
    private String content;
    private Integer level;
    private Boolean publish;
    private Date startTime;
    private Date endTime;
}

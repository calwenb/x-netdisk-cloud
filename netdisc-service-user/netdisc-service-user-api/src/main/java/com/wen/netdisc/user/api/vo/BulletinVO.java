package com.wen.netdisc.user.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author calwen
 * @since 2022/9/6
 */
@Data
public class BulletinVO {
    private Integer id;
    private String title;
    private String content;
    private String level;
    private Boolean publish;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;
}

package com.wen.netdisc.user.api.vo;

import lombok.Data;

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
}

package com.wen.netdisc.user.api.dto;

import lombok.Data;

/**
 * @author calwen
 * @since 2022/9/6
 */
@Data
public class BulletinFindDto {
    private String keyword;
    private Integer id;
    private String title;
    private String level;
    private Boolean publish;
    private Integer userId;
}

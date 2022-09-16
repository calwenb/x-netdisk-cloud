package com.wen.netdisc.user.api.service;

import com.wen.netdisc.common.pojo.Bulletin;
import com.wen.netdisc.user.api.dto.BulletinDto;
import com.wen.netdisc.user.api.dto.BulletinFindDto;

import java.util.List;

/**
 * @author calwen
 * @since 2022/9/6
 */
public interface BulletinService {
    List<Bulletin> list(BulletinFindDto findDto);

    /**
     * 只获取最需要的公告
     */
    Bulletin getNeed();

    Bulletin get(Integer id);


    Bulletin save(BulletinDto bulletinDto);


    Boolean del(Integer id);

    Bulletin push();
}

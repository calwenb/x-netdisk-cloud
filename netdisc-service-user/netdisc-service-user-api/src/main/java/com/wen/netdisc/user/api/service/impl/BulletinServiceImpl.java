package com.wen.netdisc.user.api.service.impl;

import com.wen.netdisc.common.pojo.Bulletin;
import com.wen.netdisc.user.api.dto.BulletinDto;
import com.wen.netdisc.user.api.dto.BulletinFindDto;
import com.wen.netdisc.user.api.service.BulletinService;
import com.wen.netdisc.user.api.util.UserUtil;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author calwen
 * @since 2022/9/6
 */
@Service
public class BulletinServiceImpl implements BulletinService {
    @Resource
    BaseMapper baseMapper;

    @Override
    public List<Bulletin> list(BulletinFindDto findDto) {
        QueryWrapper wrapper = new QueryWrapper();
        if (findDto.getId() != null) {
            wrapper.eq("id", findDto.getId());
        }
        if (!Strings.isBlank(findDto.getTitle())) {
            wrapper.eq("title", findDto.getTitle());
        }
        if (findDto.getLevel() != null) {
            wrapper.eq("level", findDto.getLevel());
        }
        if (findDto.getPublish() != null) {
            wrapper.eq("publish", findDto.getPublish());
        }
        if (findDto.getUserId() != null) {
            wrapper.eq("uid", findDto.getUserId());
        }
        if (!Strings.isBlank(findDto.getKeyword())) {
            wrapper.like("title,content", findDto.getKeyword());
        }
        wrapper.orderDesc("level");
        return baseMapper.getList(Bulletin.class, wrapper);
    }

    @Override
    public Bulletin getNeed() {
        BulletinFindDto findDto = new BulletinFindDto();
        findDto.setPublish(true);
        List<Bulletin> list = list(findDto);
        if (list == null || list.isEmpty()) {
            return new Bulletin();
        }
        return list.get(0);
    }

    @Override
    public Bulletin get(Integer id) {
        return baseMapper.getById(Bulletin.class, id);
    }


    @Override
    public Bulletin save(BulletinDto dto) {
        Bulletin bulletin = new Bulletin();
        BeanUtils.copyProperties(dto, bulletin);
        if (dto.getId() == null) {
            bulletin.setUserId(UserUtil.getUid());
            baseMapper.add(bulletin);
        } else {
            baseMapper.save(bulletin);
        }
        return bulletin;
    }


    @Override
    public Boolean del(Integer id) {
        return baseMapper.deleteById(Bulletin.class, id);
    }

    @Override
    public Bulletin push() {
        return null;
    }
}

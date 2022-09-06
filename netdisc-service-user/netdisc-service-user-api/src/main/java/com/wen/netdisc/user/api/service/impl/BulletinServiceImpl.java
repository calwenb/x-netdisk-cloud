package com.wen.netdisc.user.api.service.impl;

import com.wen.netdisc.common.pojo.Bulletin;
import com.wen.netdisc.user.api.dto.BulletinDto;
import com.wen.netdisc.user.api.dto.BulletinFindDto;
import com.wen.netdisc.user.api.service.BulletinService;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
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
        if (findDto.getTitle() != null) {
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
        return baseMapper.getList(Bulletin.class, wrapper);
    }

    @Override
    public Bulletin get(Integer id) {
        return baseMapper.getById(Bulletin.class, id);
    }

    @Override
    public Bulletin add(BulletinDto dto) {
        Bulletin bulletin = new Bulletin();
        BeanUtils.copyProperties(dto, bulletin);
        baseMapper.add(bulletin);
        return bulletin;
    }

    @Override
    public Bulletin update(BulletinDto dto) {
        Bulletin bulletin = new Bulletin();
        BeanUtils.copyProperties(dto, bulletin);
        baseMapper.save(bulletin);
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

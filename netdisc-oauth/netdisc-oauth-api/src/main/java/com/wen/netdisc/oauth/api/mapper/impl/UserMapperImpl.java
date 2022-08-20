package com.wen.netdisc.oauth.api.mapper.impl;

import com.wen.netdisc.common.pojo.User;
import com.wen.netdisc.oauth.api.mapper.UserMapper;
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author calwen
 * @create 2022/7/28 17:19
 */
@Repository
public class UserMapperImpl implements UserMapper {
    @Resource
    BaseMapper baseMapper;

    @Override
    public User getUserById(int id) {
        return baseMapper.selectTargetById(User.class, id);
    }
}

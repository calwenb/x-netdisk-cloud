package com.wen.oauth.api.mapper.impl;

import com.wen.baseorm.core.mapper.BaseMapper;
import com.wen.common.pojo.User;
import com.wen.oauth.api.mapper.UserMapper;
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

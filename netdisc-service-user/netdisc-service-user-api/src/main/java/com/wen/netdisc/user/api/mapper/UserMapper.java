package com.wen.netdisc.user.api.mapper;

import com.wen.netdisc.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * UserMapper类 持久层
 * 已被 releasedao “释放”
 *
 * @author calwen
 */
@Mapper
@Repository
public interface UserMapper {

    int addUser(User user);

}

package com.wen.netdisc.user.api.mapper;

import com.wen.netdisc.common.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * UserMapper类 持久层
 *
 * @author calwen
 */
@Mapper
@Repository
public interface UserMapper {
    int updateUser(User user);

    int addUser(User user);

    User getUserById(int id);

    User getUserByLName(String loginName);

}

package com.wen.mapper;

import com.wen.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserMapper类 持久层
 * @author Mr.文
 */
@Mapper
@Repository
@CacheConfig(cacheNames = "user")
public interface UserMapper {
    /**
     * 查询全部用户
     *
     * @return 全部用户
     */
    List<User> queryUsers();

    @CacheEvict(key = "'uid:'+#p0.id")
    int updateUser(User user);

    User login(String loginName, String pwd);

    int addUser(User user);

    @Cacheable(key = "'uid:'+#p0")
    User getUserById(int id);
}

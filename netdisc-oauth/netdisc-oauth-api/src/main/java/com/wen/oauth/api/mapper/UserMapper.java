package com.wen.oauth.api.mapper;

import com.wen.common.pojo.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;


/**
 * UserMapper类 持久层
 *
 * @author calwen
 */
@Repository
@CacheConfig(cacheNames = "user")
public interface UserMapper {


    @Cacheable(key = "'uid:'+#p0")
    User getUserById(int id);


}

package com.wen.netdisc.oauth.api.mapper;

import com.wen.netdisc.common.pojo.User;


/**
 * UserMapper类 持久层
 *
 * @author calwen
 */
//@CacheConfig(cacheNames = "user")
public interface UserMapper {
    //    @Cacheable(key = "'uid:'+#p0")
    User getUserById(int id);


}

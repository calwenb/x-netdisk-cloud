/*
package com.wen.user.api.service;

import com.wen.common.pojo.Result;
import com.wen.user.service.fallback.UserFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

*/
/**
 * FileStoreService业务类
 * 对仓库初始化、查询业务，并操作服务器I/O
 *
 * @author Mr.文
 *//*

@FeignClient(value = "netdisc-service-filesystem", path = "/store", fallback = UserFallback.class)
public interface FileStoreService {

    @PostMapping("/init")
    Result initStore(@RequestParam("uid") int userId);

    @DeleteMapping("/del/{storeId}")
    Result delStore(@PathVariable int storeId);

}
*/

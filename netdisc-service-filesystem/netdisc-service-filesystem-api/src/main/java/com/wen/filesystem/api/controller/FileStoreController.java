package com.wen.filesystem.api.controller;

import com.wen.common.pojo.Result;
import com.wen.commutil.annotation.PassAuth;
import com.wen.filesystem.servcie.FileStoreService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/store")
public class FileStoreController {
    @Resource
    FileStoreService fileStoreService;

    @PassAuth
    @PostMapping("/init")
    public Result initStore(@RequestParam("uid") int userId) {
        int storeId = fileStoreService.initStore(userId);
        if (storeId == -1) {
            return Result.error("初始化仓库失败");
        }
        return Result.success(storeId);
    }
}

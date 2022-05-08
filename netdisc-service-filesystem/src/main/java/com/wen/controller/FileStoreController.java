package com.wen.controller;

import com.wen.common.annotation.PassToken;
import com.wen.common.pojo.Result;
import com.wen.servcie.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/store")
public class FileStoreController {
    @Autowired
    FileStoreService fileStoreService;

    @PassToken
    @PostMapping("/init")
    public Result initStore(@RequestParam("uid") int userId) {
        int storeId = fileStoreService.initStore(userId);
        if (storeId == -1) {
            return Result.error("初始化仓库失败");
        }
        return Result.success(storeId);
    }
}

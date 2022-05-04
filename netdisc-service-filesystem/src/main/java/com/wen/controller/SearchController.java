package com.wen.controller;

import com.alibaba.fastjson.JSON;
import com.wen.common.utils.ResponseUtil;
import com.wen.common.utils.TokenUtil;
import com.wen.servcie.EsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * SearchController类
 *
 * @author Mr.文
 */
@RestController
@RequestMapping("/search")
public class SearchController extends BaseController {
    @Autowired
    EsService esService;

    @GetMapping("/s/{keyword}")
    public String searchData(@RequestParam("token") String token,
                             @PathVariable("keyword") String keyword) {
        int userId = Integer.parseInt(TokenUtil.getTokenUserId(token));
        try {
            int storeId = fileStoreService.queryStoreByUserId(userId).getFileStoreId();
            String rs = JSON.toJSONString(esService.searchData(storeId, keyword));
            return ResponseUtil.success(rs);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseUtil.error("搜索数据错误");
        }
    }
}

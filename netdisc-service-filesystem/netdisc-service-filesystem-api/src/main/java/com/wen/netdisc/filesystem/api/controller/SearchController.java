package com.wen.netdisc.filesystem.api.controller;

import com.alibaba.fastjson.JSON;
import com.wen.common.util.ResponseUtil;
import com.wen.netdisc.common.util.TokenUtil;
import com.wen.filesystem.servcie.EsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * SearchController类
 *
 * @author Mr.文
 */
@RestController
@RequestMapping("/search")
public class SearchController extends BaseController {
    @Resource
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

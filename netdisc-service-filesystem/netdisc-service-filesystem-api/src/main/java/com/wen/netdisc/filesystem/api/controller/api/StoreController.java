package com.wen.netdisc.filesystem.api.controller.api;


import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import com.wen.netdisc.oauth.client.feign.OauthClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {
    @Resource
    OauthClient oauthClient;
    @Resource
    StoreService storeService;

    @GetMapping("/my")
    public ResultVO<FileStore> queryStore() {
        int uid = oauthClient.getUserId().getData();
        FileStore store = storeService.queryStoreByUid(uid);
        if (store != null) {
            return ResultUtil.success(store);
        }
        return ResultUtil.error("获取仓库数据失败");
    }

    @GetMapping("/user-store")
    public ResultVO<List<FileStore>> queryUserStore() {
        List<FileStore> store = storeService.queryUserStore();
        return ResultUtil.success(store);
    }

    @PutMapping("/store")
    public ResultVO<String> updateStore(@RequestBody FileStore store) {
        if (storeService.updateStore(store)) {
            return ResultUtil.successDo();
        }
        return ResultUtil.errorDo();
    }

    @DeleteMapping("/{sid}")
    public ResultVO<String> del(@PathVariable("sid") Integer sid) {
        return storeService.delStore(sid) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

}

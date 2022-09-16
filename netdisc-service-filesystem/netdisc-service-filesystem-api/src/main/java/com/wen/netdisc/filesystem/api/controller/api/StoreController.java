package com.wen.netdisc.filesystem.api.controller.api;


import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Resource
    StoreService storeService;

    @GetMapping("/my")
    public ResultVO<FileStore> queryStore() {
        int uid = UserUtil.getUid();
        return ResultUtil.success(storeService.queryStoreByUid(uid));
    }

    @GetMapping("/user-store")
    public ResultVO<List<FileStore>> queryUserStore() {
        return ResultUtil.success(storeService.queryUserStore());
    }

    @PutMapping("/store")
    public ResultVO<String> updateStore(@RequestBody FileStore store) {
        return storeService.updateStore(store) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

    @DeleteMapping("/{sid}")
    public ResultVO<String> del(@PathVariable("sid") Integer sid) {
        return storeService.delStore(sid) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

}

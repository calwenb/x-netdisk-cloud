package com.wen.netdisc.filesystem.api.controller.api;

import com.alibaba.fastjson2.JSON;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.servcie.TrashService;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/trashs")
public class TrashController extends BaseController {
    @Resource
    TrashService trashService;

    @GetMapping("/data")
    public Object getData(@RequestParam("trashJSON") String trashJSON) {
        MyFile trash = JSON.parseObject(trashJSON, MyFile.class);
        return trashService.getData(trash);
    }


    @GetMapping("/list")
    public ResultVO<List<MyFile>> getList(@RequestParam("page") Integer page) {
        int uid = UserUtil.getUid();
        List<MyFile> list = trashService.getListByUid(uid, page);
        return ResultUtil.success(list);
    }


    @DeleteMapping("/by-file")
    public ResultVO<String> delTrash(String trashJSON) {
        MyFile trash = JSON.parseObject(trashJSON, MyFile.class);
        int uid = UserUtil.getUid();
        return trashService.deleteById(trash, uid) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

    @DeleteMapping("/restored")
    public ResultVO<String> restoredTrash(String trashJSON) {
        MyFile trash = JSON.parseObject(trashJSON, MyFile.class);
        int uid = UserUtil.getUid();
        return trashService.restored(trash, uid) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

}

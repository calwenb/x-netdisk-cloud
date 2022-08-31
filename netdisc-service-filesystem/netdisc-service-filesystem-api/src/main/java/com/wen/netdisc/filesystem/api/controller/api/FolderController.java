package com.wen.netdisc.filesystem.api.controller.api;


import com.alibaba.fastjson2.JSON;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.pojo.TreeNode;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.dto.FolderSaveDto;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * FolderController类
 *
 * @author calwen
 */
@RestController
@RequestMapping("/folders")
public class FolderController extends BaseController {

    @PostMapping
    public ResultVO<String> addFolder(@Valid @RequestBody FolderSaveDto dto) {
        return folderService.addFileFolder(dto) ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

    @DeleteMapping("/ids")
    public ResultVO<String> delFolder(@RequestParam("IdList") String folderId) {
        List<Integer> list = JSON.parseArray(folderId, Integer.class);
        int count = list.size();
        for (Integer fileId : list) {
            if (folderService.delFolder(fileId)) {
                count--;
            }
        }
        return count == 0 ? ResultUtil.successDo() : ResultUtil.error(count + " 个文件夹删除失败！");

    }

    @GetMapping("/tree")
    public ResultVO<TreeNode> getFolderTree() {
        int userId = UserUtil.getUid();
        TreeNode folderTree = folderService.getFolderTree(userId);
        return ResultUtil.success(folderTree);
    }

    @PutMapping("/{id}")
    public ResultVO<String> updateFolderName(@PathVariable int id, @RequestParam("newName") String newName) {
        return folderService.updateFolderName(id, newName)
                ? ResultUtil.successDo() : ResultUtil.errorDo();

    }
}

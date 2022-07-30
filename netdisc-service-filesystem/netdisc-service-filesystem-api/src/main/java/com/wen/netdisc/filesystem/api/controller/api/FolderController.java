package com.wen.netdisc.filesystem.api.controller.api;


import com.alibaba.fastjson2.JSON;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.pojo.TreeNode;
import com.wen.netdisc.common.util.ResultUtil;
import org.springframework.web.bind.annotation.*;

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
    public ResultVO<String> addFolder(@RequestParam("parent_id") String pFolderId,
                                      @RequestParam("name") String folderName) {
        try {
            Integer uid = oauthClient.getUserId().getData();
            FileStore store = storeService.queryStoreByUid(uid);
            FileFolder fileFolder = new FileFolder(-1, folderName, Integer.parseInt(pFolderId), store.getFileStoreId(), "");
            if (!folderService.addFileFolder(fileFolder)) {
                return ResultUtil.error("新建文件夹失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("新建文件夹失败");
        }
        return ResultUtil.success("新建文件夹成功");
    }

    @DeleteMapping("/ids")
    public ResultVO<String> delFolder(@RequestParam("IdList") String folderId) {
        List<String> list = JSON.parseArray(folderId, String.class);
        try {
            int count = 0;
            for (String fileId : list) {
                if (folderService.delFolder(Integer.parseInt(fileId))) {
                    count++;
                }
            }
            if (count == list.size()) {
                return ResultUtil.success("所选文件夹已删除");
            } else {
                return ResultUtil.error((list.size() - count) + " 个文件夹删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("执行异常");
        }
    }

    @GetMapping("/tree")
    public ResultVO<TreeNode> getFolderTree() {
        int userId = oauthClient.getUserId().getData();
        TreeNode folderTree = folderService.getFolderTree(userId);
        return ResultUtil.success(folderTree);
    }

    @PutMapping("/{id}")
    public ResultVO<String> updateFolderName(@PathVariable int id,
                                             @RequestParam("newName") String newName) {
        try {
            if (folderService.updateFolderName(id, newName)) {
                return ResultUtil.success("重命名成功");
            } else {
                return ResultUtil.error("重命名失败");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResultUtil.error("重命名失败");
        }
    }
}

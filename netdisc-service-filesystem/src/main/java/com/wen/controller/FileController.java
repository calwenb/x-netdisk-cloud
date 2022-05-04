package com.wen.controller;


import com.alibaba.fastjson.JSON;
import com.wen.common.annotation.PassToken;
import com.wen.common.pojo.User;
import com.wen.common.utils.FileUtil;
import com.wen.common.utils.NullUtil;
import com.wen.common.utils.ResponseUtil;
import com.wen.pojo.FileFolder;
import com.wen.pojo.FileStore;
import com.wen.pojo.MyFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * FileController类
 *
 * @author Mr.文
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("token") String token,
                             @RequestParam("fatherFileFolderId") String fatherFileFolderId) {
        if (file.isEmpty()) {
            return ResponseUtil.uploadFileError("文件为空");
        }
        // 判断上传文件大小
        if (!FileUtil.checkFileSize(file)) {
            return ResponseUtil.uploadFileError("上传文件大于2GB ");
        }
        if ("".equals(token)) {
            return ResponseUtil.error("请登录!");
        }
        User user = tokenService.getTokenUser(token).getData();
        int userId = user.getId();
        if (fileService.uploadFile(file, userId, fatherFileFolderId)) {
            return ResponseUtil.success(file.getOriginalFilename() + " 上传文件成功");
        }
        return ResponseUtil.error("未知错误!");
    }

    @GetMapping("/queryMyFiles")
    public String queryMyFiles(@RequestParam("token") String token,
                               @RequestParam("parentFolderId") String parentFolderId) {
        if (NullUtil.hasNull(token, parentFolderId)) {
            return ResponseUtil.error("有空参数！");
        }
        List<MyFile> myFiles;
        List<FileFolder> fileFolders;
        List fileAndFolds = new ArrayList();
        try {
            User user = tokenService.getTokenUser(token).getData();
            FileStore store = fileStoreService.queryStoreByUserId(user.getId());

            myFiles = fileService.queryMyFiles(user.getId(), Integer.parseInt(parentFolderId), -1);
            fileFolders = fileFolderService.queryFoldersByPId(store.getFileStoreId(), Integer.parseInt(parentFolderId));
            fileAndFolds.addAll(fileFolders);
            fileAndFolds.addAll(myFiles);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseUtil.error("执行异常");
        }
        return ResponseUtil.success(JSON.toJSONString(fileAndFolds));
    }

    @GetMapping("/queryFilesByType/{type}/P/{page}")
    public String queryFilesByType(@RequestParam("token") String token,
                                   @PathVariable String type,
                                   @PathVariable String page) {
        if (NullUtil.hasNull(token, type, page)) {
            return ResponseUtil.error("有空参数！");
        }
        List<MyFile> myFiles;
        try {
            User user = tokenService.getTokenUser(token).getData();
            myFiles = fileService.queryFilesByType(user.getId(), type, Integer.parseInt(page));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseUtil.error("执行异常");
        }
        return ResponseUtil.success(JSON.toJSONString(myFiles));
    }

    @DeleteMapping("/delByFileIds")
    public String delByFileIds(@RequestParam("token") String token,
                               @RequestParam("IdList") String fileIdList) {
        if (NullUtil.hasNull(token, fileIdList)) {
            return ResponseUtil.error("有空参数！");
        }
        List<String> list = JSON.parseArray(fileIdList, String.class);
        try {
            int count = 0;
            for (String fileId : list) {
                if (fileService.deleteByMyFileId(Integer.parseInt(fileId))) {
                    count++;
                }
            }
            if (count == list.size()) {
                return ResponseUtil.success("所选文件已删除");
            } else {
                return ResponseUtil.error((list.size() - count) + " 个文件删除失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.error("执行异常");
        }
    }

    @PassToken
    @GetMapping("/downByFileIds")
    public Object downByFileIds(@RequestParam("fileIdList") String fileIdList) {
        if (NullUtil.hasNull(fileIdList)) {
            return ResponseUtil.error("有空参数！");
        }
        List<String> list = JSON.parseArray(fileIdList, String.class);
        try {
            return fileService.downloadByMyFileId(Integer.parseInt(list.get(0)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.downloadFileError("下载失败");
        }
    }

    @PutMapping("/updateFileName")
    public String updateFileName(@RequestParam("fileId") String fileId,
                                 @RequestParam("newName") String newName) {
        if (NullUtil.hasNull(fileId, newName)) {
            return ResponseUtil.error("有空参数！");
        }
        try {
            if (fileService.updateFileName(Integer.parseInt(fileId), newName)) {
                return ResponseUtil.success("重命名成功");
            } else {
                return ResponseUtil.error("重命名失败");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseUtil.error("重命名失败");
        }
    }

    @GetMapping("/shareFile")
    public String shareFile(@RequestParam("fileId") String fileId) {
        String shareCode = fileService.shareFile(Integer.parseInt(fileId));
        if (shareCode == null) {
            return ResponseUtil.error("分享失败");
        }
        return ResponseUtil.success(shareCode);
    }

    @PassToken
    @GetMapping("/getShareFile")
    public String getShareFile(@RequestParam("shareCode") String shareCode) {
        MyFile file = null;
        try {
            file = fileService.getShareFile(shareCode);
            if (file == null) {
                return ResponseUtil.fileMiss("分享的文件不存在或过期!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseUtil.success(JSON.toJSONString(file));
    }
}

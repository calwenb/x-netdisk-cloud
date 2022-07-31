package com.wen.netdisc.filesystem.api.controller.api;


import com.alibaba.fastjson2.JSON;
import com.wen.commutil.annotation.PassAuth;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController extends BaseController {


    @GetMapping("/p/{page}")
    public ResultVO<List<MyFile>> queryFiles(@PathVariable String page) {
        Integer uid = oauthClient.getUserId().getData();
        List<MyFile> list = fileService.queryFilesByUid(uid, Integer.parseInt(page));
        return ResultUtil.success(list);
    }

    @GetMapping
    public ResultVO<List<MyFile>> queryFilesByType(@RequestParam("type") String type,
                                                   @RequestParam("page") String page) {
        Integer uid = oauthClient.getUserId().getData();
        List<MyFile> list = fileService.queryFilesByType(uid, type, Integer.parseInt(page));
        return ResultUtil.success(list);
    }


    @GetMapping("/data/p/{page}")
    public ResultVO<List<Map<String, String>>> queryFilesData(@PathVariable String page) throws IOException {
        Integer uid = oauthClient.getUserId().getData();
        return ResultUtil.success(fileService.queryFilesByUid(uid, Integer.parseInt(page), true));
    }


    @PostMapping("/upload")
    public ResultVO<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("fatherFileFolderId") String fatherFileFolderId) {

        if (file.isEmpty()) {
            return ResultUtil.error("文件为空");
        }
        // 判断上传文件大小
        if (!FileUtil.checkFileSize(file)) {
            return ResultUtil.error("上传文件大于2GB ");
        }

        Integer uid = oauthClient.getUserId().getData();
        if (fileService.uploadFile(file, uid, fatherFileFolderId)) {
            return ResultUtil.successDo(file.getOriginalFilename() + " 上传文件成功");
        }
        return ResultUtil.error("上传文件失败");
    }

    @GetMapping("/file-folder")
    public ResultVO<List<Object>> queryFiles(@RequestParam("parentFolderId") Integer parentFolderId) {
        List<MyFile> myFiles;
        List<FileFolder> fileFolders;
        List<Object> fileAndFolds = new ArrayList();
        Integer uid = oauthClient.getUserId().getData();
        FileStore store = storeService.queryStoreByUid(uid);
        myFiles = fileService.queryMyFiles(uid, parentFolderId, -1);
        fileFolders = folderService.queryFoldersByPId(store.getFileStoreId(), parentFolderId);
        fileAndFolds.addAll(fileFolders);
        fileAndFolds.addAll(myFiles);
        return ResultUtil.success(fileAndFolds);
    }


    @DeleteMapping("/ids")
    public ResultVO<String> delByFileIds(@RequestParam("IdList") String fileIdList) {
        List<String> list = JSON.parseArray(fileIdList, String.class);
        int count = 0;
        for (String fileId : list) {
            if (fileService.deleteByMyFileId(Integer.parseInt(fileId))) {
                count++;
            }
        }
        if (count == list.size()) {
            return ResultUtil.success("所选文件已删除");
        } else {
            return ResultUtil.error((list.size() - count) + " 个文件删除失败！");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downByFileIds(@RequestParam("fileIdList") String fileIdList, @RequestParam(value = "preview", defaultValue = "false") String preview) {
        List<String> list = JSON.parseArray(fileIdList, String.class);
        try {
            return fileService.downloadFile(Integer.parseInt(list.get(0)), Boolean.parseBoolean(preview));
        } catch (Exception e) {
            throw new FailException("下载文件失败");
        }
    }


    @PutMapping("/{id}")
    public ResultVO<String> updateFileName(@PathVariable("id") Integer id, @RequestParam("newName") String newName) {

        try {
            if (fileService.updateFileName(id, newName)) {
                return ResultUtil.success("重命名成功");
            } else {
                return ResultUtil.error("重命名失败");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResultUtil.error("重命名失败");
        }
    }

    @PostMapping("/share/{id}")
    public ResultVO<String> shareFile(@PathVariable Integer id) {
        String shareCode = fileService.shareFile(id);
        if (shareCode == null) {
            return ResultUtil.error("分享失败");
        }
        return ResultUtil.success(shareCode);
    }

    @PassAuth
    @GetMapping("/share/{code}")
    public ResultVO<MyFile> getShareFile(@PathVariable String code) {
        MyFile file = fileService.getShareFile(code);
        if (file == null) {
            return ResultUtil.error("分享的文件不存在或过期!");
        }

        return ResultUtil.success(file);
    }

    ////////////////


/*

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
            FileStore store = fileStoreService.queryStoreByUid(user.getId());

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

    @PassAuth
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

    @PassAuth
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
    }*/
}

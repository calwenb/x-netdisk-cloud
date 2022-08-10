package com.wen.netdisc.filesystem.api.controller.api;


import com.alibaba.fastjson2.JSON;
import com.wen.commutil.annotation.PassAuth;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.dto.ChunkDto;
import com.wen.netdisc.filesystem.api.servcie.ChunkService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import com.wen.netdisc.filesystem.api.vo.ChunkVo;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController extends BaseController {


    @GetMapping("/p/{page}")
    public ResultVO<List<MyFile>> queryFiles(@PathVariable String page) {
        Integer uid = UserUtil.getUid();
        List<MyFile> list = fileService.queryFilesByUid(uid, Integer.parseInt(page));
        return ResultUtil.success(list);
    }

    @GetMapping
    public ResultVO<List<MyFile>> queryFilesByType(@RequestParam("type") String type, @RequestParam("page") String page) {
        Integer uid = UserUtil.getUid();
        List<MyFile> list = fileService.queryFilesByType(uid, type, Integer.parseInt(page));
        return ResultUtil.success(list);
    }

    @PutMapping("/data")
    public ResultVO<String> updateData(@RequestParam("data") MultipartFile file, @RequestParam("id") Integer id) {
        fileService.updateData(file, id);
        return ResultUtil.successDo();
    }


    @GetMapping("/data/p/{page}")
    public ResultVO<List<Map<String, String>>> queryFilesData(@PathVariable String page) throws IOException {
        Integer uid = UserUtil.getUid();
        return ResultUtil.success(fileService.queryFilesByUid(uid, Integer.parseInt(page), true));
    }


    @PostMapping("/upload")
    public ResultVO<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Integer folderId) {

        if (file.isEmpty()) {
            return ResultUtil.error("文件为空");
        }
        // 判断上传文件大小
        if (!FileUtil.checkFileSize(file)) {
            return ResultUtil.error("上传文件大于2GB ");
        }

        Integer uid = UserUtil.getUid();
        if (fileService.uploadFile(file, uid, folderId)) {
            return ResultUtil.successDo(file.getOriginalFilename() + " 上传文件成功");
        }
        return ResultUtil.error("上传文件失败");
    }

    @Resource
    ChunkService chunkService;

    @GetMapping("/upload-big")
    public ResultVO<Integer> skipFile(ChunkDto chunkDto) {
        return chunkService.skip(chunkDto);
    }

    //大文件上传
    @PostMapping("/upload-big")
    public ResultVO<ChunkVo> saveChunk(ChunkDto chunkDto) {
        return chunkService.saveChunk(chunkDto);
    }

    @PostMapping("/upload-big/merge")
    public ResultVO<ChunkVo> mergeChunk(@RequestBody ChunkDto chunkDto) {
        return chunkService.merge(chunkDto);
    }

    @GetMapping("/file-folder")

    public ResultVO<List<Object>> queryFiles(@RequestParam("parentFolderId") Integer parentFolderId) {
        List<MyFile> myFiles;
        List<FileFolder> fileFolders;
        List<Object> fileAndFolds = new ArrayList();
        Integer uid = UserUtil.getUid();
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

}

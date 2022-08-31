package com.wen.netdisc.filesystem.api.controller.rpc;

import com.wen.netdisc.common.annotation.PassAuth;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.servcie.FileService;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.client.rpc.FileResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/rpc/filesystems")
public class FileResourceImpl implements FileResource {
    @Resource
    FileService fileService;
    @Resource
    StoreService storeService;

    @Override
    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO<Boolean> uploadFileComm(@RequestPart("file") MultipartFile file, String path) {
        boolean b = fileService.uploadFileComm(file, path);
        return ResultUtil.success(b);
    }

    @Override
    @PostMapping(path = "/uploadHead", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVO<String> uploadHead(@RequestPart("file") MultipartFile file) {
        String headRoot = FileUtil.getRootPath() + "head/";
        String path = headRoot + file.getOriginalFilename();
        if (fileService.uploadFileComm(file, path)) {
            return ResultUtil.success(path);
        }
        throw new FailException("保存头像失败");
    }

    @Override
    @PassAuth
    @PostMapping("/initStore")
    public ResultVO<Boolean> initStore(Integer uid) {
        boolean b = storeService.initStore(uid);
        return ResultUtil.success(b);
    }


    @Override
    @GetMapping("/downloadComm")
    public ResponseEntity<InputStreamResource> downloadComm(String path) throws IOException {
        return fileService.downloadComm(path);
    }
}

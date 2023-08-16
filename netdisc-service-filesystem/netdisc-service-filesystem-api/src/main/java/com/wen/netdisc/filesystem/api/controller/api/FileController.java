package com.wen.netdisc.filesystem.api.controller.api;


import com.alibaba.fastjson2.JSON;
import com.mysql.cj.util.StringUtils;
import com.wen.netdisc.common.vo.PageVO;
import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.common.annotation.PassAuth;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.dto.ChunkDto;
import com.wen.netdisc.filesystem.api.servcie.ChunkService;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import com.wen.netdisc.filesystem.api.vo.ChunkVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/files")
@Slf4j
public class FileController extends BaseController {

    @Resource
    ChunkService chunkService;

    @GetMapping("/p/{page}")
    public ResultVO<List<MyFile>> queryFiles(@PathVariable String page) {
        Integer uid = UserUtil.getUid();
        List<MyFile> list = fileService.queryFilesByUid(uid, Integer.parseInt(page));
        return ResultUtil.success(list);
    }

    @GetMapping("/thumbnail/list/{page}")
    public ResultVO<PageVO<Map<String, String>>> thumbnailList(@PathVariable Integer page) {
        Integer uid = UserUtil.getUid();
        return ResultUtil.success(fileService.thumbnailList(uid, page));
    }


    @GetMapping
    public ResultVO<List<MyFile>> queryFilesByType(@RequestParam("type") String type, @RequestParam("page") Integer page) {
        Integer uid = UserUtil.getUid();
        List<MyFile> list = fileService.queryFilesByType(uid, type, page);
        return ResultUtil.success(list);
    }

    @PutMapping("/data")
    public ResultVO<String> updateData(@RequestParam("data") MultipartFile file, @RequestParam("id") Integer id) {
        fileService.updateData(file, id);
        return ResultUtil.successDo();
    }

    @Deprecated
    @PostMapping("/upload")
    public ResultVO<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("folderId") Integer folderId) {
        if (file.isEmpty()) {
            return ResultUtil.error("文件为空");
        }
        Integer uid = UserUtil.getUid();
        if (fileService.uploadFile(file, uid, folderId)) {
            return ResultUtil.successDo(file.getOriginalFilename() + " 上传文件成功");
        }
        return ResultUtil.error("上传文件失败");
    }


    @GetMapping("/upload-big")
    public ResultVO<Integer> skipFile(ChunkDto chunkDto) {
        return chunkService.skip(chunkDto);
    }

    /**
     * 大文件上传
     */
    @PostMapping("/upload-big")
    public ResultVO<ChunkVo> saveChunk(ChunkDto chunkDto) {
        return chunkService.saveChunk(chunkDto);
    }

    @PostMapping("/upload-big/merge")
    public ResultVO<ChunkVo> mergeChunk(@RequestBody ChunkDto chunkDto) {
        return chunkService.merge(chunkDto);
    }

    @GetMapping("/file-folder/{parentFid}")
    public ResultVO<List<Object>> queryFiles(@PathVariable Integer parentFid) {
        List<Object> list = fileService.getFileAndFolder(parentFid);
        return ResultUtil.success(list);
    }


    @DeleteMapping("/ids")
    public ResultVO<String> delByFileIds(@RequestParam("IdList") String fileIdList) {
        List<Integer> list = JSON.parseArray(fileIdList, Integer.class);
        int count = 0;
        for (Integer id : list) {
            if (fileService.deleteById(id)) {
                count++;
            }
        }
        return count == list.size()
                ? ResultUtil.success("所选文件已删除")
                : ResultUtil.error((list.size() - count) + " 个文件删除失败！");
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downByFileIds(@RequestParam String fileIdList, @RequestParam(defaultValue = "false") String preview) {
        List<String> list = JSON.parseArray(fileIdList, String.class);
        try {
            return fileService.downloadFile(Integer.parseInt(list.get(0)), Boolean.parseBoolean(preview));
        } catch (Exception e) {
            log.error("下载文件失败 err ",e);
            throw new FailException("下载文件失败");
        }
    }


    @PutMapping("/{id}")
    public ResultVO<String> updateFileName(@PathVariable("id") Integer id, @RequestParam("newName") String newName) {
        boolean b = fileService.updateFileName(id, newName);
        return b ? ResultUtil.successDo() : ResultUtil.errorDo();
    }

    @PassAuth
    @GetMapping("/share/{code}")
    public ResultVO<MyFile> getShareFile(@PathVariable String code) {
        if (StringUtils.isNullOrEmpty(code)) {
            return ResultUtil.error("分享码不能为空");
        }
        MyFile file = Optional.ofNullable(fileService.getShareFile(code))
                .orElseThrow(() -> new FailException("分享的文件不存在或过期!"));
        return ResultUtil.success(file);
    }

    @PostMapping("/share/{id}")
    public ResultVO<String> shareFile(@PathVariable Integer id) {
        String shareCode = fileService.share(id);
        return shareCode != null ? ResultUtil.success(shareCode) : ResultUtil.errorDo();
    }

}

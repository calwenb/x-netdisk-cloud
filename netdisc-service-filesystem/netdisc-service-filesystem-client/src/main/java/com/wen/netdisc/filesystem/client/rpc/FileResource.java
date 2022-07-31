package com.wen.netdisc.filesystem.client.rpc;

import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.annotation.PrcVerify;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@PrcVerify
public interface FileResource {

    @PostMapping("/uploadFile")
    ResultVO<Boolean> uploadFileComm(@RequestParam("file") MultipartFile file, @RequestParam("path") String path);

    @PostMapping("/uploadHead")
    ResultVO<String> uploadHead(@RequestParam("file") MultipartFile file);

    @PostMapping("/initStore")
    ResultVO<Boolean> initStore(@RequestParam("uid") Integer uid);

    @GetMapping("/downloadComm")
    ResultVO<Object> downloadComm(@RequestParam("path") String path);
}

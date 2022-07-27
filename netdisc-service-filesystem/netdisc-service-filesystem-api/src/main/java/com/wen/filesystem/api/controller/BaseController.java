package com.wen.filesystem.api.controller;

import com.wen.filesystem.api.servcie.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 同时给Controller注入多个Service
 * @author Mr.文
 */
@Repository
public class BaseController {
    @Resource
    UserService userService;
    @Resource
    FileService fileService;
    @Resource
    FileFolderService fileFolderService;
    @Resource
    FileStoreService fileStoreService;
    @Resource
    TokenService tokenService;

}

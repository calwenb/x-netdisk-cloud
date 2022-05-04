package com.wen.controller;

import com.wen.servcie.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 同时给Controller注入多个Service
 * @author Mr.文
 */
@Repository
public class BaseController {
    @Autowired
    UserService userService;
    @Autowired
    FileService fileService;
    @Autowired
    FileFolderService fileFolderService;
    @Autowired
    FileStoreService fileStoreService;
    @Autowired
    TokenService tokenService;

}

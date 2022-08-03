package com.wen.netdisc.filesystem.api.controller.api;


import com.wen.netdisc.filesystem.api.servcie.FileService;
import com.wen.netdisc.filesystem.api.servcie.FolderService;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * 同时给Controller注入多个Service
 *
 * @author calwen
 */
@Repository
public abstract class BaseController {
    @Resource
    FileService fileService;
    @Resource
    FolderService folderService;
    @Resource
    StoreService storeService;




}

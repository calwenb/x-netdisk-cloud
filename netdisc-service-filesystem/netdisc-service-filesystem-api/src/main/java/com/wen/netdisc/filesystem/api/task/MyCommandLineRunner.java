package com.wen.netdisc.filesystem.api.task;

import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.FolderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final String CHUNK_PATH = "temp/chunk";
    private static final String BACKUP_PATH = "temp/backup";

    private static final String STORE_PATH = "store";
    private static final String HEAD_PATH = "head";

    @Override
    public void run(String... args) {
        initFolder();
    }

    private void initFolder() {
        String rootPath = FileUtil.getRootPath();
        if (FolderUtil.autoFolder(rootPath + STORE_PATH)) {
            log.info("项目 仓库 文件夹 初始化完成");
        }
        if (FolderUtil.autoFolder(rootPath + HEAD_PATH)) {
            log.info("项目 头像 文件夹 初始化完成");
        }
        if (FolderUtil.autoFolder(rootPath + CHUNK_PATH)
                && FolderUtil.autoFolder(rootPath + BACKUP_PATH)) {
            log.info("项目 临时文件 文件夹 初始化完成");
        }
    }
}
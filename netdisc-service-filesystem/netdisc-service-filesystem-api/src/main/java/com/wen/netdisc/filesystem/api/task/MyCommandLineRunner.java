package com.wen.netdisc.filesystem.api.task;

import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.FolderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyCommandLineRunner implements CommandLineRunner {
    private static final String CHUNK_PATH = "D:/project-support/x-netdisk/temp/chunk";
    private static final String BACKUP_PATH = "D:/project-support/x-netdisk/temp/backup";

    @Override
    public void run(String... args) {
        initFolder();
    }

    private void initFolder() {
        String rootPath = FileUtil.getRootPath();
        if (FolderUtil.autoFolder(rootPath + "store")) {
            log.info("项目 仓库 文件夹 初始化完成");
        }
        if (FolderUtil.autoFolder(rootPath + "head")) {
            log.info("项目 头像 文件夹 初始化完成");
        }
        if (FolderUtil.autoFolder(CHUNK_PATH) && FolderUtil.autoFolder(BACKUP_PATH)) {
            log.info("项目 临时文件 文件夹 初始化完成");
        }
    }
}
package com.wen.netdisc.filesystem.api.task;

import com.wen.commutil.util.LoggerUtil;
import com.wen.netdisc.filesystem.api.servcie.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * ClearFileTask 任务类
 * 1.清理无效的文件，以便减少磁盘资源的占用
 * 2.清理数据库中无效的文件信息
 *
 * @author calwen
 */
@Component
public class ClearFileTask {
    @Resource
    FileService fileService;

    @Scheduled(cron = "0 0 0 */1 * ?")
//    @Scheduled(cron = "0/50 * * * * ? ")
    public void clearBadFile() {
        LoggerUtil.info("开始清理 无效文件 ==>\n", ClearFileTask.class);
        Map<String, Integer> map = fileService.clearBadFile();
        map.forEach((k, v) -> {
            LoggerUtil.info(k + " : " + v, ClearFileTask.class);
        });
    }
}

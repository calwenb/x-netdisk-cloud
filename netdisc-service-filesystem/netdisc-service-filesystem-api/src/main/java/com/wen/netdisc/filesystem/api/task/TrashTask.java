package com.wen.netdisc.filesystem.api.task;

import com.wen.netdisc.common.util.LoggerUtil;
import com.wen.netdisc.filesystem.api.servcie.TrashService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TrashTask {
    @Resource
    TrashService trashService;

    @Scheduled(cron = "0 0 0 */2 * ?")
    //@Scheduled(cron = "30 * * * * ?")
    public void takeOutTrash() {
        System.out.println("开始倒垃圾==>");
        LoggerUtil.info("开始倒垃圾==>", TrashTask.class);
        int i = trashService.takeOutTrash();
        LoggerUtil.info("垃圾倒完了==> 总共清理垃圾数：" + i, TrashTask.class);
    }
}

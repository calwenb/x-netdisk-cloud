//package com.wen.netdisc.filesystem.api.task;
//
//import com.wen.xwebalbum.servcie.FileService;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * ClearFileTask 任务类
// * 清理无效的文件，以便减少磁盘资源的占用
// * @author calwen
// */
//@Component
//public class ClearFileTask {
//    @Resource
//    FileService fileService;
//
//    //@Scheduled(cron = "0 0 0 */1 * ?")
//    //@Scheduled(cron = "0/15 * * * * ? ")
//    public void clearBadFile() {
//        List<String> rs = fileService.clearBadFile();
//        System.out.println("发现文件共:" + rs.size());
//        System.out.println(rs);
//    }
//}

package com.wen.netdisc.filesystem.api.servcie.impl;


import com.wen.netdisc.common.util.ThreadPoolUtil;
import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.filesystem.api.mapper.MyFileMapper;
import com.wen.netdisc.filesystem.api.mapper.StoreMapper;
import com.wen.netdisc.filesystem.api.servcie.RedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class RedisServiceImpl implements RedisService {
    //    @Resource
//    UserMapper userMapper;
    @Resource
    MyFileMapper fileMapper;
    @Resource
    StoreMapper fileStoreMapper;
    @Resource
    com.wen.netdisc.filesystem.api.mapper.FolderMapper FolderMapper;


    @Override
    public void redisWarmUp() {
        System.out.println("开始预热Redis缓存");
        CountDownLatch countDown = new CountDownLatch(3);
        try {
            ThreadPoolExecutor threadPool = ThreadPoolUtil.getThreadPool();
//            threadPool.execute(() -> {
//                // 用户预热
//                for (User user : userMapper.queryUsers()) {
//                    userMapper.getUserById(user.getId());
//                    fileStoreMapper.queryStoreByUid(user.getId());
//                }
//                countDown.countDown();
//            });
            threadPool.execute(() -> {
                // 文件预热
                for (MyFile file : fileMapper.queryAllFiles()) {
                    fileMapper.queryFileById(file.getMyFileId());
                }
                countDown.countDown();
            });
            threadPool.execute(() -> {
                //文件夹预热
                for (FileFolder folders : FolderMapper.queryFolders()) {
                    FolderMapper.queryFolderById(folders.getFileFolderId());
                }
                countDown.countDown();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            countDown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}

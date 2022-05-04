package com.wen.servcie.impl;

import com.wen.common.utils.ThreadPoolUtil;
import com.wen.mapper.FileFolderMapper;
import com.wen.mapper.FileStoreMapper;
import com.wen.mapper.MyFileMapper;
import com.wen.mapper.UserMapper;
import com.wen.pojo.FileFolder;
import com.wen.pojo.MyFile;
import com.wen.pojo.User;
import com.wen.servcie.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MyFileMapper fileMapper;
    @Autowired
    FileStoreMapper fileStoreMapper;
    @Autowired
    FileFolderMapper fileFolderMapper;
    @Autowired

    RedisTemplate redisTemplate;

    @Override
    public void redisWarmUp() {
        System.out.println("开始预热Redis缓存");
        CountDownLatch countDown = new CountDownLatch(3);
        try {
            ThreadPoolExecutor threadPool = ThreadPoolUtil.getThreadPool();
            threadPool.execute(() -> {
                // 用户预热
                for (User user : userMapper.queryUsers()) {
                    userMapper.getUserById(user.getId());
                    fileStoreMapper.queryFileStoreByUserId(user.getId());
                }
                countDown.countDown();
            });
            threadPool.execute(() -> {
                // 文件预热
                for (MyFile file : fileMapper.queryAllFiles()) {
                    fileMapper.queryFileById(file.getMyFileId());
                }
                countDown.countDown();
            });
            threadPool.execute(() -> {
                //文件夹预热
                for (FileFolder folders : fileFolderMapper.queryFolders()) {
                    fileFolderMapper.queryFileFolderById(folders.getFileFolderId());
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

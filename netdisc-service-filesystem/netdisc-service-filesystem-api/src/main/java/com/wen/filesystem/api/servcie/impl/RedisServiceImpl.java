package com.wen.filesystem.api.servcie.impl;

import com.wen.common.util.ThreadPoolUtil;
import com.wen.filesystem.mapper.FileFolderMapper;
import com.wen.filesystem.mapper.FileStoreMapper;
import com.wen.filesystem.mapper.MyFileMapper;
import com.wen.filesystem.mapper.UserMapper;
import com.wen.filesystem.pojo.FileFolder;
import com.wen.filesystem.pojo.MyFile;
import com.wen.filesystem.pojo.User;
import com.wen.filesystem.servcie.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    UserMapper userMapper;
    @Resource
    MyFileMapper fileMapper;
    @Resource
    FileStoreMapper fileStoreMapper;
    @Resource
    FileFolderMapper fileFolderMapper;
    @Resource

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

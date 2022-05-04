package com.wen.servcie.impl;

import com.wen.common.utils.FileUtil;
import com.wen.mapper.FileStoreMapper;
import com.wen.pojo.FileStore;
import com.wen.servcie.FileStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Transactional(rollbackFor = Exception.class)
@Service
public class FileStoreServiceImpl implements FileStoreService {
    @Autowired
    FileStoreMapper fileStoreMapper;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean initStore(int userId) {
        FileStore fileStore = new FileStore(-1, userId, 0, FileUtil.STORE_MAX_SIZE);
        if (fileStoreMapper.addFileStore(fileStore) == 0) {
            return false;
        }
        String path = FileUtil.STORE_ROOT_PATH + fileStore.getFileStoreId() + "/";
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            //回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
        return true;
    }

    @Override
    public FileStore queryStoreByUserId(int userId) {
        return fileStoreMapper.queryFileStoreByUserId(userId);
    }

}

package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.common.util.FileUtil;
import com.wen.filesystem.mapper.FileStoreMapper;
import com.wen.filesystem.pojo.FileStore;
import com.wen.filesystem.servcie.FileStoreService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Transactional(rollbackFor = Exception.class)
@Service
public class FileStoreServiceImpl implements FileStoreService {
    @Resource
    FileStoreMapper fileStoreMapper;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public int initStore(int userId) {
        FileStore fileStore = new FileStore(-1, userId, 0, FileUtil.STORE_MAX_SIZE);
        if (fileStoreMapper.addFileStore(fileStore) == 0) {
            return -1;
        }
        String path = FileUtil.STORE_ROOT_PATH + fileStore.getFileStoreId() + "/";
        try {
            Files.createDirectories(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            //回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return -1;
        }
        return fileStore.getFileStoreId();
    }

    @Override
    public FileStore queryStoreByUserId(int userId) {
        return fileStoreMapper.queryFileStoreByUserId(userId);
    }

}

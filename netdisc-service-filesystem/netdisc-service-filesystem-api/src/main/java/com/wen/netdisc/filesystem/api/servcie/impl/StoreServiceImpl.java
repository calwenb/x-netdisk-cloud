package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.baseorm.core.mapper.BaseMapper;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.filesystem.api.mapper.StoreMapper;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
public class StoreServiceImpl implements StoreService {
    @Resource
    BaseMapper baseMapper;
    @Resource
    StoreMapper storeMapper;


    @Override
    public boolean initStore(int userId) {
        FileStore fileStore = new FileStore(-1, userId, 0, FileUtil.STORE_MAX_SIZE);
        if (storeMapper.addFileStore(fileStore) == 0) {
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
    public FileStore queryStoreByUid(int userId) {
        return storeMapper.queryStoreByUid(userId);
    }

    /**
     * @param fileStore
     * @return
     */
    @Override
    public boolean updateStore(FileStore fileStore) {
        return baseMapper.replaceTarget(fileStore) > 0;
    }

    @Override
    public List<FileStore> queryUserStore() {
        return storeMapper.queryUserStore();
    }

    @Override
    public boolean delStore(Integer sid) {
        return baseMapper.deleteTargetById(FileStore.class, sid) > 0;
    }


}

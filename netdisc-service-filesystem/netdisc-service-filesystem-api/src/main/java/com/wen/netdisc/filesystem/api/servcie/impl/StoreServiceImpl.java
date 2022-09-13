package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.filesystem.api.mapper.StoreMapper;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.releasedao.core.mapper.BaseMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
@Service
public class StoreServiceImpl implements StoreService {
    @Resource
    BaseMapper baseMapper;
    @Resource
    StoreMapper storeMapper;


    @Override
    public boolean initStore(int userId) {
        FileStore store = new FileStore();
        store.setUserId(userId);
        store.setCurrentSize(0L);
        store.setMaxSize(FileUtil.STORE_MAX_SIZE);
        if (storeMapper.addFileStore(store) == 0) {
            return false;
        }
        String path = FileUtil.STORE_ROOT_PATH + store.getFileStoreId() + "/";
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
        FileStore store = storeMapper.queryStoreByUid(userId);
        return Optional.ofNullable(store).orElseThrow(() -> new FailException("获取仓库数据失败"));
    }

    /**
     * @param fileStore
     * @return
     */
    @Override
    public boolean updateStore(FileStore fileStore) {
        return baseMapper.save(fileStore);
    }

    @Override
    public List<FileStore> queryUserStore() {
        return storeMapper.queryUserStore();
    }

    @Override
    public boolean delStore(Integer sid) {
        return baseMapper.deleteById(FileStore.class, sid);
    }


}

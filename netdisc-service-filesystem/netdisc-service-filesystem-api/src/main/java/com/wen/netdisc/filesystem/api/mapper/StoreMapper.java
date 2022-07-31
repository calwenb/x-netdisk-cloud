package com.wen.netdisc.filesystem.api.mapper;

import com.wen.netdisc.common.pojo.FileStore;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileStoreMapper类 持久层
 *
 * @author calwen
 */
@Mapper
@Repository
@CacheConfig(cacheNames = "store")
public interface StoreMapper {
    List<FileStore> queryFileStores();

    FileStore queryStoreById(int fileStoreId);

    int addFileStore(FileStore fileStore);

    @Cacheable(key = "'uid:'+#p0", unless = "#result == null")
    FileStore queryStoreByUid(int userId);

    List<FileStore> queryUserStore();

}

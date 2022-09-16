package com.wen.netdisc.filesystem.api.mapper;

import com.wen.netdisc.common.pojo.FileStore;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileStoreMapper类 持久层
 *
 * @author calwen
 */
@Mapper
@Repository
public interface StoreMapper {


    int addFileStore(FileStore fileStore);

    FileStore queryStoreByUid(int userId);

    List<FileStore> queryUserStore();

}

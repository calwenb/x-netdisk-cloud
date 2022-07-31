package com.wen.netdisc.filesystem.api.servcie;

import com.wen.netdisc.common.pojo.FileStore;

import java.util.List;

/**
 * FileStoreService业务类
 * 对仓库初始化、查询业务，并操作服务器I/O
 *
 * @author calwen
 */
public interface StoreService {

    boolean initStore(int userId);

    FileStore queryStoreByUid(int userId);

    boolean updateStore(FileStore fileStore);

    List<FileStore> queryUserStore();

    boolean delStore(Integer sid);
}

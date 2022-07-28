package com.wen.filesystem.api.servcie;

import com.wen.filesystem.pojo.FileStore;

/**
 * FileStoreService业务类
 * 对仓库初始化、查询业务，并操作服务器I/O
 *
 * @author Mr.文
 */
public interface FileStoreService {

    int initStore(int userId);

    FileStore queryStoreByUserId(int userId);
}

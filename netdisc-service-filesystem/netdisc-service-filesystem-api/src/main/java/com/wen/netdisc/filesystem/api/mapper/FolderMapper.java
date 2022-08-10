package com.wen.netdisc.filesystem.api.mapper;

import com.wen.netdisc.common.pojo.FileFolder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FolderMapper类 持久层
 *
 * @author calwen
 */
@Mapper
@Repository
@CacheConfig(cacheNames = "folder")
public interface FolderMapper {
    List<FileFolder> queryFolders();

    int addFileFolder(FileFolder fileFolder);

    @Cacheable(key = "'fdid:'+#p0", unless = "#result == null")
    FileFolder queryFolderById(int fileFolderId);

    List<FileFolder> queryFoldersByPId(int storeId, int parentFolderId);

    @CacheEvict(key = "'fdid:'+#p0")
    int delFolderById(int fileFolderId);

    @CacheEvict(key = "'fdid:'+#p0.fileFolderId")
    int updateFolderById(FileFolder folder);
}

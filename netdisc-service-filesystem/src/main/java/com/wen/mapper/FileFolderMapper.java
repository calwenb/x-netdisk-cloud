package com.wen.mapper;

import com.wen.pojo.FileFolder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileFolderMapper类 持久层
 *
 * @author Mr.文
 */
@Mapper
@Repository
@CacheConfig(cacheNames = "folder")
public interface FileFolderMapper {
    List<FileFolder> queryFolders();

    int addFileFolder(FileFolder fileFolder);

    @Cacheable(key = "'fdid:'+#p0")
    FileFolder queryFileFolderById(int fileFolderId);

    List<FileFolder> queryFoldersByPId(int storeId, int parentFolderId);

    @CacheEvict(key = "'fdid:'+#p0")
    int delFolderById(int fileFolderId);

    @CacheEvict(key = "'fdid:'+#p0.fileFolderId")
    int updateFolderById(FileFolder folder);
}

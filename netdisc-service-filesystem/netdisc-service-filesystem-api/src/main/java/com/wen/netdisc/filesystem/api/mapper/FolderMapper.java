package com.wen.netdisc.filesystem.api.mapper;

import com.wen.netdisc.common.pojo.FileFolder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FolderMapper类 持久层
 *
 * @author calwen
 */
@Mapper
@Repository
public interface FolderMapper {
    List<FileFolder> queryFolders();

    int addFileFolder(FileFolder fileFolder);

    FileFolder queryFolderById(int fileFolderId);

    List<FileFolder> queryFoldersByPId(int storeId, int parentFolderId);

    int delFolderById(int fileFolderId);

    int updateFolderById(FileFolder folder);
}

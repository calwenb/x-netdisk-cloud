package com.wen.servcie;

import com.wen.pojo.FileFolder;

import java.io.IOException;
import java.util.List;

/**
 * FileFolderService业务类
 * 对文件夹进行增删改查，并操作服务器I/O
 * @author Mr.文
 */
public interface FileFolderService {
    boolean addFileFolder(FileFolder fileFolder) throws IOException;

    List<FileFolder> queryFoldersByPId(int storeId,int pId);

    boolean delFolder(int folderId) throws IOException;

    /**
     * 重命名文件夹业务
     * 因为文件夹之前依据根据Id
     * 故不用更改本地的文件夹名
     *
     * @param folderId
     * @param newName
     * @return
     */
    boolean updateFolderName(int folderId, String newName);
}

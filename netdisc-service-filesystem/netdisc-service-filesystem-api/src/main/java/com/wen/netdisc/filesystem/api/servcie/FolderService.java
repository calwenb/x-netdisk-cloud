package com.wen.netdisc.filesystem.api.servcie;

import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.TreeNode;
import com.wen.netdisc.filesystem.api.dto.FolderSaveDto;

import java.util.List;

/**
 * FileFolderService业务类
 * 对文件夹进行增删改查，并操作服务器I/O
 *
 * @author calwen
 */
public interface FolderService {
    boolean addFileFolder(FolderSaveDto folderSaveDto);

    List<FileFolder> queryFoldersByPId(int storeId, int pId);

    boolean delFolder(int folderId);

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

    TreeNode getFolderTree(Integer uid);
}

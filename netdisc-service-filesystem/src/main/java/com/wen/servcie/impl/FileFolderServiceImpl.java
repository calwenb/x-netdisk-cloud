package com.wen.servcie.impl;

import com.wen.common.utils.FileUtil;
import com.wen.mapper.FileFolderMapper;
import com.wen.mapper.FileStoreMapper;
import com.wen.mapper.MyFileMapper;
import com.wen.pojo.FileFolder;
import com.wen.pojo.MyFile;
import com.wen.servcie.FileFolderService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

@Transactional(rollbackFor = Exception.class)
@Service
public class FileFolderServiceImpl implements FileFolderService {
    @Autowired
    FileFolderMapper fileFolderMapper;
    @Autowired
    FileStoreMapper fileStoreMapper;
    @Autowired
    MyFileMapper myFileMapper;

    @Override
    public boolean addFileFolder(FileFolder fileFolder) {
        //根路径+仓库Id
        StringBuffer path = new StringBuffer(FileUtil.STORE_ROOT_PATH + fileFolder.getFileStoreId());
        int parentFolderId = fileFolder.getParentFolderId();
        if (parentFolderId == FileUtil.STORE_ROOT_ID) {
            path.append("/" + fileFolder.getFileFolderName());
        } else {
            Stack<String> stack = new Stack<>();
            FileFolder pff = fileFolder;
            while (true) {
                int pid = pff.getParentFolderId();
                String pName = pff.getFileFolderName();
                //往上面查父文件夹ID
                stack.push(pName);
                if (pid == 0) {
                    break;
                }
                pff = fileFolderMapper.queryFileFolderById(pid);
            }
            while (!stack.isEmpty()) {
                path.append("/" + stack.pop());
            }
        }
        try {
            Path folderPath = Paths.get(String.valueOf(path));
            if (!Files.exists(folderPath)) {
                Files.createDirectory(folderPath);
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        fileFolder.setFileFolderPath(String.valueOf(path));
        return fileFolderMapper.addFileFolder(fileFolder) > 0;
    }

    @Override
    public List<FileFolder> queryFoldersByPId(int storeId, int pId) {
        return fileFolderMapper.queryFoldersByPId(storeId, pId);
    }

    @Override
    public boolean delFolder(int folderId) {
        FileFolder folder = fileFolderMapper.queryFileFolderById(folderId);
        File delFolder = new File(folder.getFileFolderPath());
        try {
            FileUtils.deleteDirectory(delFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return fileFolderMapper.delFolderById(folderId) > 0;
    }

    @Override
    public boolean updateFolderName(int folderId, String newName) {
        FileFolder folder = fileFolderMapper.queryFileFolderById(folderId);
        folder.setFileFolderName(newName);
        String oldPath = folder.getFileFolderPath();
        String path = oldPath.substring(0, oldPath.lastIndexOf('/') + 1) + newName;
        if (Files.exists(Paths.get(oldPath)) && !Files.exists(Paths.get(path))) {
            //修改文件夹名 成功更新数据库文件的路径
            if (new File(oldPath).renameTo(new File(path))) {
                int userId = fileStoreMapper.queryStoreById(folder.getFileStoreId()).getUserId();
                List<MyFile> fileList = myFileMapper.queryMyFiles(userId, folderId, 0, 9999);
                for (MyFile file : fileList) {
                    file.setMyFilePath(path + '/' + file.getMyFileName());
                    myFileMapper.updateByFileId(file);
                }
            }
        } else {
            return false;
        }
        folder.setFileFolderPath(path);
        return fileFolderMapper.updateFolderById(folder) > 0;
    }

}

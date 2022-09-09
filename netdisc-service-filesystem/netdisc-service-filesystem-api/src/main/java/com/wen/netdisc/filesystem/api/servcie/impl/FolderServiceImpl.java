package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.pojo.TreeNode;
import com.wen.netdisc.filesystem.api.dto.FolderSaveDto;
import com.wen.netdisc.filesystem.api.mapper.FolderMapper;
import com.wen.netdisc.filesystem.api.mapper.MyFileMapper;
import com.wen.netdisc.filesystem.api.mapper.StoreMapper;
import com.wen.netdisc.filesystem.api.servcie.FolderService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.FolderUtil;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import com.wen.releasedao.core.mapper.BaseMapper;
import com.wen.releasedao.core.wrapper.QueryWrapper;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Stack;

@Transactional(rollbackFor = Exception.class)
@Service
public class FolderServiceImpl implements FolderService {
    @Resource
    FolderMapper folderMapper;
    @Resource
    StoreMapper storeMapper;
    @Resource
    MyFileMapper fileMapper;
    @Resource
    BaseMapper baseMapper;

    @Override
    public boolean addFileFolder(FolderSaveDto dto) {
        Integer uid = UserUtil.getUid();
        FileStore store = storeMapper.queryStoreByUid(uid);
        FileFolder folder = new FileFolder();
        folder.setFileFolderId(dto.getParentId());
        folder.setFileFolderName(dto.getName());
        folder.setFileStoreId(store.getFileStoreId());

        //根路径+仓库Id
        StringBuilder path = new StringBuilder(FileUtil.STORE_ROOT_PATH + folder.getFileStoreId());
        int parentId = folder.getParentFolderId();
        if (parentId == FileUtil.STORE_ROOT_ID) {
            path.append("/").append(folder.getFileFolderName());
        } else {
            Stack<String> stack = new Stack<>();
            FileFolder pff = folder;
            while (true) {
                int pid = pff.getParentFolderId();
                String pName = pff.getFileFolderName();
                //往上面查父文件夹ID
                stack.push(pName);
                if (pid == 0) {
                    break;
                }
                pff = folderMapper.queryFolderById(pid);
            }
            while (!stack.isEmpty()) {
                path.append("/").append(stack.pop());
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
            throw new FailException("增加文件夹失败");
        }
        folder.setFileFolderPath(String.valueOf(path));
        return folderMapper.addFileFolder(folder) > 0;
    }

    @Override
    public List<FileFolder> queryFoldersByPId(int storeId, int pId) {
        return folderMapper.queryFoldersByPId(storeId, pId);
    }

    @Override
    public boolean delFolder(int id) {
        FileFolder folder = folderMapper.queryFolderById(id);
        if (folder == null) {
            throw new FailException("文件不存在");
        }
        del(storeMapper.queryStoreByUid(UserUtil.getUid()).getFileStoreId(), id);
        File delFolder = new File(folder.getFileFolderPath());
        try {
            FileUtils.deleteDirectory(delFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 数据库中删除文件夹、文件
     * dfs
     */
    private void del(Integer sid, Integer id) {
        folderMapper.delFolderById(id);
        baseMapper.delete(MyFile.class, new QueryWrapper().eq("parent_folder_id", id));
        List<FileFolder> childs = folderMapper.queryFoldersByPId(sid, id);
        if (childs == null || childs.isEmpty()) {
            return;
        }
        for (FileFolder child : childs) {
            del(sid, child.getFileFolderId());
        }

    }

    @Override
    public boolean updateFolderName(int folderId, String newName) {
        FileFolder folder = folderMapper.queryFolderById(folderId);
        folder.setFileFolderName(newName);
        String oldPath = folder.getFileFolderPath();
        String path = oldPath.substring(0, oldPath.lastIndexOf('/') + 1) + newName;
        if (Files.exists(Paths.get(oldPath)) && !Files.exists(Paths.get(path))) {
            //修改文件夹名 成功更新数据库文件的路径
            if (new File(oldPath).renameTo(new File(path))) {
                FileStore store = baseMapper.getById(FileStore.class, folder.getFileStoreId());
                if (store == null) {
                    throw new FailException("获取仓库信息失败");
                }
                int userId = store.getUserId();
                List<MyFile> fileList = fileMapper.queryMyFiles(userId, folderId, 0, 9999);
                for (MyFile file : fileList) {
                    file.setMyFilePath(path + '/' + file.getMyFileName());
                    fileMapper.update(file);
                }
            }
        } else {
            return false;
        }
        folder.setFileFolderPath(path);
        return folderMapper.updateFolderById(folder) > 0;
    }


    /**
     * @return
     */
    @Override
    public TreeNode getFolderTree(Integer uid) {
        FileStore store = storeMapper.queryStoreByUid(uid);
        int storeId = store.getFileStoreId();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.select("file_folder_id , file_folder_name , parent_folder_id");
        wrapper.eq("file_store_id", storeId);
        List<FileFolder> list = baseMapper.getList(FileFolder.class);
        return FolderUtil.getTree(list);
    }

}

package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.filesystem.api.mapper.FolderMapper;
import com.wen.netdisc.filesystem.api.mapper.MyFileMapper;
import com.wen.netdisc.filesystem.api.mapper.StoreMapper;
import com.wen.netdisc.filesystem.api.servcie.FileService;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import com.wen.netdisc.filesystem.api.servcie.TrashService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.FolderUtil;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
public class FileServiceImpl implements FileService {
    @Resource
    MyFileMapper fileMapper;
    @Resource
    StoreMapper storeMapper;
    @Resource
    FolderMapper folderMapper;
    @Resource
    StoreService storeService;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    TrashService trashService;


    @Override
    public boolean uploadFile(MultipartFile file, int userId, Integer faFolderId) {
        try {
            FileStore fileStore = storeMapper.queryStoreByUid(userId);
            if (fileStore == null) {
                throw new FailException("获取用户仓库失败");
            }
            int fileStoreId = fileStore.getFileStoreId();
            // 获取文件名
            String fileName = file.getOriginalFilename();
            long size = file.getSize();
            // 获取文件的后缀名
            String suffixName;
            if (fileName.lastIndexOf(".") == -1) {
                //文件没有后缀
                suffixName = "null";
            } else {
                suffixName = fileName.substring(fileName.lastIndexOf("."));
            }
            String filePath;
            //Pid=0，保存到根文件夹,否则获取父文件夹的路径
            if (faFolderId == 0) {
                filePath = FileUtil.STORE_ROOT_PATH + fileStoreId + "/";
            } else {
                FileFolder fileFolder = folderMapper.queryFolderById(faFolderId);
                String fileFolderPath = fileFolder.getFileFolderPath();
                filePath = fileFolderPath + "/";
            }

            FolderUtil.autoFolder(filePath);

            //如果有相同的文件名 加后缀
            File[] broFiles = new File(filePath).listFiles();
            assert broFiles != null;
            for (File broFile : broFiles) {
                if (broFile.getName().equals(fileName)) {
                    String pureName = broFile.getName().substring(0, fileName.lastIndexOf(suffixName));
                    int len = pureName.length();
                    if (pureName.charAt(len - 2) == '_') {
                        int count = Integer.parseInt(pureName.substring(len - 1)) + 1;
                        fileName = pureName.substring(0, pureName.lastIndexOf('_') + 1) + count + suffixName;
                    } else {
                        fileName = pureName + "_1" + suffixName;
                    }
                }
            }
            // 设置文件存储路径
            String path = filePath + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                // 新建文件夹
                dest.getParentFile().mkdirs();
            }
            String type = FileUtil.getFileType(suffixName);
            MyFile myFile = new MyFile(-1, fileName, fileStoreId, path, 0, new Date(), faFolderId, size, type);
            Integer i = fileMapper.addFile(myFile);
            if (i > 0) {
                file.transferTo(dest);
                //
             /*   FileStore store = storeService.queryStoreByUserId(userId);
                store.setCurrentSize(store.getCurrentSize() + file.getSize());
                storeService.updateStore(store);*/
                System.out.println("用户ID：" + userId + " 上传成功。服务器保存地址：" + path);
                return true;
            }
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    @Override
    public List<MyFile> queryMyFiles(int userId, int parentFolderId, int pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        /**
         * 不指定页数，即不分页
         */
        if (pageNum == -1) {
            startRow = 0;
            showRow = Integer.MAX_VALUE;
        }
        List<MyFile> myFiles = fileMapper.queryMyFiles(userId, parentFolderId, startRow, showRow);
        return myFiles;
    }


    @Override
    public List<MyFile> queryFilesByUid(int userId, int pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        /**
         * 不指定页数，即不分页
         */
        if (pageNum == -1) {
            startRow = 0;
            showRow = Integer.MAX_VALUE;
        }
        return fileMapper.queryFilesByUid(userId, startRow, showRow);
    }

    @Override
    public List<MyFile> queryFilesByType(Integer uid, String type, int pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        /**
         * 不指定页数，即不分页
         */
        if (pageNum == -1) {
            startRow = 0;
            showRow = Integer.MAX_VALUE;
        }
        return fileMapper.queryFilesByType(uid, FileUtil.getTypeChinese(type), startRow, showRow);
    }

    @Override
    public boolean updateData(MultipartFile file, Integer id) {
        MyFile myFile = fileMapper.queryFileById(id);
        String path = myFile.getMyFilePath();
        try {
            Files.write(Paths.get(path), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public List<Object> getFileAndFolder(Integer parentFid) {
        List<Object> list = new ArrayList<>();
        Integer uid = UserUtil.getUid();
        FileStore store = storeService.queryStoreByUid(uid);
        List<MyFile> files = queryMyFiles(uid, parentFid, -1);
        List<FileFolder> folders = folderMapper.queryFoldersByPId(store.getFileStoreId(), parentFid);
        list.addAll(folders);
        list.addAll(files);
        return list;
    }

    public List<Map<String, String>> queryFilesByUid(int userId, int pageNum, boolean preview) throws IOException {
        if (preview) {
            int showRow = FileUtil.FILE_SHOW_ROW;
            int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
            /**
             * 不指定页数，即不分页
             */
            if (pageNum == -1) {
                startRow = 0;
                showRow = Integer.MAX_VALUE;
            }
            List<MyFile> files = fileMapper.queryFileByUidOrdDate(userId, startRow, showRow);
/*            if (files != null && files.size() != 0) {
                return FileUtil.previewImage(files);
            }*/
        }
        return null;
    }


    @Override
    public boolean deleteByMyFileId(int fileId) {
        MyFile file = fileMapper.queryFileById(fileId);
        if (fileMapper.deleteByMyFileId(fileId) > 0) {
            int uid = UserUtil.getUid();
            FileStore store = storeService.queryStoreByUid(uid);
            return trashService.addTrash(file, uid) && storeService.updateStore(store);
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(int fileId, boolean preview) throws IOException {
        //从数据库查询文件信息 换取文件路径
        MyFile file = fileMapper.queryFileById(fileId);
        String filePath = file.getMyFilePath();
        return this.download(filePath);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadComm(String path) throws IOException {
        return this.download(path);
    }


    /**
     * 通用下载方法
     * 通过文件路径 获得本地文件
     * 自定义响应 将文件流放入响应体中
     */
    private ResponseEntity<InputStreamResource> download(String path) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Access-Contro1-A11ow-0rigin", "*");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        FileSystemResource downloadFile = new FileSystemResource(path);
        if (!downloadFile.exists()) {
            return ResponseEntity.ok().headers(headers).contentLength(0).contentType(MediaType.parseMediaType("application/octet-stream")).body(null);
        }
        InputStream inputStream = downloadFile.getInputStream();
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        ResponseEntity<InputStreamResource> body = ResponseEntity.ok()
                .headers(headers)
                .contentLength(downloadFile.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(downloadFile.getInputStream()));
        //设置响应头
        return body;
    }


    @Override
    public boolean updateFileName(int fileId, String newName) {
        MyFile file = fileMapper.queryFileById(fileId);
        String filePath = file.getMyFilePath();
        String path = filePath.substring(0, filePath.lastIndexOf('/') + 1);
        String newFilePath = path + newName;
        //本地仓库文件命名
        File fileIO = new File(file.getMyFilePath());
        if (fileIO.renameTo(new File(newFilePath))) {
            file.setMyFilePath(newFilePath);
            file.setMyFileName(newName);
            // 获取文件的后缀名
            String suffixName;
            if (newName.lastIndexOf(".") == -1) {
                //文件没有后缀
                suffixName = "null";
            } else {
                suffixName = newName.substring(newName.lastIndexOf("."));
            }
            //修改文件类型
            file.setType(FileUtil.getFileType(suffixName));
            return fileMapper.updateByFileId(file) > 0;
        } else {
            return false;
        }
    }


    @Override
    public String shareFile(int fileId) {
        Object scd = redisTemplate.opsForValue().get("share:fid:" + fileId);
        StringBuffer code = new StringBuffer();
        if (scd != null) {
            code.append(scd);
        } else {
            //文件生成码
            for (int i = 0; i < 5; i++) {
                code.append(new Random().nextInt(9));
            }
        }
        try {
            //SessionCallback事务
            SessionCallback<Object> callback = new SessionCallback<Object>() {
                @Override
                public Object execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    operations.opsForValue().set("share:code:" + code, fileId, 1, TimeUnit.DAYS);
                    operations.opsForValue().set("share:fid:" + fileId, code, 1, TimeUnit.DAYS);
                    return operations.exec();
                }
            };
            if (!"[true, true]".equals(Objects.requireNonNull(redisTemplate.execute(callback)).toString())) {
                System.out.println("redis事务失败");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return code.toString();
    }

    @Override
    public MyFile getShareFile(String shareCode) {
        Object fileId = redisTemplate.opsForValue().get("share:code:" + shareCode);
        if (fileId == null) {
            return null;
        }
        MyFile file = fileMapper.queryFileById((int) fileId);
        return file;
    }

    @Override
    public Map<String, Integer> clearBadFile() {
        HashMap<String, Integer> map = new HashMap<>();
        List<MyFile> files = fileMapper.queryAllFiles();
        int sum = 0;
        for (MyFile file : files) {
            String filePath = file.getMyFilePath();
            if (!new File(filePath).exists()) {
                fileMapper.deleteByMyFileId(file.getMyFileId());
                sum++;
            }
        }
        map.put("db文件数：", sum);
        File store = new File(FileUtil.STORE_ROOT_PATH);
        Set<String> validPath = files.stream().collect(Collectors.groupingBy((f) -> f.getMyFilePath().replace('/', '\\'))).keySet();
        delBadFile(validPath, store);

        sum = 0;
        List<FileFolder> folders = folderMapper.queryFolders();
        for (FileFolder folder : folders) {
            if (!Files.exists(Paths.get(folder.getFileFolderPath()))) {
                folderMapper.delFolderById(folder.getFileFolderId());
                sum++;
            }
        }
        map.put("db文件夹数：", sum);
        return map;
    }

    private void delBadFile(Set<String> validPath, File file) {
        File[] list = file.listFiles();
        if (list == null || list.length == 0) {
            return;
        }
        for (File f : list) {
            if (f.isDirectory()) {
                delBadFile(validPath, f);
            }
            if (f.isFile()) {
                if (!validPath.contains(f.getPath())) {
                    f.delete();
                }
            }

        }

    }

    @Override
    public boolean uploadFileComm(MultipartFile file, String path) {
        try {
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                // 新建文件夹
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }
}

package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.common.util.FileUtil;
import com.wen.filesystem.mapper.FileFolderMapper;
import com.wen.filesystem.mapper.FileStoreMapper;
import com.wen.filesystem.mapper.MyFileMapper;
import com.wen.filesystem.pojo.FileFolder;
import com.wen.filesystem.pojo.FileStore;
import com.wen.filesystem.pojo.MyFile;
import com.wen.filesystem.servcie.FileService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Transactional(rollbackFor = Exception.class)
@Service
public class FileServiceImpl implements FileService {
    @Resource
    MyFileMapper myFileMapper;
    @Resource
    FileStoreMapper fileStoreMapper;
    @Resource
    FileFolderMapper fileFolderMapper;
    @Resource
    RedisTemplate redisTemplate;


    @Override
    public boolean uploadFile(MultipartFile file, int userId, String fatherFileFolderId) {
        try {

            FileStore fileStore = fileStoreMapper.queryFileStoreByUserId(userId);
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
            if ("0".equals(fatherFileFolderId)) {
                filePath = FileUtil.STORE_ROOT_PATH + fileStoreId + "/";
            } else {
                FileFolder fileFolder = fileFolderMapper.queryFileFolderById(Integer.parseInt(fatherFileFolderId));
                String fileFolderPath = fileFolder.getFileFolderPath();
                filePath = fileFolderPath + "/";
            }

            //如果有相同的文件名 加后缀
            File[] broFiles = new File(filePath).listFiles();
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
            // 文件写入
            file.transferTo(dest);
            System.out.println("用户ID：" + userId + " 上传成功。服务器保存地址：" + path);
            String type = FileUtil.getFileType(suffixName);
            MyFile myFile = new MyFile(-1, fileName, fileStoreId, path, 0, new Date(), Integer.parseInt(fatherFileFolderId), size, type);
            myFileMapper.addFile(myFile);
            return true;
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
        List<MyFile> myFiles = myFileMapper.queryMyFiles(userId, parentFolderId, startRow, showRow);
        return myFiles;
    }

    @Override
    public List<MyFile> queryFilesByType(int userId, String type, int pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        /**
         * 不指定页数，即不分页
         */
        if (pageNum == -1) {
            startRow = 0;
            showRow = Integer.MAX_VALUE;
        }
        List<MyFile> myFiles = myFileMapper.queryFilesByType(userId, FileUtil.getTypeChinese(type), startRow, showRow);
        return myFiles;
    }

    @Override
    public boolean deleteByMyFileId(int fileId) {
        MyFile file = myFileMapper.queryFileById(fileId);
        File delFile = new File(file.getMyFilePath());
        boolean isDelete = false;
        if (delFile.isFile() && delFile.exists()) {
            isDelete = delFile.delete();
        }
        return myFileMapper.deleteByMyFileId(fileId) > 0 && isDelete;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadByMyFileId(int fileId) throws IOException {
        //从数据库查询文件信息
        MyFile file = myFileMapper.queryFileById(fileId);
        String filePath = file.getMyFilePath();
        FileSystemResource downloadFile = new FileSystemResource(filePath);
        //设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", downloadFile.getFilename()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(downloadFile.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(downloadFile.getInputStream()));
    }

    @Override
    public boolean updateFileName(int fileId, String newName) {
        MyFile file = myFileMapper.queryFileById(fileId);
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
            return myFileMapper.updateByFileId(file) > 0;
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
        MyFile file = myFileMapper.queryFileById((int) fileId);
        return file;
    }

    @Override
    public List<String> clearBadFile() {
        ArrayList<String> list = new ArrayList<>();
        List<MyFile> fileList = myFileMapper.queryAllFiles();
        for (MyFile file : fileList) {
            String filePath = file.getMyFilePath();
            if (!new File(filePath).exists()) {
                myFileMapper.deleteByMyFileId(file.getMyFileId());
                list.add(filePath);
            }
        }
        return list;
    }
}

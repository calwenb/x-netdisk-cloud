package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.FileFolder;
import com.wen.netdisc.common.pojo.FileStore;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.util.NumberUtil;
import com.wen.netdisc.common.util.ThreadPoolUtil;
import com.wen.netdisc.common.vo.PageVO;
import com.wen.netdisc.filesystem.api.mapper.FolderMapper;
import com.wen.netdisc.filesystem.api.mapper.MyFileMapper;
import com.wen.netdisc.filesystem.api.mapper.StoreMapper;
import com.wen.netdisc.filesystem.api.servcie.FileService;
import com.wen.netdisc.filesystem.api.servcie.StoreService;
import com.wen.netdisc.filesystem.api.servcie.TrashService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.FolderUtil;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
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
    RedisTemplate<String, Object> redisTemplate;
    @Resource
    TrashService trashService;
    @Resource
    FileUtil fileUtil;

    @Deprecated
    @Override
    public boolean uploadFile(MultipartFile file, int userId, Integer faFolderId) {
        try {
            FileStore store = storeMapper.queryStoreByUid(userId);
            if (store == null) {
                throw new FailException("????????????????????????");
            }
            long size = file.getSize() / 1000;
            if (store.getCurrentSize() + size > store.getMaxSize()) {
                throw new FailException("????????????????????????????????????");
            }
            int storeId = store.getFileStoreId();
            // ???????????????
            String fileName = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new FailException("?????????????????????"));
            // ????????????????????????
            String suffixName;
            if (fileName.lastIndexOf(".") == -1) {
                //??????????????????
                suffixName = "null";
            } else {
                suffixName = fileName.substring(fileName.lastIndexOf("."));
            }
            String filePath;
            //Pid=0????????????????????????,?????????????????????????????????
            if (faFolderId == 0) {
                filePath = FileUtil.STORE_ROOT_PATH + storeId + "/";
            } else {
                FileFolder fileFolder = folderMapper.queryFolderById(faFolderId);
                String fileFolderPath = fileFolder.getFileFolderPath();
                filePath = fileFolderPath + "/";
            }

            FolderUtil.autoFolder(filePath);

            //??????????????????????????? ?????????
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
            // ????????????????????????
            String path = filePath + fileName;
            File dest = new File(path);
            // ????????????????????????
            if (!dest.getParentFile().exists()) {
                // ???????????????
                dest.getParentFile().mkdirs();
            }
            String type = FileUtil.getFileType(suffixName);
            MyFile myFile = new MyFile(-1, fileName, storeId, path, 0, new Date(), faFolderId, size, type);
            Integer i = fileMapper.add(myFile);
            if (i > 0) {
                file.transferTo(dest);
                store.setCurrentSize(store.getCurrentSize() + size);
                storeService.updateStore(store);
                log.info("??????ID???" + userId + " ???????????????????????????????????????" + path);
                log.info("????????????????????????" + store.getCurrentSize() + " KB");
                return true;
            }
            return false;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void giveUserFile(File file, int userId, Integer faFolderId) throws IOException {
        FileStore store = storeMapper.queryStoreByUid(userId);
        if (store == null) {
            throw new FailException("????????????????????????");
        }
        long size = file.length() / 1000;
        Long currentSize = store.getCurrentSize();
        Long maxSize = store.getMaxSize();
        if (currentSize + size > maxSize) {
            String s = String.format("???????????????%s??????????????????%s??????????????????%s", maxSize, currentSize, size);
            log.warn(s);
            throw new FailException("????????????????????????????????????");
        }
        int storeId = store.getFileStoreId();
        // ???????????????
        String fileName = Optional.of(file.getName())
                .orElseThrow(() -> new FailException("?????????????????????"));
        // ????????????????????????
        String suffixName;
        if (fileName.lastIndexOf(".") == -1) {
            //??????????????????
            suffixName = "null";
        } else {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        String filePath;
        //Pid=0????????????????????????,?????????????????????????????????
        if (faFolderId == 0) {
            filePath = FileUtil.STORE_ROOT_PATH + storeId + "/";
        } else {
            FileFolder fileFolder = folderMapper.queryFolderById(faFolderId);
            String fileFolderPath = fileFolder.getFileFolderPath();
            filePath = fileFolderPath + "/";
        }

        FolderUtil.autoFolder(filePath);

        //??????????????????????????? ?????????
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
        // ????????????????????????
        String path = filePath + fileName;
        File dest = new File(path);
        // ????????????????????????
        if (!dest.getParentFile().exists()) {
            // ???????????????
            dest.getParentFile().mkdirs();
        }
        String type = FileUtil.getFileType(suffixName);
        MyFile myFile = new MyFile(-1, fileName, storeId, path, 0, new Date(), faFolderId, size, type);
        if (fileMapper.add(myFile) > 0) {
            Files.copy(file.toPath(), dest.toPath());
            store.setCurrentSize(store.getCurrentSize() + size);
            storeService.updateStore(store);
            log.info("??????ID???" + userId + " ???????????????????????????????????????" + path);
            log.info("????????????????????????" + store.getCurrentSize() + " KB");
            if ("??????".equals(type)) {
                //???????????????
                ThreadPoolUtil.execute(() -> fileUtil.previewImage(Collections.singletonList(myFile)));
            }
        }
    }


    @Override
    public List<MyFile> queryFiles(int userId, int parentFolderId, int pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        //??????????????????????????????
        if (pageNum == -1) {
            startRow = 0;
            showRow = Integer.MAX_VALUE;
        }
        return fileMapper.queryMyFiles(userId, parentFolderId, startRow, showRow);
    }


    @Override
    public List<MyFile> queryFilesByUid(int userId, int pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        /**
         * ??????????????????????????????
         */
        if (pageNum == -1) {
            startRow = 0;
            showRow = Integer.MAX_VALUE;
        }
        return fileMapper.queryListByUid(userId, startRow, showRow);
    }

    @Override
    public List<MyFile> queryFilesByType(Integer uid, String type, int pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        //??????????????????????????????
        if (pageNum == -1) {
            startRow = 0;
            showRow = Integer.MAX_VALUE;
        }
        return fileMapper.queryFilesByType(uid, FileUtil.getTypeChinese(type), startRow, showRow);
    }

    @Override
    public void updateData(MultipartFile file, Integer id) {
        MyFile myFile = fileMapper.queryById(id);
        String path = myFile.getMyFilePath();
        try {
            Files.write(Paths.get(path), file.getBytes());
        } catch (IOException e) {
            throw new FailException("????????????");
        }
    }

    @Override
    public List<Object> getFileAndFolder(Integer parentFid) {
        List<Object> list = new ArrayList<>();
        Integer uid = UserUtil.getUid();
        FileStore store = storeService.queryStoreByUid(uid);
        List<MyFile> files = queryFiles(uid, parentFid, -1);
        List<FileFolder> folders = folderMapper.queryFoldersByPId(store.getFileStoreId(), parentFid);
        list.addAll(folders);
        list.addAll(files);
        return list;
    }


    @Override
    public PageVO<Map<String, String>> thumbnailList(Integer uid, Integer pageNum) {
        int showRow = FileUtil.FILE_SHOW_ROW;
        int startRow = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        List<MyFile> files = fileMapper.queryFilesByType(uid, "??????", startRow, showRow);
        Integer count = fileMapper.countByType(uid, "??????");
        if (files == null || files.isEmpty()) {
            return PageVO.of(Collections.emptyList(), pageNum, showRow, count);
        }
        List<Map<String, String>> list = fileUtil.previewImage(files);
        return PageVO.of(list, pageNum, showRow, count);
    }


    @Override
    public boolean deleteById(int fileId) {
        MyFile file = fileMapper.queryById(fileId);
        if (fileMapper.delete(fileId) > 0) {
            int uid = UserUtil.getUid();
            FileStore store = storeService.queryStoreByUid(uid);
            store.setCurrentSize(Math.abs(store.getCurrentSize() - file.getSize()));
            return trashService.addTrash(file, uid) && storeService.updateStore(store);
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return false;
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadFile(int fileId, boolean preview) throws IOException {
        //?????????????????????????????? ??????????????????
        MyFile file = fileMapper.queryById(fileId);
        String filePath = file.getMyFilePath();
        return this.download(filePath);
    }

    @Override
    public ResponseEntity<InputStreamResource> downloadComm(String path) throws IOException {
        return this.download(path);
    }


    /**
     * ??????????????????
     * ?????????????????? ??????????????????
     * ??????????????? ??????????????????????????????
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
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(0)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(null);
        }
        //???????????????
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(downloadFile.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(downloadFile.getInputStream()));
    }


    @Override
    public boolean updateFileName(int fileId, String newName) {
        MyFile file = fileMapper.queryById(fileId);
        String filePath = file.getMyFilePath();
        String path = filePath.substring(0, filePath.lastIndexOf('/') + 1);
        String newFilePath = path + newName;
        //????????????????????????
        File fileIO = new File(file.getMyFilePath());
        if (fileIO.renameTo(new File(newFilePath))) {
            file.setMyFilePath(newFilePath);
            file.setMyFileName(newName);
            // ????????????????????????
            String suffixName;
            if (newName.lastIndexOf(".") == -1) {
                //??????????????????
                suffixName = "null";
            } else {
                suffixName = newName.substring(newName.lastIndexOf("."));
            }
            //??????????????????
            file.setType(FileUtil.getFileType(suffixName));
            return fileMapper.update(file) > 0;
        } else {
            return false;
        }
    }


    @Override
    public String share(int fileId) {
        Object scd = redisTemplate.opsForValue().get("share:fid:" + fileId);
        String code = String.valueOf(Optional.ofNullable(scd)
                .orElse(NumberUtil.createCode()));
        try {
            //SessionCallback??????
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
                System.out.println("redis????????????");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return code;
    }

    @Override
    public MyFile getShareFile(String shareCode) {
        Object fileId = redisTemplate.opsForValue().get("share:code:" + shareCode);
        if (fileId == null) {
            return null;
        }
        return fileMapper.queryById((int) fileId);
    }

    @Override
    public Map<String, Integer> clearBadFile() {
        HashMap<String, Integer> map = new HashMap<>();
        List<MyFile> files = fileMapper.queryList();
        int sum = 0;
        for (MyFile file : files) {
            String filePath = file.getMyFilePath();
            if (!new File(filePath).exists()) {
                fileMapper.delete(file.getMyFileId());
                sum++;
            }
        }
        map.put("db????????????", sum);
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
        map.put("db???????????????", sum);
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
            // ????????????????????????
            if (!dest.getParentFile().exists()) {
                // ???????????????
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

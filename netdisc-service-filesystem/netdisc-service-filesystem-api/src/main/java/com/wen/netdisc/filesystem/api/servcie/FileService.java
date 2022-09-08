package com.wen.netdisc.filesystem.api.servcie;

import com.wen.netdisc.common.vo.PageVO;
import com.wen.netdisc.common.pojo.MyFile;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * FileService业务类
 * 对File进行上传、下载、删除、查询、修改、分享、清除无效文件
 * 并操作服务器I/O
 *
 * @author calwen
 */
public interface FileService {
    /**
     * 上传，不建议使用，请使用分片上传
     */
    boolean uploadFile(MultipartFile file, int userId, Integer faFolderId);

    void giveUserFile(File file, int userId, Integer faFolderId) throws IOException;


    List<MyFile> queryFiles(int userId, int parentFolderId, int pageNum);

    List<MyFile> queryFilesByUid(int userId, int pageNum);


    PageVO<Map<String, String>> thumbnailList(Integer uid, Integer pageNum);


    boolean deleteById(int fileId);

    /**
     * 文件下载业务
     */
    ResponseEntity<InputStreamResource> downloadFile(int fileId, boolean preview) throws IOException;

    ResponseEntity<InputStreamResource> downloadComm(String path) throws IOException;


    boolean updateFileName(int fileId, String newName);

    /**
     * 生成分享码，保存在redis中
     * {key：fid，value：code}
     *
     * @param fileId 文件id
     * @return 文件分享码
     */
    String share(int fileId);

    /**
     * 通过分享码在redis中找到fid，在通过数据库返回文件信息
     *
     * @param shareCode 分享码
     * @return
     */
    MyFile getShareFile(String shareCode);

    Map<String, Integer> clearBadFile();

    boolean uploadFileComm(MultipartFile file, String path);

    List<MyFile> queryFilesByType(Integer uid, String type, int pageNum);

    void updateData(MultipartFile file, Integer id);

    List<Object> getFileAndFolder(Integer parentFid);


}

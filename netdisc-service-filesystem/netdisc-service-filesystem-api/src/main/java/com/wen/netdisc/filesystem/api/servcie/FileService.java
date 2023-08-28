package com.wen.netdisc.filesystem.api.servcie;

import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.vo.PageVO;
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
     * 上传文件，不建议使用，请使用分片上传
     *
     * @param file       要上传的文件
     * @param userId     用户ID
     * @param faFolderId 文件夹ID
     * @return 是否上传成功
     */
    boolean uploadFile(MultipartFile file, int userId, Integer faFolderId);

    /**
     * 将文件赋给用户，即文件上传
     *
     * @param file       要上传的文件
     * @param userId     用户ID
     * @param faFolderId 文件夹ID
     * @throws IOException 如果发生I/O错误
     */
    void giveUserFile(File file, int userId, Integer faFolderId) throws IOException;

    /**
     * 查询用户指定文件夹下的文件列表
     *
     * @param userId         用户ID
     * @param parentFolderId 父文件夹ID
     * @param pageNum        页码
     * @return 文件列表
     */
    List<MyFile> queryFiles(int userId, int parentFolderId, int pageNum);

    /**
     * 搜索 文件
     */
    List<MyFile> queryFileSearch(int storeId, String keyword);

    /**
     * 查询用户所有文件的列表
     *
     * @param userId  用户ID
     * @param pageNum 页码
     * @return 文件列表
     */
    List<MyFile> queryFilesByUid(int userId, int pageNum);

    /**
     * 获取用户缩略图列表
     *
     * @param uid     用户ID
     * @param pageNum 页码
     * @return 缩略图列表
     */
    PageVO<Map<String, String>> thumbnailList(Integer uid, Integer pageNum);

    /**
     * 根据文件ID删除文件
     *
     * @param fileId 文件ID
     * @return 是否删除成功
     */
    boolean deleteById(int fileId);

    /**
     * 下载文件
     *
     * @param fileId  文件ID
     * @param preview 是否预览
     * @return 下载的文件资源
     * @throws IOException 如果发生I/O错误
     */
    ResponseEntity<InputStreamResource> downloadFile(int fileId, boolean preview) throws IOException;

    /**
     * 下载通用文件
     *
     * @param path 文件路径
     * @return 下载的文件资源
     * @throws IOException 如果发生I/O错误
     */
    ResponseEntity<InputStreamResource> downloadComm(String path) throws IOException;

    /**
     * 更新文件名
     *
     * @param fileId  文件ID
     * @param newName 新的文件名
     * @return 是否更新成功
     */
    boolean updateFileName(int fileId, String newName);

    /**
     * 生成文件分享码，保存在Redis中
     *
     * @param fileId 文件ID
     * @return 文件分享码
     */
    String share(int fileId);

    /**
     * 根据分享码获取文件信息
     *
     * @param shareCode 分享码
     * @return 分享的文件信息
     */
    MyFile getShareFile(String shareCode);

    /**
     * 清除无效的文件
     *
     * @return 清除的文件数量
     */
    Map<String, Integer> clearBadFile();

    /**
     * 通用文件上传
     *
     * @param file 要上传的文件
     * @param path 文件路径
     * @return 是否上传成功
     */
    boolean uploadFileComm(MultipartFile file, String path);

    /**
     * 根据文件类型查询用户文件列表
     *
     * @param uid     用户ID
     * @param type    文件类型
     * @param pageNum 页码
     * @return 文件列表
     */
    List<MyFile> queryFilesByType(Integer uid, String type, int pageNum);

    /**
     * 更新数据
     *
     * @param file 要更新的文件
     * @param id   文件ID
     */
    void updateData(MultipartFile file, Integer id);

    /**
     * 获取文件和文件夹列表
     *
     * @param parentFid 父文件夹ID
     * @return 文件和文件夹列表
     */
    List<Object> getFileAndFolder(Integer parentFid);

    /**
     * 获取共享列表
     */
    List<MyFile> sharingList();

    /**
     * 修改共享状态
     */
    boolean setSharing(Integer userId, Integer id);

}

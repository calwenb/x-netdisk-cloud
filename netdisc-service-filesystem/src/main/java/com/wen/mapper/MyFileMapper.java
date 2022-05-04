package com.wen.mapper;

import com.wen.pojo.MyFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileMapper类 持久层
 * @author Mr.文
 */
@Mapper
@Repository
@CacheConfig(cacheNames = "file")
public interface MyFileMapper {
    List<MyFile> queryAllFiles();

    List<MyFile> queryMyFiles(int userId, int parentFolderId, int startRow, int showRow);

    List<MyFile> queryFilesByType(int userId, String type, int startRow, int showRow);

    Integer addFile(MyFile myFile);

    /**
     * 通过ID查询文件
     * 缓存
     *
     * @param myFileId
     * @return
     */
    @Cacheable(key = "'fid:'+#p0")
    MyFile queryFileById(int myFileId);

    /**
     * 通过ID删除文件
     * 删除对应的缓存
     *
     * @param myFileId
     * @return 修改行数
     */
    @CacheEvict(key = "'fid:'+#p0")
    Integer deleteByMyFileId(int myFileId);

    /**
     * 修改文件
     * 删除对应的缓存
     *
     * @param myFile
     * @return 修改行数
     */
    @CacheEvict(key = "'fid:'+#p0.myFileId")
    Integer updateByFileId(MyFile myFile);
}

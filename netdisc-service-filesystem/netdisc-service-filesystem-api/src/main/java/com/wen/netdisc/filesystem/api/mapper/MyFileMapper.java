package com.wen.netdisc.filesystem.api.mapper;

import com.wen.netdisc.common.pojo.MyFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * FileMapper类 持久层
 *
 * @author calwen
 */
@Mapper
@Repository
public interface MyFileMapper {
    List<MyFile> queryList();

    List<MyFile> queryMyFiles(int userId, int parentFolderId, int startRow, int showRow);

    List<MyFile> queryFilesByType(int userId, String type, int startRow, int showRow);

    Integer countByType(int userId, String type);

    List<MyFile> queryListByUid(int userId, int startRow, int showRow);


    Integer add(MyFile myFile);

    /**
     * 通过ID查询文件
     */
    MyFile queryById(int myFileId);

    /**
     * 通过ID删除文件
     * 删除对应的缓存
     *
     * @param myFileId
     * @return 修改行数
     */
    Integer delete(int myFileId);

    /**
     * 修改文件
     * 删除对应的缓存
     *
     * @param myFile
     * @return 修改行数
     */
    Integer update(MyFile myFile);
}

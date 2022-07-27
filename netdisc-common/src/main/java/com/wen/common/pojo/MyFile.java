package com.wen.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * File实体类
 *
 * @author calwen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MyFile implements Serializable {

    /**
     * 文件ID
     */
    private int myFileId;
    /**
     * 文件名
     */
    private String myFileName;
    /**
     * 文件仓库ID
     */
    private int fileStoreId;
    /**
     * 文件存储路径
     */
    private String myFilePath;
    /**
     * 下载次数
     */
    private int downloadCount;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 父文件夹ID
     */
    private int parentFolderId;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 文件类型
     */
    private String type;

}

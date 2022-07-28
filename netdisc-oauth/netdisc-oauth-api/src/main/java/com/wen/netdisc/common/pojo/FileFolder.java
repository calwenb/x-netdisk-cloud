package com.wen.netdisc.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * FileFolder实体类
 *
 * @author calwen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileFolder implements Serializable {

    /**
     * 文件夹ID
     */
    private int fileFolderId;
    /**
     * 文件夹名称
     */
    private String fileFolderName;
    /**
     * 父文件夹ID
     */
    private int parentFolderId;
    /**
     * 所属文件仓库ID
     */
    private int fileStoreId;
    /**
     * 文件夹路径
     */
    private String fileFolderPath;

}

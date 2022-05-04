package com.wen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * FileFolder实体类
 * @author Mr.文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileFolder {

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

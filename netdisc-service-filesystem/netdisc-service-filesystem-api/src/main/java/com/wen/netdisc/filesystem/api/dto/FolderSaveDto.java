package com.wen.netdisc.filesystem.api.dto;

import lombok.Data;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/8/23
 */
@Data
public class FolderSaveDto {
    /**
     * 文件夹名称
     */
    private String fileFolderName;
    /**
     * 父文件夹ID
     */
    private int parentFolderId;

}

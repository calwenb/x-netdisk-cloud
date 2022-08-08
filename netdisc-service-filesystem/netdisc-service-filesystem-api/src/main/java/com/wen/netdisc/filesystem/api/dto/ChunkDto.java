package com.wen.netdisc.filesystem.api.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 文件块
 */
@Data
public class ChunkDto implements Serializable {

    Integer storeId;
    Integer faFolderId;
    /**
     * 当前文件块，从1开始
     */
    private Integer number;
    /**
     * 分块大小
     */
    private Long size;
    /**
     * 当前分块大小
     */
    private Long currentSize;
    /**
     * 总块数
     */
    private Integer sum;
    /**
     * 总大小
     */
    private Long totalSize;
    /**
     * 文件标识
     */
    private String identifier;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 相对路径
     */
    private String path;


    /**
     * 二进制文件
     */
    private MultipartFile file;
}



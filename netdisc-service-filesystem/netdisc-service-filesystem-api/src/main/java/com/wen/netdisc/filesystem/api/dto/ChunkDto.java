package com.wen.netdisc.filesystem.api.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 文件块
 */
@Data
public class ChunkDto implements Serializable {

    /**
     * 当前文件块，从1开始
     */
    private Integer chunkNumber;
    /**
     * 分块大小
     */
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    private Long currentChunkSize;
    /**
     * 总块数
     */
    private Integer totalChunks;
    /**
     * 总大小
     */
    private Long totalSize;
    /**
     * 文件标识，即md5
     */
    private String identifier;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 相对路径
     */
    private String relativePath;


    /**
     * 二进制文件
     */
    private MultipartFile file;

    private String path;

    Integer storeId;

    Integer faFolderId;
    /**
     * 文件类型
     */
    private String type;
    private String md5;

}



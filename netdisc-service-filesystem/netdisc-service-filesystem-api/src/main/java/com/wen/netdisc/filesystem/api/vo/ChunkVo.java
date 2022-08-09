package com.wen.netdisc.filesystem.api.vo;

import com.wen.netdisc.filesystem.api.dto.ChunkDto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/8/8
 */
@Data
public class ChunkVo {

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
    private String relativePath;
    /**
     * 总块数
     */
    private Integer totalChunks;
    private String path;

    Integer storeId;

    Integer faFolderId;
    /**
     * 文件类型
     */
    private String type;

}

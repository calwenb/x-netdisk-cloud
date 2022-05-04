package com.wen.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * File实体类
 * @author Mr.文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStore implements Serializable {

    /**
     * 文件仓库ID
     */
    private int fileStoreId;
    /**
     * 用户ID
     */
    private int userId;
    /**
     * 当前容量（单位KB）
     */
    private long currentSize;
    /**
     * 最大容量（单位KB）
     */
    private long maxSize;
}

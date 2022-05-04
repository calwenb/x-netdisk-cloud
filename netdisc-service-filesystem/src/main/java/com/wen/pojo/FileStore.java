package com.wen.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * File实体类
 * @author Mr.文
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStore {

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

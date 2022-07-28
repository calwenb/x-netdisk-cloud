package com.wen.netdisc.common.pojo;

import com.wen.baseorm.core.annotation.FieldName;
import com.wen.baseorm.core.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * File实体类
 *
 * @author calwen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("file_store")
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

    /**
     * 用户信息
     */
    @FieldName(exist = false)
    private User user;

    public FileStore(int fileStoreId, int userId, long currentSize, long maxSize) {
        this.fileStoreId = fileStoreId;
        this.userId = userId;
        this.currentSize = currentSize;
        this.maxSize = maxSize;
    }
}

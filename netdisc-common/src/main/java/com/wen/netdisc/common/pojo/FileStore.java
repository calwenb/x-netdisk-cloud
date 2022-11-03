package com.wen.netdisc.common.pojo;

import com.wen.releasedao.core.annotation.FieldId;
import com.wen.releasedao.core.annotation.FieldName;
import com.wen.releasedao.core.annotation.TableName;
import com.wen.releasedao.core.enums.IdTypeEnum;
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
    @FieldId(idType = IdTypeEnum.AUTO)
    private Integer fileStoreId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 当前容量（单位KB）
     */
    private Long currentSize;
    /**
     * 最大容量（单位KB）
     */
    private Long maxSize;

    /**
     * 用户信息
     */
    @FieldName(exist = false)
    private User user;

}

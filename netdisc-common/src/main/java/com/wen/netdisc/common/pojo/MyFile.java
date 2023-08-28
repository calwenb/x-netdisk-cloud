package com.wen.netdisc.common.pojo;

import com.wen.releasedao.core.annotation.FieldId;
import com.wen.releasedao.core.enums.IdTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * File实体类
 *
 * @author calwen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MyFile implements Serializable {

    /**
     * 文件ID
     */
    @FieldId(idType = IdTypeEnum.AUTO)
    private Integer myFileId;
    /**
     * 文件名
     */
    private String myFileName;
    /**
     * 文件仓库ID
     */
    private Integer fileStoreId;
    /**
     * 文件存储路径
     */
    private String myFilePath;
    /**
     * 下载次数
     */
    private Integer downloadCount;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 父文件夹ID
     */
    private Integer parentFolderId;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 是否 共享文件
     */
    private Boolean sharing;
}

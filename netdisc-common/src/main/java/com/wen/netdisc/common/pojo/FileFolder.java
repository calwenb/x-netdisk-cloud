package com.wen.netdisc.common.pojo;

import com.wen.releasedao.core.annotation.FieldId;
import com.wen.releasedao.core.enums.IdTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * FileFolder实体类
 *
 * @author calwen
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileFolder implements Serializable {

    /**
     * 文件夹ID
     */
    @FieldId(idType = IdTypeEnum.AUTO)
    private Integer fileFolderId;
    /**
     * 文件夹名称
     */
    private String fileFolderName;
    /**
     * 父文件夹ID
     */
    private Integer parentFolderId;
    /**
     * 所属文件仓库ID
     */
    private Integer fileStoreId;
    /**
     * 文件夹路径
     */
    private String fileFolderPath;

}

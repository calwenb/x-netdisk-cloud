package com.wen.netdisc.common.pojo;

import com.wen.releasedao.core.annotation.*;
import com.wen.releasedao.core.enums.IdTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author calwen
 * @since 2022/9/6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("bulletin")
public class Bulletin {
    @IdField(idType = IdTypeEnum.AUTO)
    private Integer id;
    private String title;
    private String content;
    private Integer level;
    private Boolean publish;
    @FieldName("uid")
    private Integer userId;
    @CreateTime
    private Date createTime;
    @UpdateTime
    private Date updateTime;
}

package com.wen.netdisc.filesystem.api.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/8/23
 */
@Data
public class FolderSaveDto {
    /**
     * 文件夹名称
     */
    @NotBlank(message = "文件夹名不能为空")
    private String name;
    /**
     * 父文件夹ID
     */
    @NotNull(message = "父id不能为空")
    private Integer parentId;

}

package com.wen.netdisc.filesystem.api.servcie;

import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.filesystem.api.dto.ChunkDto;
import com.wen.netdisc.filesystem.api.vo.ChunkVo;


/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/8/9
 */
public interface ChunkService {
    /**
     * 保存片
     *
     * @param chunk
     * @return
     */
    ResultVO<ChunkVo> saveChunk(ChunkDto chunk);

    /**
     * 合并片，保存数据
     */
    ResultVO<ChunkVo> merge(ChunkDto chunk);

    /**
     * 秒传，保存数据
     *
     * @param chunk
     * @return
     */
    ResultVO<Integer> skip(ChunkDto chunk);
}

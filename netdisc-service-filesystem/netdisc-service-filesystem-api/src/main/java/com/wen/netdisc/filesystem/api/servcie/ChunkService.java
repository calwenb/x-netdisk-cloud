package com.wen.netdisc.filesystem.api.servcie;

import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.filesystem.api.dto.ChunkDto;
import com.wen.netdisc.filesystem.api.vo.ChunkVo;

import java.io.IOException;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/8/9
 */
public interface ChunkService {
    ResultVO<ChunkVo> saveChunk(ChunkDto chunk);

    ResultVO<ChunkVo> merge(ChunkDto chunk);

    ResultVO<Integer> skip(ChunkDto chunk) ;
}

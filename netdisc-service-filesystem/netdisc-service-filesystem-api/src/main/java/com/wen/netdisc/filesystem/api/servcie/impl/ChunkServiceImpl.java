package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.netdisc.common.enums.RedisEnum;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.filesystem.api.dto.ChunkDto;
import com.wen.netdisc.filesystem.api.servcie.ChunkService;
import com.wen.netdisc.filesystem.api.servcie.FileService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.FolderUtil;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import com.wen.netdisc.filesystem.api.vo.ChunkVo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.stream.BaseStream;
import java.util.stream.Stream;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/8/9
 */
@Service
public class ChunkServiceImpl implements ChunkService {
    private final String CHUNK_PATH = "temp/chunk";
    private final String BACKUP_PATH = "temp/backup";
    private final String REDIS_PREFIX = RedisEnum.CHUNK_PREFIX.getProperty();
    @Resource
    FileService fileService;
    @Resource
    RedisTemplate<String, String> redisTemplate;

    @Override
    public ResultVO<ChunkVo> saveChunk(ChunkDto chunk) {
        MultipartFile chunkFile = chunk.getFile();
        try {
            chunkFile.transferTo(Paths.get(generatePath(chunk)));
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailException("秒传失败");
        }
        return ResultUtil.successDo();
    }

    @Override
    public ResultVO<ChunkVo> merge(ChunkDto chunk) {

        //创建合并后的文件
        String mergePath = FileUtil.ROOT_PATH + BACKUP_PATH + "/" + chunk.getFilename();
        String chunkPath = FileUtil.ROOT_PATH + CHUNK_PATH;
        Stream<Path> stream = null;
        try {
            File file = new File(mergePath);
            file.createNewFile();
            stream = Files.list(Paths.get(chunkPath + "/" + chunk.getIdentifier()))
                    .filter(p -> p.getFileName().toString().contains(chunk.getIdentifier() + "_"))
                    .sorted((o1, o2) -> {
                        String f1 = o1.getFileName().toString();
                        String f2 = o2.getFileName().toString();
                        String n1 = f1.substring(f1.lastIndexOf("_") + 1);
                        String n2 = f2.substring(f2.lastIndexOf("_") + 1);
                        return Integer.valueOf(n1).compareTo(Integer.valueOf(n2));
                    });
            stream.forEach((path -> {
                try {
                    Files.write(Paths.get(mergePath), Files.readAllBytes(path), StandardOpenOption.APPEND);
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
            Files.delete(Paths.get(chunkPath + "/" + chunk.getIdentifier()));
            fileService.giveUserFile(file, UserUtil.getUid(), chunk.getFaFolderId());
            redisTemplate.opsForValue().set(REDIS_PREFIX + chunk.getIdentifier(), mergePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailException("合并文件失败");
        } finally {
            Optional.ofNullable(stream).ifPresent(BaseStream::close);
        }
        return ResultUtil.successDo();
    }

    @Override
    public ResultVO<Integer> skip(ChunkDto chunk) {
        Object o = redisTemplate.opsForValue().get(REDIS_PREFIX + chunk.getIdentifier());
        if (o == null) {
            return ResultUtil.success(0);
        }
        Path path = Paths.get(String.valueOf(o));
        if (!Files.exists(path)) {
            return ResultUtil.success(0);
        }
        try {
            //发现可以秒传，将目标文件 ctrl+c v 给用户
            fileService.giveUserFile(path.toFile(), UserUtil.getUid(), chunk.getFaFolderId());
        } catch (IOException e) {
            e.printStackTrace();
            throw new FailException("秒传失败");
        }
        return ResultUtil.success(1);
    }

    private String generatePath(ChunkDto chunk) {
        String folder = FileUtil.ROOT_PATH + CHUNK_PATH + "/" + chunk.getIdentifier();
        FolderUtil.autoFolder(folder);
        return folder + "/" + chunk.getIdentifier() + "_" + chunk.getChunkNumber();
    }
}


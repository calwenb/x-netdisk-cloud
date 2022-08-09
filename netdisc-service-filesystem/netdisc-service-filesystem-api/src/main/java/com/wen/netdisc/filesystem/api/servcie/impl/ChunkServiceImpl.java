package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.enums.RedisEnum;
import com.wen.netdisc.common.util.ResultUtil;
import com.wen.netdisc.filesystem.api.dto.ChunkDto;
import com.wen.netdisc.filesystem.api.servcie.ChunkService;
import com.wen.netdisc.filesystem.api.servcie.FileService;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import com.wen.netdisc.filesystem.api.util.FolderUtil;
import com.wen.netdisc.filesystem.api.util.UserUtil;
import com.wen.netdisc.filesystem.api.vo.ChunkVo;
import org.apache.http.entity.ContentType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author calwen
 * @eamil calvinwen@dianhun.cn
 * @since 2022/8/9
 */
@Service
public class ChunkServiceImpl implements ChunkService {
    private static final String CHUNK_PATH = FileUtil.ROOT_PATH + "/temp/chunk";
    private static final String BACKUP_PATH = FileUtil.ROOT_PATH + "/temp/backup";
    private static final String REDIS_PREFIX = RedisEnum.CHUNK_PREFIX.getProperty();
    @Resource
    FileService fileService;
    @Resource
    RedisTemplate redisTemplate;

    @Override
    public ResultVO<ChunkVo> saveChunk(ChunkDto chunk) {
        MultipartFile chunkFile = chunk.getFile();
        try {
            chunkFile.transferTo(Paths.get(generatePath(chunk)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResultUtil.successDo();
    }

    @Override
    public ResultVO<ChunkVo> merge(ChunkDto chunk) {
        //创建合并后的文件
        String mergePath = BACKUP_PATH + "/" + chunk.getFilename();
        try {
            new File(mergePath).createNewFile();
            Files.list(Paths.get(CHUNK_PATH + "/" + chunk.getIdentifier())).filter(p -> p.getFileName().toString().contains(chunk.getIdentifier() + "_")).sorted((o1, o2) -> {
                String f1 = o1.getFileName().toString();
                String f2 = o2.getFileName().toString();
                String n1 = f1.substring(f1.lastIndexOf("_") + 1);
                String n2 = f2.substring(f2.lastIndexOf("_") + 1);
                return Integer.valueOf(n1).compareTo(Integer.valueOf(n2));
            }).forEach((path -> {
                try {
                    Files.write(Paths.get(mergePath), Files.readAllBytes(path), StandardOpenOption.APPEND);
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
            Files.delete(Paths.get(CHUNK_PATH + "/" + chunk.getIdentifier()));
            //照常走上传文件逻辑
            MultipartFile multipartFile = new MockMultipartFile(chunk.getFilename(), chunk.getFilename(), ContentType.APPLICATION_OCTET_STREAM.toString(), Files.newInputStream(Paths.get(mergePath)));
            fileService.uploadFile(multipartFile, UserUtil.getUid(), chunk.getFaFolderId());
            //保存路径,秒传
            redisTemplate.opsForValue().set(REDIS_PREFIX + chunk.getIdentifier(), mergePath);
//            redisTemplate.opsForValue().set(REDIS_PREFIX + chunk.getMd5(), mergePath, 1, TimeUnit.DAYS);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
            //照常走上传文件逻辑
            MultipartFile multipartFile = new MockMultipartFile(chunk.getRelativePath(), chunk.getRelativePath(), ContentType.APPLICATION_OCTET_STREAM.toString(), Files.newInputStream(path));
            fileService.uploadFile(multipartFile, UserUtil.getUid(), chunk.getFaFolderId());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResultUtil.success(1);
    }

    private String generatePath(ChunkDto chunk) {
        String folder = CHUNK_PATH + "/" + chunk.getIdentifier();
        FolderUtil.autoFolder(folder);
        return folder + "/" + chunk.getIdentifier() + "_" + chunk.getChunkNumber();
    }
}


package com.wen.netdisc.filesystem.api.servcie.impl;

import com.wen.netdisc.common.enums.RedisEnum;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.filesystem.api.mapper.MyFileMapper;
import com.wen.netdisc.filesystem.api.servcie.FileService;
import com.wen.netdisc.filesystem.api.servcie.TrashService;
import com.wen.netdisc.filesystem.api.util.ConfigUtil;
import com.wen.netdisc.filesystem.api.util.FileUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class TrashServiceImpl implements TrashService {
    private final String REDIS_PREFIX = RedisEnum.TRASH_PREFIX.getProperty();
    @Resource
    ConfigUtil configUtil;
    @Resource
    MyFileMapper fileMapper;
    @Resource
    RedisTemplate<String, MyFile> redisTemplate;
    @Resource
    FileService fileService;


    @Override
    public List<MyFile> getListByUid(int userId, int pageNum) {
        int offset = FileUtil.FILE_SHOW_ROW;
        int count = (pageNum - 1) * FileUtil.FILE_SHOW_ROW;
        if (pageNum == -1) {
            count = 0;
            offset = Integer.MAX_VALUE;
        }
        Set<MyFile> set = redisTemplate.opsForZSet()
                .reverseRangeByScore(REDIS_PREFIX + userId, 0, Double.MAX_VALUE, count, offset);
        set = Optional.ofNullable(set).orElse(Collections.emptySet());
        return new ArrayList<>(set);
    }

    @Override
    public Boolean addTrash(MyFile trash, int uid) {
        return redisTemplate.opsForZSet().add(REDIS_PREFIX + uid, trash, System.currentTimeMillis());
    }

    @Override
    public boolean deleteById(MyFile trash, int uid) {
        return this.delTrashComm(trash, REDIS_PREFIX + uid);
    }


    @Override
    public boolean restored(MyFile trash, int uid) {
        if (Optional.ofNullable(redisTemplate.opsForZSet().remove(REDIS_PREFIX + uid, trash)).orElse(0L) == 0) {
            throw new FailException("删除垃圾文件失败");
        }
        return fileMapper.add(trash) > 0;
    }


    @Override
    public int takeOutTrash() {
        //过期时间
        long expiredTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(configUtil.getTrashKeepDay());
//        long expiredTime = System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(60);
        int count = 0;
        //获取全部用户垃圾桶,删除过期文件
        Set<String> keys = redisTemplate.keys(REDIS_PREFIX + "*");
        if (keys == null) {
            return count;
        }
        for (String key : keys) {
            Set<MyFile> trashSet = redisTemplate.opsForZSet().rangeByScore(key, 0, expiredTime);
            if (trashSet == null) {
                continue;
            }
            for (MyFile trash : trashSet) {
                if (this.delTrashComm(trash, key)) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public ResponseEntity<InputStreamResource> getData(MyFile trash) {
        try {
            return fileService.downloadComm(trash.getMyFilePath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据失败");
        }
    }

    private boolean delTrashComm(MyFile trash, String key) {
        if (Optional.ofNullable(redisTemplate.opsForZSet().remove(key, trash)).orElse(0L) == 0) {
            return false;
        }
        File delFile = new File(trash.getMyFilePath());
        boolean isDelete = false;
        if (delFile.isFile() && delFile.exists()) {
            isDelete = delFile.delete();
        }
        return isDelete;
    }
}

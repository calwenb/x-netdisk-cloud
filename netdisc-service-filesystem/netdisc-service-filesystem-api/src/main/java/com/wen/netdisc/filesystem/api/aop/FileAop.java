package com.wen.netdisc.filesystem.api.aop;


import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.util.NullUtil;
import com.wen.netdisc.common.util.ThreadPoolUtil;
import com.wen.netdisc.filesystem.api.mapper.MyFileMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 下载量Aop类
 *
 * @author calwen
 * @create 2022/7/12 11:40
 **/
@Aspect
@Component
public class FileAop {
    @Resource
    MyFileMapper fileMapper;

    @Around("execution(* com.wen.netdisc.filesystem.api.servcie.FileService.downloadFile(..))")
    public Object downloadCount(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        boolean preview = (boolean) args[1];
        int fileId = (int) joinPoint.getArgs()[0];
        Object rs = joinPoint.proceed();
        ThreadPoolUtil.execute(() -> {
            if (!NullUtil.hasNull(rs, preview)) {
                MyFile file = fileMapper.queryById(fileId);
                if (!preview) {
                    file.setDownloadCount(file.getDownloadCount() + 1);
                }
                fileMapper.update(file);
            }
        });
        return rs;
    }
}

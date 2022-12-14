package com.wen.netdisc.filesystem.api.aop;

import com.wen.netdisc.common.pojo.MyFile;
import com.wen.netdisc.common.util.ThreadPoolUtil;
import com.wen.netdisc.filesystem.api.servcie.EsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * ElasticSearch的AOP
 * 用于保证es中数据一致性，
 * 当修改据库es中也需要修改
 *
 * @author calwen
 */
@Aspect
@Component
public class EsAop {
    @Resource
    EsService esService;

    @Around("execution(* com.wen.netdisc.filesystem.api.mapper.MyFileMapper.add(..))")
    public int addEsData(ProceedingJoinPoint pjp) throws Throwable {
        Object rs = pjp.proceed();
        MyFile file = (MyFile) pjp.getArgs()[0];
        rs = Optional.ofNullable(rs).orElse(0);
        if ((int) rs > 0) {
            ThreadPoolUtil.execute(() -> esService.addData(file));
        }
        return (int) rs;
    }

    @Around("execution(* com.wen.netdisc.filesystem.api.mapper.MyFileMapper.delete(..))")
    public int delEsData(ProceedingJoinPoint pjp) throws Throwable {
        Object rs = pjp.proceed();
        String id = String.valueOf(pjp.getArgs()[0]);
        rs = Optional.ofNullable(rs).orElse(0);
        if ((int) rs > 0) {
            ThreadPoolUtil.execute(() -> esService.delDate(String.valueOf(id)));
        }
        return (int) rs;
    }

    @Around("execution(* com.wen.netdisc.filesystem.api.mapper.MyFileMapper.update(..))")
    public int updateEsData(ProceedingJoinPoint pjp) throws Throwable {
        Object rs = pjp.proceed();
        MyFile file = (MyFile) pjp.getArgs()[0];
        rs = Optional.ofNullable(rs).orElse(0);
        ThreadPoolUtil.execute(() -> esService.updateData(file));
        return (int) rs;
    }
}

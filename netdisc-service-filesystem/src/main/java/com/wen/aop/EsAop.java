package com.wen.aop;

import com.wen.pojo.MyFile;
import com.wen.servcie.EsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ElasticSearch的AOP
 * 用于保证es中数据一致性，
 * 当修改据库es中也需要修改
 *
 * @author Mr.文
 */
@Aspect
@Component
public class EsAop {
    @Autowired
    EsService esService;

    @Around("execution(* com.wen.mapper.MyFileMapper.addFile(..))")
    public int addEsData(ProceedingJoinPoint pjp) throws Throwable {
        Object rs = pjp.proceed();
        MyFile file = (MyFile) pjp.getArgs()[0];
        if (rs == null) {
            return 0;
        }
        if ((int) rs > 0) {
            esService.addData(file);
        }
        return (int) rs;
    }

    @Around("execution(* com.wen.mapper.MyFileMapper.deleteByMyFileId(..))")
    public int delEsData(ProceedingJoinPoint pjp) throws Throwable {
        Object rs = pjp.proceed();
        String id = String.valueOf(pjp.getArgs()[0]);
        if (rs == null) {
            return 0;
        }
        if ((int) rs > 0) {
            esService.delDate(String.valueOf(id));
        }
        return (int) rs;
    }

    @Around("execution(* com.wen.mapper.MyFileMapper.updateByFileId(..))")
    public int updateEsData(ProceedingJoinPoint pjp) throws Throwable {
        Object rs = pjp.proceed();
        MyFile file = (MyFile) pjp.getArgs()[0];
        if (rs == null) {
            return 0;
        }
        esService.updateData(file);
        return (int) rs;
    }
}

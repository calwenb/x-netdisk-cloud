package com.wen.releasedao.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.Field;
import java.util.List;

/**
 * MapperLogAop类
 * 对BaseMapper进行日志输出
 * AOP会将类中的属性初始化，并且不会设值
 * 所以不用考虑线程安全问题
 *
 * @author calwen
 * @since 2022/7/9
 */
@Slf4j
@Aspect
public class MapperLogAop {

    @Around("execution(public * com.wen.releasedao.core.mapper.impl.BaseMapperImpl.*(..))")
    public Object printfLog(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("\n===========================\n");
        Object rs = joinPoint.proceed();
       /* Field pstLog = joinPoint.getTarget().getClass().getDeclaredField("pstLog");
        pstLog.setAccessible(true);
        String outLog = "sql==> " + pstLog.get(joinPoint.getTarget());
        System.out.println(outLog);*/

        if (rs instanceof List<?>) {
            int row = 0;
            List<Object> rsList = (List<Object>) rs;
            for (Object o : rsList) {
                System.out.println("row " + (++row) + " ==> " + o);
            }
            System.out.println("count ==> " + row);
        } else {
            System.out.println("result ==> " + rs);
        }
        //log.info(outLog);
        System.out.println("\n===========================\n");
        return rs;
    }

}

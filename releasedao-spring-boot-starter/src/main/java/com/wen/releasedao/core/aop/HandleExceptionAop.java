package com.wen.releasedao.core.aop;

import com.wen.releasedao.core.exception.MapperException;
import com.wen.releasedao.core.manager.LoggerManager;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * 异常处理AOP<br>
 * 记录错误日志
 *
 * @author calwen
 * @since 2022/8/24
 */
@Aspect
@Slf4j
@Order
public class HandleExceptionAop {
    @Pointcut("execution(public * com.wen.releasedao.core.mapper.BaseMapper.*(..))")
    private void pointcut() {
    }

    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void handle(Throwable e) {
        if (e instanceof MapperException) {
            MapperException mapperException = (MapperException) e;
            LoggerManager.logError(mapperException.getMessage(), mapperException.getCause());
        }
    }

}

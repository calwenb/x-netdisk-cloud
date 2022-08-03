package com.wen.netdisc.common.aop;

import com.wen.commutil.util.LoggerUtil;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.exception.BadRequestException;
import com.wen.netdisc.common.exception.FailException;
import com.wen.netdisc.common.exception.OauthException;
import com.wen.netdisc.common.exception.RpcException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/**
 * 对rpc调用进行环绕增强
 * 确保rpc调用结果正确且成功
 * 1.直接抛出 rpc错误的返回ResultVO
 *
 * @author calwen
 * @since 2022/7/28
 */
@Aspect
@Component
public class RpcAop {
    @Around("@within(com.wen.netdisc.common.annotation.PrcVerify)")
    public <T> Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = joinPoint.proceed();
        //处理非标准响应体
        if (!(o instanceof ResultVO)) {
            if (o instanceof ResponseEntity) {
                return o;
            }
            throw new RpcException("未知rcp响应体");
        }
        //处理标准响应体，传递响应体
        ResultVO<T> resultVO = (ResultVO<T>) o;
        switch (resultVO.getCode()) {
            case 2000:
                return resultVO;
            case 7000:
                throw new FailException(resultVO.getMessage());
            case 4010:
                throw new OauthException(resultVO.getMessage());
            case 4000:
                throw new BadRequestException(resultVO.getMessage());
            case 5000:
                throw new Exception(resultVO.getMessage());
            default:
                LoggerUtil.error("[异常] 未知异常返回状态", RpcAop.class);
                throw new RpcException("未知异常返回状态");
        }
    }
}

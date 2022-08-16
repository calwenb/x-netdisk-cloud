package com.wen.netdisc.common.exception;

import com.wen.commutil.util.LoggerUtil;
import com.wen.commutil.vo.ResultVO;
import com.wen.netdisc.common.util.ResultUtil;
import feign.FeignException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 *
 * @author calwen
 * @create 2022/7/12 11:07
 **/
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {
    @ExceptionHandler(OauthException.class)
    public ResultVO<String> OauthException(Exception e) {
        LoggerUtil.warn("\n [验证失败] ：===> " + e.getMessage(), GlobalExceptionHandler.class);
        return ResultUtil.unauthorized();
    }

    @ExceptionHandler(FailException.class)
    public ResultVO<String> failException(Exception e) {
        LoggerUtil.warn("\n [操作失败] ：===> " + e.getMessage(), GlobalExceptionHandler.class);
        return ResultUtil.error(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResultVO<String> BadRequestException(Exception e) {
        LoggerUtil.warn("\n [坏的请求] ：===> " + e.getMessage(), GlobalExceptionHandler.class);
        return ResultUtil.badRequest(e.getMessage());
    }

    @ExceptionHandler({RpcException.class, FeignException.class})
    public ResultVO<String> rpcException(Exception e) {
        LoggerUtil.error("\n [rpc调用错误]：===> " + e, GlobalExceptionHandler.class);
        e.printStackTrace();
        return ResultUtil.exception("rpc调用错误");
    }

    @ExceptionHandler({RuntimeException.class, Exception.class, Throwable.class})
    public ResultVO<String> RuntimeException(Exception e) {
        LoggerUtil.error("\n [发生异常]：===> " + e, GlobalExceptionHandler.class);
        e.printStackTrace();
        return ResultUtil.exception(e.getMessage());
    }


}

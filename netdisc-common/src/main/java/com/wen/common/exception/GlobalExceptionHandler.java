package com.wen.common.exception;

import com.wen.common.util.ResultUtil;
import com.wen.commutil.util.LoggerUtil;
import com.wen.commutil.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理类
 *
 * @author calwen
 * @create 2022/7/12 11:07
 **/
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(FailException.class)
    public ResultVO<String> failException(Exception e) {
        LoggerUtil.warn("\n [操作失败] ：===> " + e.getMessage(), GlobalExceptionHandler.class);
        return ResultUtil.error(e.getMessage());
    }

    @ExceptionHandler(RpcException.class)
    public ResultVO<String> rpcException(Exception e) {
        LoggerUtil.error("\n [rpc调用错误]：===> " + e, GlobalExceptionHandler.class);
        return ResultUtil.exception("发生了一些错误，错误信息: " + e.getMessage());
    }

    //TODO 修改统一响应体
    @ExceptionHandler(Exception.class)
    public ResultVO<String> exception(Exception e, HttpServletRequest request) {
        LoggerUtil.error("\n [发生异常]：===> " + e, GlobalExceptionHandler.class);
        return ResultUtil.exception("发生了一些错误，错误信息: " + e.getMessage());
    }


}

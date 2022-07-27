package com.wen.commutil.exception;

import com.wen.commutil.utils.BaseResultUtil;
import com.wen.commutil.utils.LoggerUtil;
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

    //TODO 修改统一响应体
    @ExceptionHandler(Exception.class)
    public ResultVO<String> exception(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        LoggerUtil.error("\n发生异常：===> " + e, GlobalExceptionHandler.class);
        LoggerUtil.error("发生异常接口:===>" + request.getRequestURL(), GlobalExceptionHandler.class);
        return BaseResultUtil.exception("发生了一些错误，错误信息: " + e.getMessage());
    }

}

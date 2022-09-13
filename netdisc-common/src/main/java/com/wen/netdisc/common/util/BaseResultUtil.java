package com.wen.netdisc.common.util;

import com.wen.netdisc.common.enums.BaseResultEnum;
import com.wen.netdisc.common.vo.ResultVO;

/**
 * 统一返回体
 * 使用时建议继承扩展
 *
 * @author calwen
 * @since 2022/7/20
 */
public class BaseResultUtil {

    public static <T> ResultVO<T> success(T data) {
        return buildSuccessVO(data, BaseResultEnum.SUCCESS.getMessage());
    }


    public static <T> ResultVO<T> success(T data, String msg) {
        return buildSuccessVO(data, msg);
    }

    public static <T> ResultVO<T> successDo() {
        return buildSuccessVO(null, BaseResultEnum.SUCCESS.getMessage());
    }

    public static <T> ResultVO<T> successDo(String msg) {
        return buildSuccessVO(null, msg);
    }


    public static <T> ResultVO<T> error(String msg) {
        return buildErrorVO(BaseResultEnum.ERROR.getCode(), msg);
    }

    public static <T> ResultVO<T> errorDo() {
        return buildErrorVO(BaseResultEnum.ERROR.getCode(), BaseResultEnum.ERROR.getMessage());
    }


    public static <T> ResultVO<T> exception() {
        return buildErrorVO(BaseResultEnum.EXCEPTION.getCode(), BaseResultEnum.EXCEPTION.getMessage());
    }

    public static <T> ResultVO<T> exception(String msg) {
        return buildErrorVO(BaseResultEnum.EXCEPTION.getCode(), BaseResultEnum.EXCEPTION.getMessage() + msg);
    }


    public static <T> ResultVO<T> badRequest() {
        return buildErrorVO(BaseResultEnum.BAD_REQUEST.getCode(), BaseResultEnum.BAD_REQUEST.getMessage());
    }

    public static <T> ResultVO<T> badRequest(String msg) {
        return buildErrorVO(BaseResultEnum.BAD_REQUEST.getCode(), msg);
    }

    public static <T> ResultVO<T> unauthorized() {
        return buildErrorVO(BaseResultEnum.UNAUTHORIZED.getCode(), BaseResultEnum.UNAUTHORIZED.getMessage());
    }

    public static <T> ResultVO<T> unauthorized(String msg) {
        return buildErrorVO(BaseResultEnum.UNAUTHORIZED.getCode(), BaseResultEnum.UNAUTHORIZED.getMessage());
    }

    private static <T> ResultVO<T> buildSuccessVO(T data, String msg) {
        return ResultVO.<T>builder()
                .code(BaseResultEnum.SUCCESS.getCode())
                .message(msg)
                .data(data)
                .build();
    }

    private static <T> ResultVO<T> buildErrorVO(Integer code, String msg) {
        return ResultVO.<T>builder().code(code).message(msg).data(null).build();
    }
}

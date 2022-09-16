package com.wen.netdisc.common.util;

import com.wen.netdisc.common.enums.BaseResultEnum;
import com.wen.netdisc.common.vo.ResultVO;
import com.wen.netdisc.common.exception.RpcException;

import java.util.Objects;

/**
 * 已有全局prc调用失败AOP
 * 不必使用
 *
 * @author calwen
 * @since 2022/8/15
 */
@Deprecated
public class ResultVoUtil {
    public static <T> void isSuccess(ResultVO<T> resultVO) {
        Integer successCode = BaseResultEnum.SUCCESS.getCode();
        if (resultVO != null && Objects.equals(resultVO.getCode(), successCode)) {
            return;
        }
        LoggerUtil.error("resultVO===>" + resultVO, ResultVoUtil.class);
        throw new RpcException("rpc 调用失败");
    }
}
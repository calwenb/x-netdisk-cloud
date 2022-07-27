/*
package com.wen.commutil.utils;

import com.wen.commutil.enums.BaseResultEnum;
import com.wen.commutil.vo.ResultVO;

import java.util.Objects;

public class ResultVoUtil {
    public static <T> void isSuccess(ResultVO<T> resultVO) {
        Integer successCode = BaseResultEnum.SUCCESS.getCode();
        if (resultVO != null && Objects.equals(resultVO.getCode(), successCode)) {
            return;
        }
        throw new RuntimeException("rpc 调用失败");
    }
}*/

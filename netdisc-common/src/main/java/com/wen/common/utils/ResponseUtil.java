package com.wen.common.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * ResponseUtil类
 * 约束响应规则
 *
 * @author Mr.文
 */
public class ResponseUtil {

    private static String SUCCESS_CODE = "200";
    private static String FILE_MISS_CODE = "404";
    private static String ERROR_CODE = "700";
    private static String U_FILE_ERROR_CODE = "701";

    private static String D_FILE_ERROR_CODE = "702";
    private static String POWER_ERROR = "401";


    public static String success(String msg) {
        HashMap<String, String> resp = new HashMap<>(16);
        resp.put("code", SUCCESS_CODE);
        resp.put("msg", msg);
        return JSON.toJSONString(resp);
    }

    public static String error(String msg) {
        HashMap<String, String> resp = new HashMap<>(16);
        resp.put("code", ERROR_CODE);
        resp.put("msg", msg);
        return JSON.toJSONString(resp);
    }

    public static String uploadFileError(String msg) {
        HashMap<String, String> resp = new HashMap<>(16);
        resp.put("code", D_FILE_ERROR_CODE);
        resp.put("msg", msg);
        return JSON.toJSONString(resp);
    }

    public static String downloadFileError(String msg) {
        HashMap<String, String> resp = new HashMap<>(16);
        resp.put("code", U_FILE_ERROR_CODE);
        resp.put("msg", msg);
        return JSON.toJSONString(resp);
    }

    public static String fileMiss(String msg) {
        HashMap<String, String> resp = new HashMap<>(16);
        resp.put("code", FILE_MISS_CODE);
        resp.put("msg", msg);
        return JSON.toJSONString(resp);
    }

    public static String powerError(String msg) {
        HashMap<String, String> resp = new HashMap<>(16);
        resp.put("code", POWER_ERROR);
        resp.put("msg", msg);
        return JSON.toJSONString(resp);

    }
}

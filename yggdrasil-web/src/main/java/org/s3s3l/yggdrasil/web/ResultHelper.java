package org.s3s3l.yggdrasil.web;

import org.s3s3l.yggdrasil.bean.web.DefaultJsonResult;
import org.s3s3l.yggdrasil.bean.web.JsonResult;

public abstract class ResultHelper {
    private static String DEFAULT_SUCCESS_MSG = "";
    private static String DEFAULT_FAIL_MSG = "";

    public static void changeMessage(String successMsg, String failMsg) {
        DEFAULT_SUCCESS_MSG = successMsg;
        DEFAULT_FAIL_MSG = failMsg;
    }

    public static <T> JsonResult<T> success(T data) {
        return DefaultJsonResult.<T> builder()
                .msg(DEFAULT_SUCCESS_MSG)
                .data(data)
                .build();
    }

    public static <T> JsonResult<T> fail(T data) {
        return fail(-1, data);
    }

    public static <T> JsonResult<T> fail(int code, T data) {
        return fail(code, DEFAULT_FAIL_MSG, data);
    }

    public static <T> JsonResult<T> fail(String message) {
        return fail(-1, message);
    }

    public static <T> JsonResult<T> fail(int code) {
        return fail(code, null);
    }

    public static <T> JsonResult<T> fail(int code, String message) {
        return fail(code, message, null);
    }

    public static <T> JsonResult<T> fail(int code, String message, T data) {
        return DefaultJsonResult.<T> builder()
                .msg(message)
                .data(data)
                .code(code)
                .build();
    }
}

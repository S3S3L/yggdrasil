package io.github.s3s3l.yggdrasil.bean.web;

public interface JsonResult<T> {

    int getCode();

    boolean isSuccess();

    String getMsg();

    T getData();
}

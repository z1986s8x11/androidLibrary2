package com.zhusx.core.interfaces;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/11/9 10:39
 */

public interface IHttpResult<T> {
    boolean isSuccess();

    T getData();

    String getMessage();
}

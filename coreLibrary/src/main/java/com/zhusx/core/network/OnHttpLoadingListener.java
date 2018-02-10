package com.zhusx.core.network;


import android.support.annotation.Nullable;

public interface OnHttpLoadingListener<Id, Result, Parameter> {
    /**
     * 开始请求
     *
     * @param id id
     */
    void onLoadStart(Id id, HttpRequest<Parameter> request);

    /**
     * 请求发生错误
     *
     * @param id            id
     * @param isAPIError    <ul>
     *                      <li>true 解析之后 返回字段有表示请求错误的</li>
     *                      <li>false 请求超时 网络连接异常 解析错误等</li>
     *                      </ul>
     * @param error_message 错误消息
     */
    void onLoadError(Id id, HttpRequest<Parameter> request, @Nullable Result result, boolean isAPIError, String error_message);

    /**
     * @param id     id
     * @param result result
     */
    void onLoadComplete(Id id, HttpRequest<Parameter> request, Result result);
}

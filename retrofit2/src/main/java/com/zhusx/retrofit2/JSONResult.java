package com.zhusx.retrofit2;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/3/13 18:06
 */

public class JSONResult<M> {
    public int code;
    public String message;
    public String error;
    public M data;
}

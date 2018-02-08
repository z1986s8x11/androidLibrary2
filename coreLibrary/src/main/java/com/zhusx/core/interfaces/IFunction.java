package com.zhusx.core.interfaces;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2018/2/8 9:34
 */

public interface IFunction<T, R> {
    R apply(T t);
}

package com.zhusx.core.interfaces;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/10/13 9:59
 */
@FunctionalInterface
public interface IFilter<T> {
    boolean isFilter(T t);
}

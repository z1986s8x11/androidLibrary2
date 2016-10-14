package com.zhusx.core.interfaces;

import java.util.List;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 11:13
 */
public interface IPageData<T> {
    int getTotalPageCount();

    List<T> getListData();
}

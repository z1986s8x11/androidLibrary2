package com.zhusx.core.interfaces;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/7/13 10:20
 */

public interface Lib_LoadingListener {

    void _reLoadData(boolean isRefresh);

    boolean _isLoading();

    boolean _hasCache();

    int _getNextPage();

    boolean hasMoreData();
}

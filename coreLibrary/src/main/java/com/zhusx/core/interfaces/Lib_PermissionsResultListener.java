package com.zhusx.core.interfaces;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/30 14:23
 */

public interface Lib_PermissionsResultListener {
    void _addPermissionsListener(Lib_OnRequestPermissionsResult listener);

    void _removePermissionsListener(Lib_OnRequestPermissionsResult listener);
}

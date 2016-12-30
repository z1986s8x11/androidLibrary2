package com.zhusx.core.interfaces;

import android.support.annotation.NonNull;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/30 14:21
 */

public interface Lib_OnRequestPermissionsResult {
    void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}

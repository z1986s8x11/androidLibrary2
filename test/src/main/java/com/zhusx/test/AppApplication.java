package com.zhusx.test;

import android.app.Application;

import com.zhusx.core.debug.LogUtil;


/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/5 11:12
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.DEBUG = true;
    }
}

package com.zhusx.test;

import android.app.Application;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.show.Lib_SourceCodeManager;


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
        new Lib_SourceCodeManager.Builder(this).setOnlyShowPackage(false).build();
    }
}

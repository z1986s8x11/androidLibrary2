package com.zhusx.test;

import android.app.AlertDialog;
import android.app.Application;


/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/5 11:12
 */

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new Tools.Builder(this).build();
        new AlertDialog.Builder(this).create().show();
    }
}

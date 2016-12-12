package com.zhusx.core.imp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/12 9:59
 */

public class ActivityLifecycleCallbacksImp implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}

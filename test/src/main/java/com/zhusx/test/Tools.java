package com.zhusx.test;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.widget.slidingmenu.SlidingMenu;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/6 9:35
 */

public class Tools {
    private Tools(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                SlidingMenu mSlidingMenu = new SlidingMenu(activity, SlidingMenu.SLIDING_CONTENT);
                final View right = LayoutInflater.from(activity).inflate(R.layout.lib_layout_linearlayout, null, false);
                right.setBackgroundColor(Color.GREEN);
                mSlidingMenu.setMenu(right);
                mSlidingMenu.setBehindWidth(600);
                mSlidingMenu.setMode(SlidingMenu.LEFT_RIGHT);


                final View left = LayoutInflater.from(activity).inflate(R.layout.lib_layout_linearlayout, null, false);
                left.setBackgroundColor(Color.YELLOW);
                mSlidingMenu.setSecondaryMenu(left);

                mSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
                    @Override
                    public void onOpened() {
                        LogUtil.e(this, "setOnOpenedListener");
                    }
                });
                mSlidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
                    @Override
                    public void onOpen() {
                        LogUtil.e(this, "setOnOpenListener");
                    }
                });
                mSlidingMenu.setSecondaryOnOpenListner(new SlidingMenu.OnOpenListener() {
                    @Override
                    public void onOpen() {
                        LogUtil.e(this, "setSecondaryOnOpenListner");
                    }
                });
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
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static class Builder {
        Application application;

        public Builder(Application application) {
            this.application = application;
        }

        public Tools build() {
            return new Tools(application);
        }
    }
}

package com.zhusx.core.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/10/23 10:30
 */

public class _Toast extends Toast {
    private Context mContext;
    private WindowManager wm;
    private Handler mHandler;

    public _Toast(Context context) {
        super(context);
        mContext = context.getApplicationContext();
    }

    public static Toast makeText(Context context, @StringRes int resId, int duration) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return Toast.makeText(context, resId, duration);
        }
        _Toast result = new _Toast(context);
        View v = Toast.makeText(context, resId, duration).getView();
        result.setView(v);
        result.setDuration(duration);
        return result;
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return Toast.makeText(context, text, duration);
        }
        _Toast result = new _Toast(context);
        View v = Toast.makeText(context, text, duration).getView();
        result.setView(v);
        result.setDuration(duration);
        return result;
    }

    @Override
    public void show() {
        if (NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
            super.show();
            return;
        }
        if (wm == null) {
            wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        if (getView().getParent() == null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            params.format = PixelFormat.TRANSLUCENT;
            params.windowAnimations = android.R.style.Animation_Toast;
            params.y = _Densitys.dip2px(mContext, 64);
            params.type = WindowManager.LayoutParams.TYPE_TOAST;
            wm.addView(getView(), params);
            int mDuration;
            switch (getDuration()) {
                case Toast.LENGTH_SHORT:
                    mDuration = 1000;
                    break;
                case Toast.LENGTH_LONG:
                    mDuration = 5000;
                    break;
                default:
                    mDuration = getDuration();
                    break;
            }
            if (mHandler == null) {
                mHandler = new Handler();
            }
            mHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (getView().getParent() != null) {
                        wm.removeView(getView());
                    }
                }
            }, mDuration);
        }
    }

}
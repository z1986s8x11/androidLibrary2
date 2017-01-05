package com.zhusx.core.helper;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/1/5 9:33
 */
public class Lib_TimerHelper implements Lib_OnCycleListener {
    private Handler mHandler = new Handler();
    private Runnable runnable;
    private long delayMillis;
    private boolean isStart;
    private boolean isCanPause;

    public Lib_TimerHelper(@NonNull Runnable runnable, long delayMillis, @NonNull Lib_LifeCycleListener listener) {
        this(runnable, delayMillis, listener, true);
    }

    public Lib_TimerHelper(@NonNull Runnable runnable, long delayMillis, @NonNull Lib_LifeCycleListener listener, boolean isCanPause) {
        listener._addOnCycleListener(this);
        this.runnable = runnable;
        this.delayMillis = delayMillis;
        this.isCanPause = isCanPause;
    }

    public void start() {
        if (!isStart) {
            mHandler.postDelayed(runnable, delayMillis);
            isStart = true;
        }
    }

    @Override
    public void onResume() {
        if (isCanPause) {
            if (!isStart) {
                mHandler.postDelayed(runnable, delayMillis);
                isStart = true;
            }
        }
    }

    @Override
    public void onPause() {
        if (isCanPause) {
            if (isStart) {
                mHandler.removeCallbacks(runnable);
                isStart = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        if (isStart) {
            mHandler.removeCallbacks(runnable);
            isStart = false;
        }
    }
}

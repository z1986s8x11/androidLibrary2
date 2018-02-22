package com.zhusx.core.helper;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;
import com.zhusx.core.interfaces.Lib_Runnable;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2015/11/25. 9:32
 */
public class Lib_Subscribes {
    private static Set<Subscriber> subscribes = new LinkedHashSet<>();
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static <T> void subscribe(@NonNull final Subscriber<T> subscriber) {
        subscribe(subscriber, null);
    }

    public static <T> void subscribe(@NonNull final Subscriber<T> subscriber, final Lib_LifeCycleListener lifeCycle) {
        if (subscribes.contains(subscriber)) {
            return;
        }
        subscribes.add(subscriber);
        final CancelRunnable<T> runnable = new CancelRunnable<T>(subscriber, lifeCycle);
        executor.execute(runnable);
    }

    public static abstract class Subscriber<T> {
        @MainThread
        public void onComplete(T t) {
        }

        @WorkerThread
        public abstract T doInBackground() throws Exception;

        @MainThread
        public void onError(Throwable t) {
        }
    }

    private static class CancelRunnable<T> extends Lib_Runnable implements Lib_OnCycleListener {
        final Handler mHandler = new Handler(Looper.getMainLooper());
        private Subscriber<T> subscriber;
        private Lib_LifeCycleListener lifeCycle;

        public CancelRunnable(Subscriber<T> subscriber, Lib_LifeCycleListener lifeCycle) {
            this.subscriber = subscriber;
            this.lifeCycle = lifeCycle;
            if (lifeCycle != null) {
                lifeCycle._addOnCycleListener(this);
            }
        }

        @Override
        public void onResume() {
        }

        @Override
        public void onPause() {
        }

        @Override
        public void onDestroy() {
            _setCancel();
        }

        @Override
        public void _setCancel() {
            super._setCancel();
            if (lifeCycle != null) {
                lifeCycle._removeOnCycleListener(this);
                lifeCycle = null;
            }
            if (subscriber != null) {
                subscribes.remove(subscriber);
                subscriber = null;
            }
        }

        @Override
        public void run() {
            try {
                if (!_isCancel()) {
                    final T t = subscriber.doInBackground();
                    if (!_isCancel()) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (subscriber != null) {
                                        subscriber.onComplete(t);
                                    }
                                } catch (Exception e) {
                                    if (LogUtil.DEBUG) {
                                        LogUtil.e(e);
                                    }
                                }
                            }
                        });
                    }
                }
            } catch (final Exception e) {
                if (!_isCancel()) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            subscriber.onError(e);
                        }
                    });
                }
            } finally {
                if (!_isCancel()) {
                    if (lifeCycle != null) {
                        lifeCycle._removeOnCycleListener(this);
                        lifeCycle = null;
                    }
                    if (subscriber != null) {
                        subscribes.remove(subscriber);
                    }
                }
            }
        }
    }
}

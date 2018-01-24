package com.zhusx.core.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;
import com.zhusx.core.network.P_NetworkStateReceiver;
import com.zhusx.core.utils._Networks;
import com.zhusx.core.utils._Sets;
import com.zhusx.core.utils._Systems;

import java.util.Set;


/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:54
 */
public class ZsxApplicationManager {
    public static _Networks.NetType _Current_NetWork_Status = _Networks.NetType.Default;
    /*是否有Toast等通知权限*/
    public static boolean _IS_NOTIFICATION_ENABLED;
    @SuppressLint("StaticFieldLeak")
    private static Application mApplication;

    private ZsxApplicationManager() {
    }

    public static Application getApplication() {
        if (mApplication == null) {
            mApplication = _Systems.getApplication();
        }
        return mApplication;
    }

    public static ZsxApplicationManager.Builder builder(Application app) {
        ZsxApplicationManager.mApplication = app;
        return new Builder(app);
    }

    public static class Builder {
        private Application context;
        private boolean monitorNet;
        private boolean safety;
        private Application.ActivityLifecycleCallbacks activityCallbacks;
        private P_NetworkStateReceiver receiver;

        private Builder(Application app) {
            this.context = app;
        }

        public ZsxApplicationManager build() {
            init();
            return new ZsxApplicationManager();
        }

        /**
         * 设置是否监听网络变化
         */
        public Builder setMonitorNet(boolean monitorNet) {
            this.monitorNet = monitorNet;
            return this;
        }

        /**
         * 设置日志开关
         */
        public Builder setLogDebug(boolean isDebug) {
            LogUtil.DEBUG = isDebug;
            return this;
        }

        public Builder setSafety(boolean safety) {
            this.safety = safety;
            return this;
        }

        private void init() {
            /*监听网络变化*/
            if (monitorNet) {
                receiver = new P_NetworkStateReceiver();
                receiver.registerNetworkStateReceiver(context);
            }
            _IS_NOTIFICATION_ENABLED = NotificationManagerCompat.from(context).areNotificationsEnabled();
            if (safety) {
                if (!LogUtil.DEBUG) {
                    if ((context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                        //LogUtil.e(this, "程序被修改为可调试状态");
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                    if (android.os.Debug.isDebuggerConnected()) {
                        //LogUtil.e(this, "检测到检测器");
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                activityCallbacks = new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                        Lib_SystemExitManager.addActivity(activity);
                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                    }

                    @Override
                    public void onActivityResumed(Activity activity) {
                        if (activity instanceof Lib_LifeCycleListener) {
                            Set<Lib_OnCycleListener> cycleListener = ((Lib_LifeCycleListener) activity).getCycleListeners();
                            if (!_Sets.isEmpty(cycleListener)) {
                                for (Lib_OnCycleListener l : cycleListener) {
                                    l.onResume();
                                }
                            }
                        }
                    }

                    @Override
                    public void onActivityPaused(Activity activity) {
                        if (activity instanceof Lib_LifeCycleListener) {
                            Set<Lib_OnCycleListener> cycleListener = ((Lib_LifeCycleListener) activity).getCycleListeners();
                            if (!_Sets.isEmpty(cycleListener)) {
                                for (Lib_OnCycleListener l : cycleListener) {
                                    l.onPause();
                                }
                            }
                        }
                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {
                        if (activity instanceof Lib_LifeCycleListener) {
                            Set<Lib_OnCycleListener> cycleListener = ((Lib_LifeCycleListener) activity).getCycleListeners();
                            if (!_Sets.isEmpty(cycleListener)) {
                                for (Lib_OnCycleListener l : cycleListener) {
                                    l.onDestroy();
                                }
                            }
                        }
                        Lib_SystemExitManager.removeActivity(activity);
                    }
                };
                context.registerActivityLifecycleCallbacks(activityCallbacks);
            }
        }

        public void onTerminate() {
            if (receiver != null) {
                receiver.unRegisterNetworkStateReceiver(context);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                if (activityCallbacks != null) {
                    context.unregisterActivityLifecycleCallbacks(activityCallbacks);
                }
            }
            receiver = null;
            context = null;
            activityCallbacks = null;
        }
    }
}

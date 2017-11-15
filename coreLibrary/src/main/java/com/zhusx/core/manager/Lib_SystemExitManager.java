package com.zhusx.core.manager;

import android.app.Activity;
import android.support.annotation.Nullable;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/12 9:27
 */
public class Lib_SystemExitManager {
    private static List<SoftReference<Activity>> list;

    public static void addActivity(Activity activity) {
        if (list == null) {
            list = new ArrayList<SoftReference<Activity>>();
        }
        SoftReference<Activity> sr = new SoftReference<Activity>(activity);
        list.add(sr);
    }

    public static int size() {
        return list.size();
    }

    public static synchronized void removeActivity(Activity activity) {
        if (list == null) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            if (list.get(i) == null) {
                continue;
            }
            if (list.get(i).get() == activity) {
                list.remove(i);
                return;
            }
        }
    }

    @Nullable
    public static Activity getLastActivity() {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1).get();
    }

    public static synchronized void exitSystem() {
        exitSystem(true);
    }

    public static synchronized void exitSystem(Boolean isKillProcess) {
        if (list == null) {
            return;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            SoftReference<Activity> sr = list.get(i);
            if (sr != null) {
                if (sr.get() != null) {
                    sr.get().finish();
                }
            }
        }
        // 在有后台Service需要运行的时候 不能完全杀死进程，否则Service也将被kill
        if (isKillProcess) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
        System.gc();
    }
}

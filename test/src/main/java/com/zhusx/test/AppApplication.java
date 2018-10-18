package com.zhusx.test;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Binder;
import android.preference.PreferenceManager;

import com.zhusx.core.debug.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/5 11:12
 */

public class AppApplication extends Application {
    public static AppApplication application;
    public static String HOST;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        LogUtil.DEBUG = true;
        add(new A() {
            @Override
            public void bind(Binder binder) {
                HOST = binder.bind("域名", HOST);
            }
        });
    }

    public void add(A a) {
        Binder b = new Binder();
        a.bind(b);
    }

    public interface A {
        void bind(Binder binder);
    }

    static Map<String, String[]> map = new HashMap<>();

    public static class Binder {
        SharedPreferences sp;

        public Binder() {
            sp = PreferenceManager.getDefaultSharedPreferences(application);
        }

        public String bind(String name, String value) {
            map.put(name, new String[]{value});
            return sp.getString(name, value);
        }

        public String bind(String name, String[] value) {
            map.put(name, value);
            return sp.getString(name, value[0]);
        }
    }
}

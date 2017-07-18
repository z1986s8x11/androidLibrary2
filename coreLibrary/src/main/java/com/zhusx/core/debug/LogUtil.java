package com.zhusx.core.debug;

import android.text.TextUtils;
import android.util.Log;

import com.zhusx.core.BuildConfig;

/**
 * zsxTitle
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2013/12/18 14:04
 */
public class LogUtil {
    public static boolean DEBUG = BuildConfig.DEBUG;
    private final static String TAG = "[Log]";

    /**
     * @param cls
     * @param message
     */
    public static void e(Object cls, String message) {
        e(message);
    }

    public static void e(String cls, String message) {
        e(message);
    }

    public static void e(Class<?> cls, String message) {
        e(message);
    }

    /**
     * @param message
     */
    public static void e(String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                Log.e(TAG, String.valueOf(message));
                return;
            }
            int index = 0;
            int maxLength = 4000;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLength) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, maxLength + index);
                }
                index += maxLength;
                Log.e(TAG, sub.trim());
            }
        }
    }

    /**
     * @param cls
     * @param message
     */
    public static void d(Object cls, String message) {
        d(message);
    }

    /**
     * @param message
     */
    public static void d(String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                Log.d(TAG, String.valueOf(message));
                return;
            }
            int index = 0;
            int maxLength = 4000;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLength) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, maxLength + index);
                }
                index += maxLength;
                Log.d(TAG, sub.trim());
            }
        }
    }

    /**
     * @param cls
     * @param message
     */
    public static void i(Object cls, String message) {
        i(message);
    }

    /**
     * @param message
     */
    public static void i(String message) {
        if (DEBUG) {
            if (TextUtils.isEmpty(message)) {
                Log.i(TAG, String.valueOf(message));
                return;
            }
            int index = 0;
            int maxLength = 4000;
            String sub;
            while (index < message.length()) {
                // java的字符不允许指定超过总的长度end
                if (message.length() <= index + maxLength) {
                    sub = message.substring(index);
                } else {
                    sub = message.substring(index, maxLength + index);
                }
                index += maxLength;
                Log.i(TAG, sub.trim());
            }
        }
    }

    /**
     * @param tr
     */
    public static void w(Throwable tr) {
        if (DEBUG) {
            tr.printStackTrace();
        }
    }

    public static void e(Throwable tr) {
        if (DEBUG) {
            Log.e(TAG, "ERROR:", tr);
        }
    }
}

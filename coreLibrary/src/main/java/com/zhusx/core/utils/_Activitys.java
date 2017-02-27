package com.zhusx.core.utils;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.zhusx.core.manager.Lib_SystemExitManager;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/14 10:43
 */

public class _Activitys {
    public static final String _EXTRA_Serializable = "extra_Serializable";
    public static final String _EXTRA_String = "extra_String";
    public static final String _EXTRA_Strings = "extra_Strings";
    public static final String _EXTRA_ListSerializable = "extra_ListSerializable";
    public static final String _EXTRA_Integer = "extra_Integer";
    public static final String _EXTRA_Boolean = "extra_boolean";
    public static final String _EXTRA_Double = "extra_double";
    public static final String _EXTRA_String_ID = "extra_id";
    public static final String _EXTRA_Parcelable = "extra_parcelable";

    /**
     * 关闭程序
     */
    public static void _exitSystem(Boolean isKillProcess) {
        Lib_SystemExitManager.exitSystem(isKillProcess);
    }

    /**
     * 屏幕宽度,单位像素(px).
     */
    public static int _getWidth(Context context) {
        return _Views.getWidth(context);
    }

    /**
     * 拿到屏幕的高度
     */
    public static int _getHeight(Context context) {
        return _Views.getHeight(context);
    }

    public static void _addFragment(android.support.v4.app.FragmentActivity activity, int id, android.support.v4.app.Fragment from, android.support.v4.app.Fragment to) {
        _addFragment(activity, id, from, to, null, false, null);
    }

    public static void _addFragment(android.support.v4.app.FragmentActivity activity, int id, android.support.v4.app.Fragment from, android.support.v4.app.Fragment to, String tag, boolean addBackStack, String stackName) {
        if (from == to) {
            return;
        }
        android.support.v4.app.FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        if (from != null && from.isAdded() && !from.isHidden()) {
            transaction.hide(from);
        }
        if (!to.isAdded()) {
            if (tag == null) {
                transaction.add(id, to);
            } else {
                transaction.add(id, to, tag);
            }
            if (addBackStack) {
                transaction.addToBackStack(stackName);
            }
        } else {
            transaction.show(to);
        }
        transaction.commitAllowingStateLoss();
    }

    public static void _replaceFragment(android.support.v4.app.FragmentActivity activity, int id, android.support.v4.app.Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
    }

    public static void _replaceFragment(android.support.v4.app.FragmentActivity activity, int id, android.support.v4.app.Fragment fragment, String tag) {
        activity.getSupportFragmentManager().beginTransaction().replace(id, fragment, tag).commitAllowingStateLoss();
    }

    public static void _addFragment(Activity activity, int id, android.app.Fragment from, android.app.Fragment to) {
        _addFragment(activity, id, from, to, null, false, null);
    }

    public static void _addFragment(Activity activity, int id, android.app.Fragment from, android.app.Fragment to, String tag, boolean addBackStack, String stackName) {
        if (from == to) {
            return;
        }
        android.app.FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        if (from != null && from.isAdded() && !from.isHidden()) {
            transaction.hide(from);
        }
        if (!to.isAdded()) {
            if (tag == null) {
                transaction.add(id, to);
            } else {
                transaction.add(id, to, tag);
            }
            if (addBackStack) {
                transaction.addToBackStack(stackName);
            }
        } else {
            transaction.show(to);
        }
        transaction.commitAllowingStateLoss();
    }

    public static void _replaceFragment(Activity activity, int id, android.app.Fragment fragment) {
        activity.getFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
    }

    public static void _replaceFragment(Activity activity, int id, android.app.Fragment fragment, String tag) {
        activity.getFragmentManager().beginTransaction().replace(id, fragment, tag).commitAllowingStateLoss();
    }

    public static void _dispatchTouchEventHideSoftInput(Activity activity, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = activity.getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (v.getWindowToken() != null) {
                    _EditTexts.hideInputMethod(activity);
                }
            }
        }
    }

    private static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}

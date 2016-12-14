package com.zhusx.core.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/12/14 10:43
 */

public class _Activitys {
    /**
     * 屏幕宽度,单位像素(px).
     */
    public static int getWidth(Context context) {
        return _Views.getWidth(context);
    }

    /**
     * 拿到屏幕的高度
     */
    public static int getHeight(Context context) {
        return _Views.getHeight(context);
    }

    public static void _addFragment(FragmentActivity activity, int id, Fragment to) {
        _addFragment(activity, id, to, null, false, null);
    }

    public static void _addFragment(FragmentActivity activity, int id, Fragment to, String tag, boolean addBackStack, String stackName) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment from = fragmentManager.findFragmentById(id);
        if (from == to) {
            return;
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
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

    public static void _replaceFragment(FragmentActivity activity, int id, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction().replace(id, fragment).commitAllowingStateLoss();
    }

    public static void _replaceFragment(FragmentActivity activity, int id, Fragment fragment, String tag) {
        activity.getSupportFragmentManager().beginTransaction().replace(id, fragment, tag).commitAllowingStateLoss();
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

package com.zhusx.core.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnBackKeyListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;
import com.zhusx.core.manager.Lib_SystemExitManager;
import com.zhusx.core.utils._Lists;

import java.util.HashSet;
import java.util.Set;

public class Lib_BaseActivity extends FragmentActivity implements Lib_LifeCycleListener {
    public static final String _EXTRA_Serializable = "extra_Serializable";
    public static final String _EXTRA_String = "extra_String";
    public static final String _EXTRA_Strings = "extra_Strings";
    public static final String _EXTRA_ListSerializable = "extra_ListSerializable";
    public static final String _EXTRA_Integer = "extra_Integer";
    public static final String _EXTRA_Boolean = "extra_boolean";
    public static final String _EXTRA_Double = "extra_double";
    public static final String _EXTRA_Strig_ID = "extra_id";

    protected String mToastMessage = "再次点击退出";
    /**
     * 一个Activity 只创建一个Toast
     */
    private Toast toast;

    /**
     * 点击非EditText 关闭键盘
     */
    private boolean isClickNoEditTextCloseInput = false;
    /**
     * 基于Activity生命周期回调
     */
    private Set<Lib_OnCycleListener> cycleListener = new HashSet<Lib_OnCycleListener>();
    private Lib_OnBackKeyListener onBackKeyListener;

    public void _showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 设置 是非在顶层处理 点击非EditText 隐藏键盘
     *
     * @param isClickNoEditTextCloseInput
     */
    public void _setClickNoEditTextCloseInput(boolean isClickNoEditTextCloseInput) {
        this.isClickNoEditTextCloseInput = isClickNoEditTextCloseInput;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isClickNoEditTextCloseInput) {
            return super.dispatchTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (v.getWindowToken() != null) {
                    InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
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


    @Override
    public void _addOnCycleListener(Lib_OnCycleListener listener) {
        if (cycleListener.contains(listener)) {
            return;
        }
        cycleListener.add(listener);
    }

    @Override
    public void _removeOnCycleListener(Lib_OnCycleListener listener) {
        cycleListener.remove(listener);
    }

    @Override
    public Set<Lib_OnCycleListener> getCycleListeners() {
        return cycleListener;
    }

    /**
     * 关闭程序
     */
    public void _exitSystem() {
        Lib_SystemExitManager.exitSystem();
    }


    /**
     * 拿到屏幕的宽度
     */
    public int _getFullScreenWidth() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 拿到屏幕的高度
     */
    public int _getFullScreenHeight() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public void _addFragment(int id, Fragment from, Fragment to, String tag,
                             boolean addBackStack, String stackName) {
        if (from == to) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
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
//        transaction.commit();
        transaction.commitAllowingStateLoss();
    }

    public void _addFragment(int id, Fragment from, Fragment to) {
        _addFragment(id, from, to, null, false, null);
    }

    public void _addFragment(int id, Fragment from, Fragment to, String tag) {
        _addFragment(id, from, to, tag, false, null);
    }

    public void _addFragmentToStack(int id, Fragment from, Fragment to) {
        _addFragment(id, from, to, null, true, null);
    }

    public void _addFragmentToStack(int id, Fragment from, Fragment to,
                                    String tag) {
        _addFragment(id, from, to, tag, true, null);
    }

    public void _replaceFragment(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment).commit();
    }

    public void _replaceFragment(int id, Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment, tag).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!_Lists.isEmpty(getSupportFragmentManager().getFragments())) {
            super.onSaveInstanceState(outState);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (onBackKeyListener != null) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (onBackKeyListener.onBackKey()) {
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void _setOnBackKeyListener(Lib_OnBackKeyListener onBackKeyListener) {
        this.onBackKeyListener = onBackKeyListener;
    }
}

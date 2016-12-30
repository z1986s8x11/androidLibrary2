package com.zhusx.core.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnBackKeyListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;
import com.zhusx.core.interfaces.Lib_OnRequestPermissionsResult;
import com.zhusx.core.interfaces.Lib_PermissionsResultListener;
import com.zhusx.core.utils._Activitys;
import com.zhusx.core.utils._Lists;
import com.zhusx.core.utils._Sets;

import java.util.HashSet;
import java.util.Set;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/22 14:03
 */
public class Lib_BaseActivityResult extends FragmentActivity implements Lib_LifeCycleListener, Lib_PermissionsResultListener {
    public static final String _EXTRA_Serializable = _Activitys._EXTRA_Serializable;
    public static final String _EXTRA_String = _Activitys._EXTRA_String;
    public static final String _EXTRA_Strings = _Activitys._EXTRA_Strings;
    public static final String _EXTRA_ListSerializable = _Activitys._EXTRA_ListSerializable;
    public static final String _EXTRA_Integer = _Activitys._EXTRA_Integer;
    public static final String _EXTRA_Boolean = _Activitys._EXTRA_Boolean;
    public static final String _EXTRA_Double = _Activitys._EXTRA_Double;
    public static final String _EXTRA_String_ID = _Activitys._EXTRA_String_ID;

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
    private Set<Lib_OnRequestPermissionsResult> permissionListener;
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
        if (isClickNoEditTextCloseInput) {
            _Activitys._dispatchTouchEventHideSoftInput(this, ev);
        }
        return super.dispatchTouchEvent(ev);
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
    public void _exitSystem(Boolean isKillProcess) {
        _Activitys._exitSystem(isKillProcess);
    }

    private Fragment currentFragment;

    public void _addFragment(int id, Fragment to) {
        if (currentFragment != null && currentFragment == to) {
            return;
        }
        _Activitys._addFragment(this, id, currentFragment, to);
        currentFragment = to;
    }

    public void _addFragment(int id, Fragment to, String tag, boolean addBackStack, String stackName) {
        if (currentFragment != null && currentFragment == to) {
            return;
        }
        _Activitys._addFragment(this, id, currentFragment, to, tag, addBackStack, stackName);
        currentFragment = to;
    }

    public void _replaceFragment(int id, Fragment fragment) {
        _Activitys._replaceFragment(this, id, fragment);
    }

    public void _replaceFragment(int id, Fragment fragment, String tag) {
        _Activitys._replaceFragment(this, id, fragment, tag);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!_Sets.isEmpty(permissionListener)) {
            for (Lib_OnRequestPermissionsResult l : permissionListener) {
                l.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void _addPermissionsListener(Lib_OnRequestPermissionsResult listener) {
        if (permissionListener == null) {
            permissionListener = new HashSet<>();
        }
        permissionListener.add(listener);
    }

    @Override
    public void _removePermissionsListener(Lib_OnRequestPermissionsResult listener) {
        if (permissionListener == null) {
            return;
        }
        permissionListener.remove(listener);
    }
}

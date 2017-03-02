package com.zhusx.core.app;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;
import com.zhusx.core.utils._Activitys;
import com.zhusx.core.utils._Permissions;
import com.zhusx.core.utils._Views;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/22 14:03
 */
public abstract class Lib_BaseFragment extends Fragment implements Lib_LifeCycleListener, _Permissions.OnPermissionResultListener {
    public static final String _EXTRA_Serializable = _Activitys._EXTRA_Serializable;
    public static final String _EXTRA_ListSerializable = _Activitys._EXTRA_ListSerializable;
    public static final String _EXTRA_String = _Activitys._EXTRA_String;
    public static final String _EXTRA_Strings = _Activitys._EXTRA_Strings;
    public static final String _EXTRA_Integer = _Activitys._EXTRA_Integer;
    public static final String _EXTRA_Boolean = _Activitys._EXTRA_Boolean;
    public static final String _EXTRA_Double = _Activitys._EXTRA_Double;
    public static final String _EXTRA_String_ID = _Activitys._EXTRA_String_ID;
    private Toast pToast;
    private boolean pIsFirst = false;
    private boolean isVisibleToUser = false;
    /**
     * 基于Activity生命周期回调
     */
    private Set<Lib_OnCycleListener> cycleListener = new HashSet<Lib_OnCycleListener>();
    private HashSet<ActivityCompat.OnRequestPermissionsResultCallback> pPermissions;

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
    public void onResume() {
        super.onResume();
        for (Lib_OnCycleListener l : cycleListener) {
            l.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (Lib_OnCycleListener l : cycleListener) {
            l.onPause();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (Lib_OnCycleListener l : cycleListener) {
            l.onDestroy();
        }
        cycleListener.clear();
    }

    @Override
    public Set<Lib_OnCycleListener> getCycleListeners() {
        return cycleListener;
    }

    /**
     * 必须在onDetach中调用.防止fragment嵌套fragment生命周期问题
     */
    public void _onDetachClearChildFragment() {
        try {
            //参数是固定写法
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void _showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (getActivity() == null) {
            return;
        }
        if (pToast == null) {
            pToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        }
        pToast.setText(message);
        pToast.show();
    }

    public int _getFullScreenWidth() {
        if (getActivity() == null) {
            return 720;
        }
        return _Views.getWidth(getActivity());
    }

    /**
     * 拿到屏幕的高度
     */
    public int _getFullScreenHeight() {
        if (getActivity() == null) {
            return 1280;
        }
        return _Views.getHeight(getActivity());
    }

    protected final void _removeParentView(View view) {
        if (view != null) {
            // 缓存的rootView需要判断是否已经被加过parent，
            // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> list = getChildFragmentManager().getFragments();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isVisibleToUser && !pIsFirst) {
            pIsFirst = true;
            __onFragmentFirstVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisibleToUser = isVisibleToUser;
        if (this.isVisibleToUser && !pIsFirst && getView() != null) {
            pIsFirst = true;
            __onFragmentFirstVisible();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        pIsFirst = false;
        isVisibleToUser = false;
    }

    protected void __onFragmentFirstVisible() {
    }

    public void finish() {
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void registerPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener) {
        if (pPermissions == null) {
            pPermissions = new HashSet<>();
        }
        pPermissions.add(listener);
    }

    @Override
    public void unregisterPermissionResult(ActivityCompat.OnRequestPermissionsResultCallback listener) {
        if (pPermissions != null) {
            pPermissions.remove(listener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (pPermissions != null) {
            for (ActivityCompat.OnRequestPermissionsResultCallback p : pPermissions) {
                p.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}

package com.zhusx.core.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.zhusx.core.utils._EditTexts;

import java.util.LinkedList;
import java.util.List;

/**
 * 软键盘相关
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:22
 */
public class Lib_SoftKeyboardStateHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened(int keyboardHeightInPx);

        void onSoftKeyboardClosed();
    }

    private final List<SoftKeyboardStateListener> listeners = new LinkedList<SoftKeyboardStateListener>();
    private final View activityRootView;
    private boolean isSoftKeyboardOpened;

    public Lib_SoftKeyboardStateHelper(Activity activity) {
        this(activity, false);
    }

    public Lib_SoftKeyboardStateHelper(Activity activity, boolean isSoftKeyboardOpened) {
        this.activityRootView = activity.getWindow().getDecorView();
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        final Rect r = new Rect();
        activityRootView.getWindowVisibleDisplayFrame(r);
        final int heightDiff = activityRootView.getRootView().getHeight()
                - (r.bottom - r.top);
        if (!isSoftKeyboardOpened && heightDiff > 100) {
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened(heightDiff);
        } else if (isSoftKeyboardOpened && heightDiff < 100) {
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public boolean _isSoftKeyboardOpened() {
        return isSoftKeyboardOpened;
    }

    public void _addSoftKeyboardStateListener(SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void _removeSoftKeyboardStateListener(
            SoftKeyboardStateListener listener) {
        listeners.remove(listener);
    }

    public void _hideInputMethod(Context context) {
        _EditTexts.hideInputMethod(context);
    }

    public void _showInputMethod(EditText et) {
        _EditTexts.showInputMethod(et);
    }

    private void notifyOnSoftKeyboardOpened(int keyboardHeightInPx) {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened(keyboardHeightInPx);
            }
        }
    }

    private void notifyOnSoftKeyboardClosed() {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }
}

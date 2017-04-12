package com.zhusx.core.app;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/6/29 9:56
 */
public class Lib_BasePopupWindow extends PopupWindow {
    public Lib_BasePopupWindow(Context context) {
        super(context);
    }

    public Lib_BasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Lib_BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Lib_BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Lib_BasePopupWindow() {
    }

    public Lib_BasePopupWindow(View contentView) {
        super(contentView);
    }

    public Lib_BasePopupWindow(int width, int height) {
        super(width, height);
    }

    public Lib_BasePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public Lib_BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    @Override
    public void showAtLocation(final View parent, final int gravity, final int x, final int y) {
        if (parent.getWindowToken() == null) {
            //防止Activity or Fragment onCreate 的时候 show会报 BadTokenException
            parent.post(new Runnable() {
                @Override
                public void run() {
                    if (parent.getWindowToken() == null) {
                        return;
                    }
                    showAtLocation(parent, gravity, x, y);
                }
            });
        } else {
            super.showAtLocation(parent, gravity, x, y);
        }
    }


    public void _setOutsideDismiss() {
        if (getBackground() == null) {
            setBackgroundDrawable(new BitmapDrawable());
        }
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
    }
}

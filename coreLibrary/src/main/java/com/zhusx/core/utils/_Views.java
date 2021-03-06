package com.zhusx.core.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhusx.core.debug.LogUtil;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2017/1/4 10:13
 */
public class _Views {
    /**
     * 获得绘制文本的宽和高
     */
    public static Rect getTextFontRect(TextView v, String text) {
        Rect mBound = new Rect();
        v.getPaint().getTextBounds(text, 0, text.length(), mBound);
        return mBound;
    }

    /**
     * 屏幕宽度,单位像素(px).
     */
    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 拿到屏幕的高度
     */
    public static int getHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    /**
     * 计算child 的 width 以及 height 通过 child.getMeasuredHeight()获取高;
     */
    public static void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    /**
     * 在resView 和 父View 之间插入一个ViewGroup
     */
    public static void insertView(View resView, ViewGroup insertView) {
        ViewParent parent = resView.getParent();
        if (parent == null || !(parent instanceof ViewGroup)) {
            if (LogUtil.DEBUG) {
                throw new RuntimeException("resView ==null or resView parent is not ViewGroup");
            }
            return;
        }
        ViewGroup.LayoutParams lp = resView.getLayoutParams();
        ViewGroup group = (ViewGroup) parent;
        int index = group.indexOfChild(resView);
        group.removeView(resView);
        group.addView(insertView, index, lp);
        insertView.addView(resView, lp);
    }

    /**
     * return    false 在顶部
     * true  不在顶部
     */
    public static boolean isVerticallyScrollUp(View mTargetScrollView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetScrollView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetScrollView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTargetScrollView, -1) || mTargetScrollView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetScrollView, -1);
        }
    }

    /**
     * return    false 在底部
     * true  不在底部
     */
    public static boolean isVerticallyScrollDown(View mTargetScrollView) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetScrollView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetScrollView;
                int count = absListView.getAdapter().getCount();
                int fristPos = absListView.getFirstVisiblePosition();
                if (fristPos == 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop()) {
                    return false;
                }
                int lastPos = absListView.getLastVisiblePosition();
                return lastPos > 0 && count > 0 && lastPos == count - 1;
            } else {
                return ViewCompat.canScrollVertically(mTargetScrollView, 1) || mTargetScrollView.getScrollY() < 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetScrollView, 1);
        }
    }

    /**
     * 判断scrollview中某子控件是否可见
     */
    public static boolean isVisibilityForChildView(ScrollView scrollView, View childView) {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        if (childView.getLocalVisibleRect(scrollBounds)) {
            //子控件至少有一个像素在可视范围内
            return true;
        } else {
            //子控件完全不在可视范围内
            return false;
        }
    }

    /**
     * 显示或者隐藏 系统状态栏 在OnCreate 中加入
     * getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
     * onWindowVisibilityChanged(int visibility){}
     *
     * @param fullView 全屏View
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setNavVisibility(View fullView, boolean visible) {
        int newVis = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        if (!visible) {
            newVis |= View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
        fullView.setSystemUiVisibility(newVis);
    }

    /**
     * 显示或者隐藏 系统状态栏 在OnCreate 中加入
     * getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
     * onWindowVisibilityChanged() onSystemUiVisibilityChange() 显示系统状态栏
     * onWindowVisibilityChanged() onSystemUiVisibilityChange 隐藏系统状态栏
     */
    public static void setSystemFulllScreen(Activity activity, boolean isFullScreen) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (isFullScreen) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(lp);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}

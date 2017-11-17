package com.zhusx.core.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ViewSwitcher;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/3/25 16:54
 */
public class _Anims {
    /**
     * 初始化顶部View Title 动画
     */
    public static void initVisibilityAnim(int gravity, View animView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            if (animView.getParent() != null && animView.getParent() instanceof ViewGroup) {
                android.animation.LayoutTransition mLayoutTransition = new android.animation.LayoutTransition();
                android.animation.ValueAnimator animVisible = null;
                android.animation.ValueAnimator animGone = null;
                switch (gravity) {
                    case Gravity.TOP:
                        _Views.measureView(animView);
                        animVisible = android.animation.ObjectAnimator.ofFloat(animView, "translationY", -animView.getMeasuredHeight(), 0);
                        animGone = android.animation.ObjectAnimator.ofFloat(animView, "translationY", 0, -animView.getMeasuredHeight());
                        break;
                    case Gravity.BOTTOM:
                        _Views.measureView(animView);
                        animVisible = android.animation.ObjectAnimator.ofFloat(animView, "translationY", animView.getMeasuredHeight(), 0);
                        animGone = android.animation.ObjectAnimator.ofFloat(animView, "translationY", 0, animView.getMeasuredHeight());
                        break;
                    case Gravity.CENTER:
                        animVisible = android.animation.ObjectAnimator.ofFloat(animView, "alpha", 0f, 1f);
                        animGone = android.animation.ObjectAnimator.ofFloat(animView, "alpha", 1f, 0f);
                        break;
                    case Gravity.RIGHT:
                        _Views.measureView(animView);
                        animVisible = android.animation.ObjectAnimator.ofFloat(animView, "translationX", animView.getMeasuredWidth(), 0);
                        animGone = android.animation.ObjectAnimator.ofFloat(animView, "translationX", 0, animView.getMeasuredWidth());
                        break;
                    case Gravity.LEFT:
                        _Views.measureView(animView);
                        animVisible = android.animation.ObjectAnimator.ofFloat(animView, "translationX", -animView.getMeasuredWidth(), 0);
                        animGone = android.animation.ObjectAnimator.ofFloat(animView, "translationX", 0, -animView.getMeasuredWidth());
                        break;
                    default:
                        return;
                }
                mLayoutTransition.setAnimator(android.animation.LayoutTransition.APPEARING, animVisible);
                mLayoutTransition.setStagger(android.animation.LayoutTransition.APPEARING, 30);
                mLayoutTransition.setDuration(mLayoutTransition.getDuration(android.animation.LayoutTransition.APPEARING));
                mLayoutTransition.setAnimator(android.animation.LayoutTransition.DISAPPEARING, animGone);
                mLayoutTransition.setStagger(android.animation.LayoutTransition.DISAPPEARING, 30);
                mLayoutTransition.setDuration(mLayoutTransition.getDuration(android.animation.LayoutTransition.DISAPPEARING));
                ((ViewGroup) animView.getParent()).setLayoutTransition(mLayoutTransition);
            }
        }
    }

    public void showViewTop(View fromView, int top, ViewSwitcher.ViewFactory makeAnimView) {
        //拿到ContentView
        final FrameLayout rootView = (FrameLayout) ((Activity) fromView.getContext()).findViewById(android.R.id.content);
        //创建用于动画显示的View
        final View animView = makeAnimView.makeView();
        if (animView.getParent() != null) {
            throw new IllegalArgumentException("animView 构造器参数不能有父类  ");
        }
        if (animView.getMeasuredHeight() == 0 || animView.getMeasuredWidth() == 0) {
            _Views.measureView(animView);
        }
        //动画View 添加进ContextView
        rootView.addView(animView, animView.getMeasuredWidth(), animView.getMeasuredHeight());

        int[] location = new int[2];
        fromView.getLocationOnScreen(location);
        animView.setX(fromView.getX() + (fromView.getMeasuredWidth() - animView.getMeasuredWidth()) / 2);
        animView.setY(fromView.getY());

        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", location[1] - fromView.getMeasuredHeight(), location[1] - fromView.getMeasuredHeight() - top);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1, 0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animView, translationY, alpha);
        animator.setDuration(1000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rootView.removeView(animView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                rootView.removeView(animView);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }

    public void showViewMove(View fromView, View toView, ViewSwitcher.ViewFactory makeAnimView) {
        //拿到ContentView
        final FrameLayout rootView = (FrameLayout) ((Activity) fromView.getContext()).findViewById(android.R.id.content);
        //创建用于动画显示的View
        final View animView = makeAnimView.makeView();
        if (animView.getParent() != null) {
            throw new IllegalArgumentException("animView 构造器参数不能有父类  ");
        }
        if (animView.getMeasuredHeight() == 0 || animView.getMeasuredWidth() == 0) {
            _Views.measureView(animView);
        }
        //动画View 添加进ContextView
        rootView.addView(animView, animView.getMeasuredWidth(), animView.getMeasuredHeight());

        int[] location = new int[2];
        fromView.getLocationOnScreen(location);
        int[] location1 = new int[2];
        toView.getLocationOnScreen(location1);
        animView.setX(fromView.getX());
        animView.setY(fromView.getY());

        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", location[1], location1[1]);
        PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", fromView.getLeft(), toView.getLeft());
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(animView, translationY, translationX);
        animator.setDuration(500);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rootView.removeView(animView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                rootView.removeView(animView);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.start();
    }
}

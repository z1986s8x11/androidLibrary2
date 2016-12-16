package com.zhusx.core.helper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.zhusx.core.utils._Views;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/12 9:28
 */
public class Lib_AnimatorHelper {
    protected FrameLayout rootView;

    public Lib_AnimatorHelper(Activity activity) {
        rootView = (FrameLayout) activity.findViewById(android.R.id.content);
    }

    public interface IMakeAnimView {
        View makeView(Context context);
    }

    private View getAnimView(IMakeAnimView makeAnimView) {
        View v = makeAnimView.makeView(rootView.getContext());
        if (v.getParent() != null) {
            throw new IllegalArgumentException("animView 构造器参数不能有父类  ");
        }
        if (v.getMeasuredHeight() == 0 || v.getMeasuredWidth() == 0) {
            _Views.measureView(v);
        }
        rootView.addView(v, v.getMeasuredWidth(), v.getMeasuredHeight());
        return v;
    }

    public void startMove(View fromView, View toView, IMakeAnimView makeAnimView) {
        int[] location = new int[2];
        fromView.getLocationOnScreen(location);
        int[] location1 = new int[2];
        toView.getLocationOnScreen(location1);
        final View animView = getAnimView(makeAnimView);
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

    public void startTop(View fromView, int top, IMakeAnimView makeAnimView) {
        int[] location = new int[2];
        fromView.getLocationOnScreen(location);
        final View animView = getAnimView(makeAnimView);
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
}

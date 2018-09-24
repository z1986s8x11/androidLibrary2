package com.zhusx.others;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.utils._Densitys;
import com.zhusx.core.utils._Views;

/**
 * Author  zhusx
 * Email   327270607@qq.com
 * Create  2018/6/26 11:28
 */
public class ChartView extends View {
    Paint p = new Paint();
    RectF rectF = new RectF(dip2px(4.5f), dip2px(4.5f), dip2px(94), dip2px(94));
    ValueAnimator anim;
    volatile float currentValue;

    final int TIME = 10000;

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p.setColor(Color.RED);
        p.setStrokeWidth(dip2px(3));
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(rectF, -90f, currentValue, false, p);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == View.VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    public void start() {
        if (anim != null && anim.isRunning()) {
            return;
        }
        if (currentValue >= 360) {
            currentValue = 0;
        }
        anim = ValueAnimator.ofFloat(currentValue, 360);
        anim.setDuration((long) (TIME * (360 - currentValue) / 360));
        LogUtil.DEBUG = true;
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentValue = (Float) animation.getAnimatedValue();
                postInvalidate();
                if (currentValue == 360) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentValue = 0;
                            start();
                        }
                    }, 2000);
                }
            }
        });
        anim.start();
    }

    public void stop() {
        if (anim.isRunning()) {
            anim.cancel();
        }
    }

    public int dip2px(float dipValue) {
        return _Densitys.dip2px(getContext(), dipValue);
    }
}

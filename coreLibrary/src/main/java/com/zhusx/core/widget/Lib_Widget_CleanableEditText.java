package com.zhusx.core.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2018/1/5 10:56
 */
public class Lib_Widget_CleanableEditText extends EditText {
    private Drawable mRightDrawable;
    private Drawable InvisibleDrawable;
    private boolean isHasFocus;

    public Lib_Widget_CleanableEditText(Context context) {
        super(context);
        init();
    }

    public Lib_Widget_CleanableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Lib_Widget_CleanableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Drawable[] drawables = this.getCompoundDrawables();
        mRightDrawable = drawables[2];
        if (mRightDrawable != null) {
            InvisibleDrawable = new InvisibleDrawable(this);
            InvisibleDrawable.setBounds(mRightDrawable.getBounds());
            this.setOnFocusChangeListener(new FocusChangeListenerImpl());
            this.addTextChangedListener(new TextWatcherImpl());
            setClearDrawableVisible(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                boolean isClean = (event.getX() > (getWidth() - getTotalPaddingRight()))
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if (isClean) {
                    setText("");
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private class FocusChangeListenerImpl implements OnFocusChangeListener {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            isHasFocus = hasFocus;
            if (isHasFocus) {
                boolean isVisible = !TextUtils.isEmpty(getText().toString().trim());
                setClearDrawableVisible(isVisible);
            } else {
                setClearDrawableVisible(false);
            }
        }

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            setClearDrawableVisible(false);
        }
    }

    private class TextWatcherImpl implements TextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            if (!isEnabled()) {
                setClearDrawableVisible(false);
                return;
            }
            if (isHasFocus) {
                boolean isVisible = !TextUtils.isEmpty(getText().toString().trim());
                setClearDrawableVisible(isVisible);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }

    protected void setClearDrawableVisible(boolean isVisible) {
        if (isVisible) {
            if (mRightDrawable != null) {
                setCompoundDrawables(getCompoundDrawables()[0],
                        getCompoundDrawables()[1], mRightDrawable,
                        getCompoundDrawables()[3]);
            }
        } else {
            if (InvisibleDrawable != null) {
                setCompoundDrawables(getCompoundDrawables()[0],
                        getCompoundDrawables()[1], InvisibleDrawable,
                        getCompoundDrawables()[3]);
            }
        }
    }

    public void setShakeAnimation() {
        this.clearAnimation();
        this.setAnimation(shakeAnimation(5));
    }

    public Animation shakeAnimation(int CycleTimes) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
        translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
        translateAnimation.setDuration(1000);
        return translateAnimation;
    }

    final class InvisibleDrawable extends Drawable {
        InvisibleDrawable(Lib_Widget_CleanableEditText paramEditTextWithClearButton) {
        }

        public final void draw(Canvas paramCanvas) {
        }

        public final int getOpacity() {
            return PixelFormat.UNKNOWN;
        }

        public final void setAlpha(int paramInt) {
        }

        public final void setColorFilter(ColorFilter paramColorFilter) {
        }
    }
}
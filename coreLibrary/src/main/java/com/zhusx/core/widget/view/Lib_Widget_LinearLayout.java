package com.zhusx.core.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhusx.core.R;
import com.zhusx.core.helper.Lib_ShapeHelper;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/30 14:31
 */
public class Lib_Widget_LinearLayout extends LinearLayout {
    private float aspectRatio = 0f; //长宽比

    public Lib_Widget_LinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    public Lib_Widget_LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Lib_Widget_LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Lib_Widget_LinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Lib_ShapeHelper.initShapeDrawable(this, context, attrs);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_Widget_LinearLayout);
            int dividerInterval = typedArray.getDimensionPixelSize(R.styleable.Lib_Widget_LinearLayout_lib_dividerHeight, -1);
            if (dividerInterval > 0) {
                int divider = typedArray.getColor(R.styleable.Lib_Widget_LinearLayout_lib_divider, Color.GRAY);
                _setDividerDrawable(dividerInterval, divider);
            }
            aspectRatio = typedArray.getFloat(R.styleable.Lib_Widget_LinearLayout_lib_aspectRatio, aspectRatio);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (aspectRatio <= 0 || layoutParams == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        int widthPadding = getPaddingLeft() + getPaddingRight();
        int heightPadding = getPaddingTop() + getPaddingBottom();
        if (layoutParams.height == 0 || layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            int widthSpecSize = View.MeasureSpec.getSize(width);
            int desiredHeight = (int) ((widthSpecSize - widthPadding) / aspectRatio + heightPadding);
            int resolvedHeight = View.resolveSize(desiredHeight, height);
            height = View.MeasureSpec.makeMeasureSpec(resolvedHeight, View.MeasureSpec.EXACTLY);
        } else {
            int heightSpecSize = View.MeasureSpec.getSize(height);
            int desiredWidth = (int) ((heightSpecSize - heightPadding) * aspectRatio + widthPadding);
            int resolvedWidth = View.resolveSize(desiredWidth, width);
            width = View.MeasureSpec.makeMeasureSpec(resolvedWidth, View.MeasureSpec.EXACTLY);
        }
        super.onMeasure(width, height);
    }

    public void _setDividerDrawable(int dividerInterval, @ColorInt int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        if (getOrientation() == LinearLayout.HORIZONTAL) {
            gradientDrawable.setSize(dividerInterval, 0);
        } else {
            gradientDrawable.setSize(0, dividerInterval);
        }
        gradientDrawable.setColor(color);
        setDividerDrawable(gradientDrawable);
        if (getShowDividers() == SHOW_DIVIDER_NONE) {
            setShowDividers(SHOW_DIVIDER_MIDDLE);
        }
    }
}

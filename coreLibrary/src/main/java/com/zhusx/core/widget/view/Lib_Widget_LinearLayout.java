package com.zhusx.core.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhusx.core.R;
import com.zhusx.core.helper.Lib_ShapeHelper;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/30 14:31
 */
public class Lib_Widget_LinearLayout extends LinearLayout {
    private boolean isSquare;//是否正方形

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
        Lib_ShapeHelper.initBackground(this, context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_LinearLayout);
        int dividerInterval = typedArray.getDimensionPixelSize(R.styleable.Lib_LinearLayout_lib_dividerHeight, -1);
        if (dividerInterval > 0) {
            int divider = typedArray.getColor(R.styleable.Lib_LinearLayout_lib_divider, Color.GRAY);
            _setDividerDrawable(dividerInterval, divider);
        }
        typedArray.recycle();

        TypedArray squareArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_ViewGroup);
        isSquare = squareArray.getBoolean(R.styleable.Lib_ViewGroup_lib_isSquare, false);
        squareArray.recycle();
    }

    protected void _setSquare(boolean isSquare) {
        this.isSquare = isSquare;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isSquare) {
            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
            int childWidthSize = getMeasuredWidth();
            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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

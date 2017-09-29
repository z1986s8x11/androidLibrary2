package com.zhusx.core.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zhusx.core.R;
import com.zhusx.core.helper.Lib_ShapeHelper;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/1/29 17:36
 */
public class Lib_Widget_RelativeLayout extends RelativeLayout {
    private boolean isSquare; //是否正方形

    public Lib_Widget_RelativeLayout(Context context) {
        super(context);
        init(context, null);
    }

    public Lib_Widget_RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Lib_Widget_RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void _setSquare(boolean isSquare) {
        this.isSquare = isSquare;
        invalidate();
    }

    private void init(Context context, AttributeSet attrs) {
        Lib_ShapeHelper.initShapeDrawable(this, context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_ViewGroup);
        isSquare = typedArray.getBoolean(R.styleable.Lib_ViewGroup_lib_isSquare, false);
        typedArray.recycle();
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
}

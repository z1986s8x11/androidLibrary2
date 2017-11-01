package com.zhusx.core.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zhusx.core.R;
import com.zhusx.core.helper.Lib_ShapeHelper;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/1/29 17:36
 */
public class Lib_Widget_RelativeLayout extends RelativeLayout {
    private float aspectRatio = 0f; //长宽比

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

    private void init(Context context, AttributeSet attrs) {
        Lib_ShapeHelper.initShapeDrawable(this, context, attrs);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_Widget_RelativeLayout);
            aspectRatio = typedArray.getFloat(R.styleable.Lib_Widget_RelativeLayout_lib_aspectRatio, aspectRatio);
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
}

package com.zhusx.core.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zhusx.core.helper.Lib_ShapeHelper;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/10/31 17:51
 */

public class Lib_Widget_ImageView extends ImageView {
    public Lib_Widget_ImageView(Context context) {
        super(context);
        init(context, null);
    }

    public Lib_Widget_ImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Lib_Widget_ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Lib_Widget_ImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Lib_ShapeHelper.initImageDrawable(this, context, attrs);
    }
}

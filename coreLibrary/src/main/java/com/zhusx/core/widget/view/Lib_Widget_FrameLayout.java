package com.zhusx.core.widget.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zhusx.core.R;
import com.zhusx.core.helper.Lib_ShapeHelper;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/30 14:31
 */
public class Lib_Widget_FrameLayout extends FrameLayout {
    private float aspectRatio = 0f; //长宽比
    private ViewDragHelper mDragHelper;

    public Lib_Widget_FrameLayout(Context context) {
        super(context);
        init(context, null);
    }

    public Lib_Widget_FrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Lib_Widget_FrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Lib_Widget_FrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Lib_ShapeHelper.initShapeDrawable(this, context, attrs);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_Widget_FrameLayout);
            aspectRatio = typedArray.getFloat(R.styleable.Lib_Widget_FrameLayout_lib_aspectRatio, aspectRatio);
            int viewId = typedArray.getResourceId(R.styleable.Lib_Widget_FrameLayout_lib_dragViewId, View.NO_ID);
            if (viewId != View.NO_ID) {
                boolean isAutoKeepSide = typedArray.getBoolean(R.styleable.Lib_Widget_FrameLayout_lib_dragAutoKeepSide, false);
                initDragHelper(viewId, isAutoKeepSide);
            }
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
            int widthSpecSize = MeasureSpec.getSize(width);
            int desiredHeight = (int) ((widthSpecSize - widthPadding) / aspectRatio + heightPadding);
            int resolvedHeight = View.resolveSize(desiredHeight, height);
            height = MeasureSpec.makeMeasureSpec(resolvedHeight, MeasureSpec.EXACTLY);
        } else {
            int heightSpecSize = MeasureSpec.getSize(height);
            int desiredWidth = (int) ((heightSpecSize - heightPadding) * aspectRatio + widthPadding);
            int resolvedWidth = View.resolveSize(desiredWidth, width);
            width = MeasureSpec.makeMeasureSpec(resolvedWidth, MeasureSpec.EXACTLY);
        }
        super.onMeasure(width, height);
    }
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (isSquare) {
//            setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
//            int childWidthSize = getMeasuredWidth();
//            heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
//        }
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    /**
     * @param isAutoKeepSide 自动左右靠边
     */
    private void initDragHelper(final int viewId, final boolean isAutoKeepSide) {
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child.getId() == viewId;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                if (!isAutoKeepSide) {
                    return;
                }
                if (releasedChild.getLeft() * 2 <= getMeasuredWidth()) {
                    mDragHelper.settleCapturedViewAt(getPaddingLeft(), releasedChild.getTop());
                } else {
                    mDragHelper.settleCapturedViewAt(getMeasuredWidth() - releasedChild.getMeasuredWidth() - getPaddingRight(), releasedChild.getTop());
                }
                invalidate();
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();//响应点击事件
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();//响应点击事件
            }

            @Override
            public void onViewDragStateChanged(int state) {
                switch (state) {
                    case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                        break;
                    case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                        break;
                    case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                        break;
                }
                super.onViewDragStateChanged(state);
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                // 保证拖动不越出屏幕
                if (getPaddingLeft() > left) {
                    return getPaddingLeft();
                }
                if (getWidth() - child.getWidth() < left) {
                    return getWidth() - child.getWidth();
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                // 保证拖动不越出屏幕
                if (getPaddingTop() > top) {
                    return getPaddingTop();
                }
                if (getHeight() - child.getHeight() < top) {
                    return getHeight() - child.getHeight();
                }
                return top;
            }
        });
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mDragHelper == null) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_DOWN:
                mDragHelper.cancel();
                break;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (mDragHelper == null) {
            return super.onTouchEvent(event);
        }
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper != null) {
            if (mDragHelper.continueSettling(true)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }
    }
}

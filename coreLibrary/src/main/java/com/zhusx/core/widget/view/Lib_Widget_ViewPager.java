package com.zhusx.core.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zhusx.core.R;
import com.zhusx.core.interfaces.Lib_LifeCycleListener;
import com.zhusx.core.interfaces.Lib_OnCycleListener;

/**
 * 可禁止滑动的ViewPager
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2017/1/4 10:28
 */
public class Lib_Widget_ViewPager extends ViewPager {
    private boolean isScrollable = true;//是否可以滑动
    private boolean isAutoScroll;//是否自动滚动
    private boolean isAllowScroll = true; //是否本父类拦截子类ViewPager等控件滑动事件
    private boolean isRefreshViewParent = false;//父类中是否有可以下拉刷新的View
    private Bitmap mBigBitmap;
    private Paint b;
    private AutoScroll autoScroll;

    public Lib_Widget_ViewPager(Context context) {
        super(context);
    }

    public Lib_Widget_ViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lib_ViewPager);
        isScrollable = typedArray.getBoolean(R.styleable.Lib_ViewPager_lib_scrollable, isScrollable);
        isAutoScroll = typedArray.getBoolean(R.styleable.Lib_ViewPager_lib_autoScroll, isAutoScroll);
        isRefreshViewParent = typedArray.getBoolean(R.styleable.Lib_ViewPager_lib_parentIsRefresh, isRefreshViewParent);
        if (isAutoScroll) {
            int interval = typedArray.getInt(R.styleable.Lib_ViewPager_lib_interval, 5000);
            autoScroll = new AutoScroll(interval);
        }
        typedArray.recycle();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        if (autoScroll != null) {
            autoScroll._stopAutoScroll();
            if (adapter.getCount() > 1) {
                autoScroll._startAutoScroll();
            }
        }
    }

    /**
     * 解决轮播图滑动和下拉刷新滑动冲突问题（左右滑动很容易触发下拉刷新bug）
     */
    public void setParentIsRefreshView(boolean isRefreshViewParent) {
        this.isRefreshViewParent = isRefreshViewParent;
    }

    /**
     * 是否可以滑动
     */
    public void _setScrollable(boolean isScrollable) {
        this.isScrollable = isScrollable;
    }

    /**
     * 是否允许不拦截子类滑动
     */
    public void _setAllowChildScroll(boolean isAllowScroll) {
        this.isAllowScroll = isAllowScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!isAllowScroll) {
            return false;
        }
        if (isScrollable) {
            if (isRefreshViewParent) {
                if (this.getParent() != null && e.getAction() != MotionEvent.ACTION_UP) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
            return super.onTouchEvent(e);
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        super.onTouchEvent(e);
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (!isAllowScroll) {
            return false;
        }
        if (isScrollable) {
            if (isRefreshViewParent) {
                if (this.getParent() != null && e.getAction() != MotionEvent.ACTION_UP) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
            return super.onInterceptTouchEvent(e);
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                super.onInterceptTouchEvent(e);
                return false;
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isScrollable) {
            if (isRefreshViewParent) {
                if (this.getParent() != null && ev.getAction() != MotionEvent.ACTION_UP) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
            }
            if (isAutoScroll) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (autoScroll != null) {
                            autoScroll._stopAutoScroll();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_OUTSIDE:
                        if (autoScroll != null) {
                            autoScroll._startAutoScroll();
                        }
                        break;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private class AutoScroll {
        private int interval;//滑动间隔时间
        private Handler pHandler = new Handler();

        public AutoScroll(int interval) {
            this.interval = interval;
            if (getContext() instanceof Lib_LifeCycleListener) {
                Lib_LifeCycleListener lifeCycle = (Lib_LifeCycleListener) getContext();
                lifeCycle._addOnCycleListener(cycleListener);
            }
        }

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (getAdapter() != null && getAdapter().getCount() > 1) {
                    setCurrentItem((getCurrentItem() + 1) % getAdapter().getCount());
                    pHandler.postDelayed(runnable, interval);
                }
            }
        };

        private Lib_OnCycleListener cycleListener = new Lib_OnCycleListener() {
            @Override
            public void onResume() {
                if (isAutoScroll) {
                    _startAutoScroll();
                }
            }

            @Override
            public void onPause() {
                if (isAutoScroll) {
                    _stopAutoScroll();
                }
            }

            @Override
            public void onDestroy() {
            }
        };

        public void _stopAutoScroll() {
            if (getAdapter() == null) {
                return;
            }
            pHandler.removeCallbacks(runnable);
        }

        public void _startAutoScroll() {
            if (getAdapter() == null) {
                return;
            }
            pHandler.removeCallbacks(runnable);
            if (getAdapter().getCount() > 1) {
                pHandler.postDelayed(runnable, interval);
            }
        }
    }

    /**
     * 设置大图的背景
     */
    public void _setBigBackground(Bitmap bigBitmap) {
        this.mBigBitmap = bigBitmap;
        this.b = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.b.setFilterBitmap(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (this.mBigBitmap != null) {
            PagerAdapter adapter = this.getAdapter();
            if (adapter != null) {
                int count = adapter.getCount();
                int width = this.mBigBitmap.getWidth();
                int height = this.mBigBitmap.getHeight();
                if (count > 0) {
                    int x = getScrollX();
                    //子View中背景图片需要显示的宽度，放大背景图或缩小背景图。
                    int n = height * this.getWidth() / this.getHeight();
                    //(width - n) / (count - 1)表示除去显示第一个ViewPager页面用去的背景宽度，剩余的ViewPager需要显示的背景图片的宽度。
                    //getWidth()等于ViewPager一个页面的宽度，即手机屏幕宽度。在该计算中可以理解为滑动一个ViewPager页面需要滑动的像素值。
                    //((width - n) / (count - 1)) / getWidth()也就表示ViewPager滑动一个像素时，背景图片滑动的宽度。
                    //x * ((width - n) / (count - 1)) / getWidth()也就表示ViewPager滑动x个像素时，背景图片滑动的宽度。
                    //背景图片滑动的宽度的宽度可以理解为背景图片滑动到达的位置。
                    int w = (x + this.getWidth()) * ((width - n) / count) / this.getWidth();
                    canvas.drawBitmap(this.mBigBitmap, new Rect(w, 0, n + w, height), new Rect(x, 0, x + this.getWidth(), this.getHeight()), this.b);
                } else {
                    canvas.drawBitmap(this.mBigBitmap, new Rect(0, 0, width, height), new Rect(0, 0, this.getWidth(), this.getHeight()), this.b);
                }
            }
        }
        super.dispatchDraw(canvas);
    }

    public static class _ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.9f;
        private static final float MIN_ALPHA = 0.5f;
        private float defaultScale = 0.9f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                //view.setAlpha(0);
                view.setScaleX(defaultScale);
                view.setScaleY(defaultScale);
            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }
                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                // Fade the page relative to its size.
                //view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                //view.setAlpha(0);
                view.setScaleX(defaultScale);
                view.setScaleY(defaultScale);
            }
        }
    }

    public static class _DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) { // [-Infinity,-1)
                view.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);
            } else if (position <= 1) { // (0,1]
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0);
            }
        }
    }

    public static class _GradualChangePageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) {
                view.setAlpha(0);
                view.setScaleY(1f);
            } else if (position <= 0) {
                view.setAlpha(1 + position);
                view.setScaleX(1.2f);
                view.setScaleY(1.2f);
                view.setTranslationX(-pageWidth * position * 0.9f);
            } else if (position <= 1) {
                view.setScaleX(1.2f);
                view.setScaleY(1.2f);
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position * 0.9f);
            } else {
                view.setScaleY(1f);
                view.setAlpha(0);
            }
        }
    }
}

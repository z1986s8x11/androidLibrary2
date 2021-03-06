package com.zhusx.core.helper;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhusx.core.interfaces.IHttpResult;
import com.zhusx.core.network.HttpRequest;
import com.zhusx.core.network.OnHttpLoadingListener;


/**
 * 用于需要网络请求时,显示Loading页面
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:20
 */
public abstract class Lib_LoadingHelper<Id, Result, Parameter> implements OnHttpLoadingListener<Id, IHttpResult<Result>, Parameter> {
    private ViewGroup helperLayout;
    private View loadingView;
    private View errorView;
    private boolean isSuccess = false;
    protected View resLayout;
    private Animatable anim;

    public Lib_LoadingHelper(View resLayout) {
        this.resLayout = resLayout;
        ViewParent parent = resLayout.getParent();
        if (parent == null) {
            throw new IllegalStateException("resLayout 不能为null");
        }
        if (parent instanceof ViewGroup) {
            ViewGroup.LayoutParams lp = resLayout.getLayoutParams();
            LinearLayout parentLayout = new LinearLayout(resLayout.getContext());
            parentLayout.setOrientation(LinearLayout.VERTICAL);
            parentLayout.setLayoutParams(lp);
            ViewGroup group = (ViewGroup) parent;
            int index = group.indexOfChild(resLayout);
            group.removeView(resLayout);
            group.addView(parentLayout, index, lp);
            parentLayout.addView(resLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            parentLayout.addView(helperLayout = new FrameLayout(resLayout.getContext()), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            resLayout.setVisibility(View.GONE);
            parentLayout.invalidate();
        } else {
            throw new IllegalStateException("resLayout.getParent() 必须是ViewGroup!!");
        }
    }

    public Lib_LoadingHelper(Activity activity) {
        ViewGroup group = (ViewGroup) activity.getWindow().getDecorView();
        this.resLayout = group.getChildAt(0);
        ViewGroup.LayoutParams lp = resLayout.getLayoutParams();
        LinearLayout parentLayout = new LinearLayout(resLayout.getContext());
        parentLayout.setOrientation(LinearLayout.VERTICAL);
        parentLayout.setLayoutParams(lp);
        int index = group.indexOfChild(resLayout);
        group.removeView(resLayout);
        group.addView(parentLayout, index, lp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            parentLayout.setFitsSystemWindows(true);
        } else {
            //FIXME 兼容 2.3
        }
        parentLayout.addView(resLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        parentLayout.addView(helperLayout = new FrameLayout(resLayout.getContext()), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        resLayout.setVisibility(View.GONE);
        parentLayout.invalidate();
    }

    public final void _setLoadingView(View child) {
        if (loadingView != null) {
            helperLayout.removeView(loadingView);
        }
        if (child != null) {
            if (child.getParent() != null) {
                ((ViewGroup) child.getParent()).removeView(child);
            }
            helperLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setVisibility(View.GONE);
        }
        loadingView = child;
    }

    public final void _setErrorView(View child) {
        if (errorView != null) {
            helperLayout.removeView(errorView);
        }
        if (child != null) {
            if (child.getParent() != null) {
                ((ViewGroup) child.getParent()).removeView(child);
            }
            helperLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            child.setVisibility(View.GONE);
        }
        errorView = child;
    }

    protected void _setLoadingAnim(Animatable animatable) {
        if (anim != null && anim.isRunning()) {
            anim.stop();
        }
        anim = animatable;
    }

    protected void __startLoadingAnim(Id id, HttpRequest<Parameter> request) {
        if (anim != null) {
            anim.start();
        }
    }

    protected void __stopLoadingAnim() {
        if (anim != null) {
            if (anim.isRunning()) {
                anim.stop();
            }
        }
    }

    public void __onError(View errorView, HttpRequest<Parameter> request, IHttpResult<Result> data, boolean isAPIError, String error_message) {
    }

    public abstract void __onComplete(HttpRequest<Parameter> request, IHttpResult<Result> data);

    @Override
    public void onLoadStart(Id id, HttpRequest<Parameter> request) {
        if (request.isRefresh || !isSuccess) {
            if (loadingView != null) {
                loadingView.setVisibility(View.VISIBLE);
                __startLoadingAnim(id, request);
            }
            if (resLayout.getVisibility() == View.VISIBLE) {
                resLayout.setVisibility(View.GONE);
            }
            if (errorView != null && errorView.getVisibility() == View.VISIBLE) {
                errorView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoadError(Id id, HttpRequest<Parameter> request, IHttpResult<Result> data, boolean isAPIError, String error_message) {
        if (request.isRefresh || !isSuccess) {
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
                __stopLoadingAnim();
            }
            if (errorView != null) {
                errorView.setVisibility(View.VISIBLE);
            }
            if (resLayout.getVisibility() == View.VISIBLE) {
                resLayout.setVisibility(View.GONE);
            }
        }
        __onError(errorView, request, data, isAPIError, error_message);
    }

    @Override
    public void onLoadComplete(Id id, HttpRequest<Parameter> request, IHttpResult<Result> data) {
        if (request.isRefresh || !isSuccess) {
            if (loadingView != null) {
                loadingView.setVisibility(View.GONE);
                __stopLoadingAnim();
            }
            if (resLayout.getVisibility() != View.VISIBLE) {
                resLayout.setVisibility(View.VISIBLE);
            }
        }
        isSuccess = true;
        __onComplete(request, data);
    }
}

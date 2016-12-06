package com.zhusx.retrofit2;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/2/16 16:33
 */
//public abstract class LoadingHelper<T> extends LinearLayout implements OnHttpLoadingListener<LoadData.Api, HttpResult<T>, Object> {
//    private ViewGroup helperLayout;
//    private View loadingView;
//    private View errorView;
//    private boolean isSuccess = false;
//    private View resLayout;
//    LoadData<T> loadData;
//    public LoadingHelper(Activity activity, LoadData<T> loadData) {
//        super(activity);
//        this.loadData=loadData;
//        ViewGroup group = (ViewGroup) activity.getWindow().getDecorView();
//        this.resLayout = group.getChildAt(0);
//        ViewGroup.LayoutParams lp = resLayout.getLayoutParams();
//        setOrientation(LinearLayout.VERTICAL);
//        setLayoutParams(lp);
//        int index = group.indexOfChild(resLayout);
//        group.removeView(resLayout);
//        group.addView(this, index, lp);
//        addView(resLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        addView(helperLayout = new FrameLayout(resLayout.getContext()), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        resLayout.setVisibility(View.GONE);
//        _setLoadingView(LayoutInflater.from(activity).inflate(R.layout.layout_loading_default, null, false));
//        _setErrorView(LayoutInflater.from(activity).inflate(R.layout.layout_loading_error, null, false));
//        invalidate();
//    }
//
//    public final void _setLoadingView(View child) {
//        if (loadingView != null) {
//            helperLayout.removeView(loadingView);
//        }
//        if (child.getParent() != null) {
//            ((ViewGroup) child.getParent()).removeView(child);
//        }
//        helperLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        child.setVisibility(View.GONE);
//        loadingView = child;
//    }
//
//    public final void _setErrorView(View child) {
//        if (errorView != null) {
//            helperLayout.removeView(errorView);
//        }
//        if (child.getParent() != null) {
//            ((ViewGroup) child.getParent()).removeView(child);
//        }
//        helperLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        child.setVisibility(View.GONE);
//        errorView = child;
//        errorView.findViewById(R.id.btn_error).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadData._reLoadData(true);
//            }
//        });
//    }
//
//    public void __onError(HttpRequest<Object> request, HttpResult<T> data, boolean isAPIError, String error_message) {
//    }
//
//    public abstract void __onComplete(HttpRequest<Object> request, HttpResult<T> data);
//
//    @Override
//    public void onLoadStart(LoadData.Api api, HttpRequest<Object> request) {
//        if (request.isRefresh || !isSuccess) {
//            if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
//                loadingView.setVisibility(View.VISIBLE);
//            }
//            if (errorView != null && errorView.getVisibility() != View.GONE) {
//                errorView.setVisibility(View.GONE);
//            }
//            if (resLayout.getVisibility() == View.VISIBLE) {
//                resLayout.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    @Override
//    public void onLoadError(LoadData.Api api, HttpRequest<Object> request, HttpResult<T> tHttpResult, boolean isAPIError, String error_message) {
//        if (request.isRefresh || !isSuccess) {
//            if (loadingView != null && loadingView.getVisibility() != View.GONE) {
//                loadingView.setVisibility(View.GONE);
//            }
//            if (errorView != null && errorView.getVisibility() != View.VISIBLE) {
//                errorView.setVisibility(View.VISIBLE);
//            }
//        }
//        __onError(request, tHttpResult, isAPIError, error_message);
//    }
//
//    @Override
//    public void onLoadComplete(LoadData.Api api, HttpRequest<Object> request, HttpResult<T> data) {
//        if (request.isRefresh || !isSuccess) {
//            if (loadingView != null) {
//                loadingView.setVisibility(View.GONE);
//            }
//            if (errorView != null && errorView.getVisibility() != View.GONE) {
//                errorView.setVisibility(View.GONE);
//            }
//            resLayout.setVisibility(View.VISIBLE);
//        }
//        isSuccess = true;
//        __onComplete(request, data);
//    }
//}

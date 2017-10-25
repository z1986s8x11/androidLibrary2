package com.zhusx.retrofit2;


import android.text.TextUtils;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.interfaces.IPageData;
import com.zhusx.core.interfaces.Lib_LoadingListener;
import com.zhusx.core.network.HttpRequest;
import com.zhusx.core.network.HttpResult;
import com.zhusx.core.network.OnHttpLoadingListener;

import java.io.IOException;
import java.net.SocketException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Retrofit 请求基类
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/9/27 9:27
 */

public abstract class Lib_BaseRetrofitLoadData<Id, Result, Parameter, Transform> implements Lib_LoadingListener {
    private Id pId;
    private HttpResult<Result> pBean;
    private boolean pIsDownding = false;
    private OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> listener;
    private HttpRequest<Parameter> pLastRequestData;
    private CompositeDisposable mCompositeSubscription;
    int currentPage = -1;

    public HttpRequest<Parameter> _getRequestParams() {
        return this.pLastRequestData != null ? this.pLastRequestData : null;
    }

    public void _setOnLoadingListener(OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> listener) {
        this.listener = listener;
    }

    public Lib_BaseRetrofitLoadData(Id id) {
        this.pId = id;
        this.mCompositeSubscription = new CompositeDisposable();
    }

    public Id _getRequestID() {
        return this.pId;
    }

    public void _loadData(Parameter... objects) {
        this.requestData(false, objects);
    }

    public void _refreshData(Parameter... objects) {
        this.requestData(true, objects);
    }


    public void _reLoadData() {
        if (this.pLastRequestData != null) {
            this._reLoadData(this.pLastRequestData.isRefresh);
        } else if (LogUtil.DEBUG) {
            LogUtil.e("requestData(Objects... objs) 从未主动加载过数据 不能直接刷新");
        }
    }

    @Override
    public void _reLoadData(boolean isRefresh) {
        if (this.pLastRequestData != null) {
            this.requestData(isRefresh, this.pLastRequestData.lastObjectsParams);
        } else if (LogUtil.DEBUG) {
            LogUtil.e("requestData(Objects... objs) 从未主动加载过数据 不能直接刷新");
        }
    }

    @Override
    public boolean _isLoading() {
        return this.pIsDownding;
    }

    @Override
    public boolean _hasCache() {
        return this.pBean != null;
    }

    public void _clearData() {
        this.pBean = null;
    }

    public HttpResult<Result> _getLastData() {
        return this.pBean;
    }

    public void _cancelLoadData() {
        if (mCompositeSubscription != null && mCompositeSubscription.isDisposed()) {
            mCompositeSubscription.dispose();
            mCompositeSubscription = null;
        }
        pIsDownding = false;
    }

    private synchronized void requestData(boolean isRefresh, Parameter... objects) {
        if (this.pIsDownding) {
            if (LogUtil.DEBUG) {
                LogUtil.e("id:" + this.pId + "\t 上一个请求正在进行时");
            }
            return;
        }
        if (this.pLastRequestData == null) {
            this.pLastRequestData = new HttpRequest<>();
        }
        this.pLastRequestData.lastObjectsParams = objects;
        this.pLastRequestData.isRefresh = isRefresh;
        __requestProtocol(pId, getHttpParams(pId, objects));
    }

    public <T> T cast(Object o) {
        return (T) o;
    }

    protected void __requestProtocol(final Id id, Observable<Transform> observable) {
        if (observable == null) {
            throw new NullPointerException("getHttpParams(pId, objects) at" + String.valueOf(pId) + " = null");
        }
        pIsDownding = true;
        if (listener != null) {
            listener.onLoadStart(id, pLastRequestData);
        }
        __onStart(id, pLastRequestData);
        DisposableObserver observer = new DisposableObserver<Transform>() {

            @Override
            public void onError(Throwable e) {
                if (LogUtil.DEBUG) {
                    LogUtil.e(e);
                }
                String errorMessage = __parseErrorMessage(e);
                pIsDownding = false;
                if (listener != null) {
                    listener.onLoadError(id, pLastRequestData, null, false, errorMessage);
                }
                __onError(id, pLastRequestData, null, false, errorMessage);
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onNext(Transform data) {
                pIsDownding = false;
                pBean = switchResult(data);
                if (pBean.isSuccess()) {
                    if (pLastRequestData.isRefresh) {
                        currentPage = getDefaultPage();
                    } else {
                        if (currentPage == -1) {
                            currentPage = getDefaultPage();
                        } else {
                            currentPage++;
                        }
                    }
                    //TODO pBean.setCurrentDataIndex(currentPage);
                    if (listener != null) {
                        listener.onLoadComplete(id, pLastRequestData, pBean);
                    }
                    __onComplete(id, pLastRequestData, pBean);
                } else {
                    if (listener != null) {
                        listener.onLoadError(id, pLastRequestData, null, false, pBean.getMessage());
                    }
                    __onError(id, pLastRequestData, null, false, pBean.getMessage());
                }
            }
        };
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        this.mCompositeSubscription.add(observer);
    }

    protected String __parseErrorMessage(Throwable e) {
        String errorMessage = null;
        if (e instanceof retrofit2.HttpException) {
            Response<?> response = ((retrofit2.HttpException) e).response();
            if (response != null) {
                ResponseBody responseBody = response.errorBody();
                try {
                    errorMessage = responseBody.string();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        } else if (e instanceof SocketException) {
            errorMessage = "网络错误，请检查网络";
        }
        if (TextUtils.isEmpty(errorMessage)) {
            errorMessage = "服务繁忙,请稍后重试";
        }
        return errorMessage;
    }

    protected abstract Observable<Transform> getHttpParams(Id var1, Parameter... var2);

    protected abstract HttpResult<Result> switchResult(Transform data);

    @Override
    public int _getNextPage() {
        if (currentPage == -1) {
            return getDefaultPage();
        }
        if (pLastRequestData.isRefresh) {
            return getDefaultPage();
        }
        return currentPage + 1;
    }

    protected int getDefaultPage() {
        return 1;
    }


    protected void __onStart(Id id, HttpRequest<Parameter> request) {
    }

    protected void __onError(Id id, HttpRequest<Parameter> request, HttpResult<Result> result, boolean var4, String errorMessage) {
    }

    protected void __onComplete(Id id, HttpRequest<Parameter> request, HttpResult<Result> result) {
    }

    @Override
    public boolean hasMoreData() {
        if (_isLoading()) {
            return false;
        } else if (!_hasCache()) {
            return true;
        } else {
            HttpResult result = _getLastData();
            if (result.getData() instanceof IPageData) {
                IPageData impl = (IPageData) result.getData();
                return impl.getTotalPageCount() > 0 && impl.getTotalPageCount() >= (currentPage + 1);
            } else {
                if (LogUtil.DEBUG) {
                    LogUtil.e(this._getRequestID() + "T 必须实现 IPageData 接口");
                }
                return true;
            }
        }
    }
}

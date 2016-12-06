package com.zhusx.retrofit2;


import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.network.HttpRequest;
import com.zhusx.core.network.OnHttpLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/9/27 9:27
 */

public abstract class BaseLoadData<Id, Result, Parameter> {
    private Id pId;
    private Result pBean;
    private boolean pIsDownding = false;
    private OnHttpLoadingListener<Id, Result, Parameter> listener;
    private HttpRequest<Parameter> pLastRequestData;
    private CompositeSubscription mCompositeSubscription;
    int currentPage = -1;

    public HttpRequest<Parameter> _getRequestParams() {
        return this.pLastRequestData != null ? this.pLastRequestData : null;
    }

    public void _setOnLoadingListener(OnHttpLoadingListener<Id, Result, Parameter> listener) {
        this.listener = listener;
    }

    public BaseLoadData(Id id) {
        this.pId = id;
        this.mCompositeSubscription = new CompositeSubscription();
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
            LogUtil.e(this, "requestData(Objects... objs) 从未主动加载过数据 不能直接刷新");
        }
    }

    public void _reLoadData(boolean isRefresh) {
        if (this.pLastRequestData != null) {
            this.requestData(isRefresh, this.pLastRequestData.lastObjectsParams);
        } else if (LogUtil.DEBUG) {
            LogUtil.e(this, "requestData(Objects... objs) 从未主动加载过数据 不能直接刷新");
        }
    }

    public boolean _isLoading() {
        return this.pIsDownding;
    }

    public boolean _hasCache() {
        return this.pBean != null;
    }

    public void _clearData() {
        this.pBean = null;
    }

    public Result _getLastData() {
        return this.pBean;
    }

    public void _cancelLoadData() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
            mCompositeSubscription = null;
        }
        pIsDownding = false;
    }

    private synchronized void requestData(boolean isRefresh, Parameter... objects) {
        if (this.pIsDownding) {
            if (LogUtil.DEBUG) {
                LogUtil.e(this, "id:" + this.pId + "\t 上一个请求正在进行时");
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

    protected String __requestProtocol(final Id id, Observable<Result> observable) {
        if (observable == null) {
            throw new NullPointerException("getHttpParams(pId, objects) at" + String.valueOf(pId) + " = null");
        }
        pIsDownding = true;
        if (listener != null) {
            listener.onLoadStart(id, pLastRequestData);
        }
        onLoadStart(id, pLastRequestData);
        this.mCompositeSubscription.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Result>() {
                            @Override
                            public void onCompleted() {
                                pIsDownding = false;
                            }

                            @Override
                            public void onError(Throwable e) {
                                LogUtil.e(e);
                                String errorMessage = "服务繁忙,请稍后重试";
                                if (e instanceof HttpException) {
                                    Response<?> response = ((HttpException) e).response();
                                    if (response != null) {
                                        ResponseBody responseBody = response.errorBody();
                                        if (responseBody != null) {
                                            try {
                                                JSONObject json = new JSONObject(responseBody.string());
                                                errorMessage = json.getString("message");
                                            } catch (JSONException e1) {
                                                e1.printStackTrace();
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    }
                                } else if (e instanceof SocketException) {
                                    errorMessage = "网络错误，请检查网络";
                                }
                                pIsDownding = false;
                                if (listener != null) {
                                    listener.onLoadError(id, pLastRequestData, null, false, errorMessage);
                                }
                                onLoadError(id, pLastRequestData, null, false, errorMessage);
                            }

                            @Override
                            public void onNext(Result data) {
                                if (isSuccess(id, data)) {
                                    pBean = data;
                                    if (pLastRequestData.isRefresh) {
                                        currentPage = getDefaultPage();
                                    } else {
                                        if (currentPage == -1) {
                                            currentPage = getDefaultPage();
                                        } else {
                                            currentPage++;
                                        }
                                    }
                                    if (listener != null) {
                                        listener.onLoadComplete(id, pLastRequestData, data);
                                    }
                                    onLoadComplete(id, pLastRequestData, data);
                                } else {
                                    if (listener != null) {
                                        listener.onLoadError(id, pLastRequestData, null, false, getMessage(id, data));
                                    }
                                    onLoadError(id, pLastRequestData, null, false, getMessage(id, data));
                                }
                            }
                        }));
        return null;
    }

    protected abstract Observable<Result> getHttpParams(Id var1, Parameter... var2);

    protected int getNextPage() {
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

    protected abstract boolean isSuccess(Id id, Result data);

    protected abstract String getMessage(Id id, Result data);

    protected void onLoadStart(Id id, HttpRequest<Parameter> request) {
    }

    protected void onLoadError(Id id, HttpRequest<Parameter> request, Result result, boolean var4, String errorMessage) {
    }

    protected void onLoadComplete(Id id, HttpRequest<Parameter> request, Result result) {
    }
}

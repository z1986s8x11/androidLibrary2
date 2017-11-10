package com.zhusx.core.network;

import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.interfaces.IHttpResult;
import com.zhusx.core.interfaces.IPageData;
import com.zhusx.core.interfaces.Lib_LoadingListener;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/11/10 11:25
 */

public abstract class Lib_BaseRequestData<Id, Result, Parameter, Transform extends IHttpResult<Result>> implements Lib_LoadingListener {
    private Id pId;
    private IHttpResult<Result> pBean;
    private boolean pIsDownding = false;
    private OnHttpLoadingListener<Id, IHttpResult<Result>, Parameter> listener;
    private HttpRequest<Parameter> pLastRequestData;
    int currentPage = -1;

    public HttpRequest<Parameter> _getRequestParams() {
        return pLastRequestData;
    }

    public void _setOnLoadingListener(OnHttpLoadingListener<Id, IHttpResult<Result>, Parameter> listener) {
        this.listener = listener;
    }

    public Lib_BaseRequestData(Id id) {
        this.pId = id;
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

    public IHttpResult<Result> _getLastData() {
        return this.pBean;
    }

    @CallSuper
    public void _cancelLoadData() {
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
        __requestProtocol(pId, objects);
    }

    protected abstract void __requestProtocol(Id id, Parameter... parameters);

    @MainThread
    protected final void _onStart() {
        pIsDownding = true;
        if (listener != null) {
            listener.onLoadStart(pId, pLastRequestData);
        }
        __onStart(pId, pLastRequestData);
    }

    @MainThread
    protected final void _onError(String errorMessage) {
        pIsDownding = false;
        if (listener != null) {
            listener.onLoadError(pId, pLastRequestData, null, false, errorMessage);
        }
        __onError(pId, pLastRequestData, null, false, errorMessage);
    }

    @MainThread
    protected final void _onComplete(Transform data) {
        pIsDownding = false;
        if (data.isSuccess()) {
            if (pLastRequestData.isRefresh) {
                currentPage = __getDefaultPage();
            } else {
                if (currentPage == -1) {
                    currentPage = __getDefaultPage();
                } else {
                    currentPage++;
                }
            }
            if (listener != null) {
                listener.onLoadComplete(pId, pLastRequestData, data);
            }
            __onComplete(pId, pLastRequestData, data);
            pBean = data;
        } else {
            if (listener != null) {
                listener.onLoadError(pId, pLastRequestData, null, false, data.getMessage());
            }
            __onError(pId, pLastRequestData, null, false, data.getMessage());
        }
    }

    /**
     * 重写, 用于监听  _onStart(id) 被调用后
     */
    protected abstract void __onStart(Id id, HttpRequest<Parameter> request);

    /**
     * 重写, 用于监听  _onError(id) 被调用后
     */
    protected abstract void __onError(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result, boolean isAPIError, String errorMessage);

    /**
     * 重写, 用于监听  _onComplete(id) 被调用后
     */
    protected abstract void __onComplete(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result);

    protected int __getDefaultPage() {
        return 1;
    }

    @Override
    public int _getNextPage() {
        if (currentPage == -1) {
            return __getDefaultPage();
        }
        if (pLastRequestData.isRefresh) {
            return __getDefaultPage();
        }
        return currentPage + 1;
    }

    @Override
    public boolean hasMoreData() {
        if (_isLoading()) {
            return false;
        } else if (!_hasCache()) {
            return true;
        } else {
            IHttpResult result = _getLastData();
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

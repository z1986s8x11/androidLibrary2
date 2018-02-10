package com.zhusx.core.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.interfaces.IHttpResult;
import com.zhusx.core.interfaces.IPageData;
import com.zhusx.core.interfaces.Lib_LoadingListener;
import com.zhusx.core.manager.ZsxApplicationManager;
import com.zhusx.core.utils._HttpURLRequests;
import com.zhusx.core.utils._Networks;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Parameter loadData 参数类型
 * Result    返回参数
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2017/1/4 10:18
 */
public abstract class Lib_BaseHttpRequestData<Id, Result, Parameter> implements Lib_LoadingListener {
    private Id pId;
    private HttpResult<Result> pBean;
    private boolean pIsDownloading = false;
    private Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> pListeners = new LinkedHashSet<>();
    private HttpRequest<Parameter> pLastRequestData;
    int currentPage = -1;

    private HttpWork pWorkThread;
    private Handler pHandler = new Handler(Looper.getMainLooper());

    public HttpRequest<Parameter> _getRequestParams() {
        return pLastRequestData;
    }

    public void _setOnLoadingListener(OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> listener) {
        pListeners.clear();
        pListeners.add(listener);
    }

    public void _addOnLoadingListener(OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> listener) {
        pListeners.add(listener);
    }

    public Lib_BaseHttpRequestData(Id id) {
        this.pId = id;
    }

    public Id _getRequestID() {
        return pId;
    }

    public void _loadData(Parameter... objects) {
        requestData(false, objects);
    }

    public void _refreshData(Parameter... objects) {
        requestData(true, objects);
    }

    public void _reLoadData() {
        if (pLastRequestData != null) {
            _reLoadData(pLastRequestData.isRefresh);
        } else {
            if (LogUtil.DEBUG) {
                LogUtil.e("requestData(Objects... objs) 从未主动加载过数据 不能直接刷新");
            }
        }
    }

    @Override
    public void _reLoadData(boolean isRefresh) {
        if (pLastRequestData != null) {
            requestData(isRefresh, pLastRequestData.lastObjectsParams);
        } else {
            if (LogUtil.DEBUG) {
                LogUtil.e("requestData(Objects... objs) 从未主动加载过数据 不能直接刷新");
            }
        }
    }

    @Override
    public boolean _isLoading() {
        return pIsDownloading;
    }

    @Override
    public boolean _hasCache() {
        return pBean != null;
    }

    public void _clearData() {
        pBean = null;
    }

    public HttpResult<Result> _getLastData() {
        return pBean;
    }

    /**
     * 不成功
     */
    public void _cancelLoadData() {
        if (pIsDownloading) {
            pIsDownloading = false;
            if (pWorkThread != null) {
                pWorkThread.cancel();
            }
        }
    }

    private synchronized void requestData(boolean isRefresh, Parameter... objects) {
        if (pIsDownloading) {
            if (LogUtil.DEBUG) {
                LogUtil.e("id:" + pId + "\t 正在请求");
            }
            return;
        }
        if (pLastRequestData == null) {
            pLastRequestData = new HttpRequest<>();
        }
        pLastRequestData.lastObjectsParams = objects;
        pLastRequestData.isRefresh = isRefresh;
        if (ZsxApplicationManager._Current_NetWork_Status == _Networks.NetType.NoneNet) {
            if (LogUtil.DEBUG) {
                LogUtil.e("网络异常,请检查网络设置!" + pId);
            }
            onRequestStart(pListeners, pLastRequestData);
            onRequestError(null, false, "网络异常,请检查网络设置!", pListeners);
            return;
        }
        HttpParams pParams = getHttpParams(pId, objects);
        onRequestStart(pListeners, pLastRequestData);
        executeWork(pParams);
    }

    protected void executeWork(HttpParams pParams) {
        pWorkThread = new HttpWork(pParams, pListeners);
        pWorkThread.start();
    }

    private class HttpWork extends Thread {
        private HttpParams mParams;
        private Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> mListeners;

        public HttpWork(HttpParams params, Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> listeners) {
            this.mParams = params;
            this.mListeners = listeners;
        }

        public void cancel() {
            this.mParams.isCancel = true;
            this.mListeners.clear();
        }


        private void onPostError(@Nullable final HttpResult<Result> result, final boolean isApiError, final String error_message, final Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> listeners) {
            if (this.mParams.isCancel) {
                pIsDownloading = false;
                return;
            }
            pHandler.post(new Runnable() {

                @Override
                public void run() {
                    onRequestError(result, isApiError, error_message, listeners);
                }
            });
        }

        private void onPostComplete(final HttpResult<Result> bean, final Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> listeners) {
            if (this.mParams.isCancel) {
                pIsDownloading = false;
                return;
            }
            pHandler.post(new Runnable() {

                @Override
                public void run() {
                    onRequestComplete(bean, listeners);
                }
            });
        }

        @Override
        public void run() {
            String returnStr = null;
            HttpReturn http = new HttpReturn();
            try {
                returnStr = __requestProtocol(pId, mParams);
            } catch (HttpException e) {
                http.code = e._getErrorCode();
                if (http.code == HttpException.ERROR_CODE_CANCEL) {
                    http.message = e._getErrorMessage(); //主动取消 上传or下载
                } else if (http.code > HttpURLConnection.HTTP_OK) {
                    try {
                        __parseReadHttpCodeError(pId, http, e._getErrorMessage());
                    } catch (Exception ee) {
                        http.message = e._getErrorMessage();
                    }
                } else {
                    http.message = e._getErrorMessage();
                }
            } catch (ClassCastException e) {
                LogUtil.e(e);
                http.message = "参数类型错误";
            } catch (URISyntaxException e) {
                LogUtil.w(e);
                http.message = "网络地址错误";
            } catch (SocketTimeoutException e) {
                http.message = "请求超时";
                LogUtil.w(e);
            } catch (IOException e) {
                http.message = "发生未知异常";
                LogUtil.e(e);
            } catch (Exception e) {
                http.message = "发生未知异常";
                LogUtil.e(e);
            }
            if (http.message != null) {
                HttpResult<Result> xx = null;
                if (http.code != 0) {
                    xx = new HttpResult<>();
                    xx.setSuccess(false);
                    xx.setMessage(http.message);
                    xx.setErrorCode(http.code);
                }
                onPostError(xx, false, http.message, mListeners);
                return;
            }
            boolean isError = false;
            HttpResult<Result> bean = null;
            try {
                bean = parseStr(pId, returnStr, pBean);
            } catch (Exception e) {
                isError = true;
                if (LogUtil.DEBUG) {
                    LogUtil.e(e);
                }
                onPostError(bean, false, "解析异常", mListeners);
            }
            if (!isError) {
                if (bean.isSuccess()) {
                    if (pLastRequestData.isRefresh) {
                        currentPage = __getDefaultPage(pId);
                    } else {
                        if (currentPage == -1) {
                            currentPage = __getDefaultPage(pId);
                        } else {
                            currentPage++;
                        }
                    }
                    onPostComplete(bean, mListeners);
                } else {
                    onPostError(bean, true, bean.getMessage(), mListeners);
                }
                pBean = bean;
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected String __requestProtocol(final Id id, final HttpParams params)
            throws URISyntaxException, IOException,
            HttpException {
        String str = null;
        Object paramObject = params.getParams();
        switch (params.getRequestMethod()) {
            case HttpParams.GET:
                String getUrl = null;
                if (paramObject == null) {
                    getUrl = params.getRequestUrl();
                } else {
                    if (paramObject instanceof Map) {
                        Map<String, Object> param = (Map<String, Object>) paramObject;
                        getUrl = _HttpURLRequests.encodeUrl(params.getRequestUrl(), param);
                    } else {
                        getUrl = params.getRequestUrl() + String.valueOf(paramObject);
                    }
                }
                str = _HttpURLRequests.httpRequest(params.getRequestMethod(), getUrl, null, null, params.getHttpHead(), params.isReadHttpCodeErrorMessage());
                break;
            case HttpParams.POST:
            case HttpParams.PUT:
            case HttpParams.DELETE:
                if (paramObject instanceof Map) {
                    str = _HttpURLRequests.httpRequest(params.getRequestMethod(), params.getRequestUrl(), (Map<String, Object>) paramObject, params.getHttpHead(), params.isReadHttpCodeErrorMessage());
                } else {
                    str = _HttpURLRequests.httpRequest(params.getRequestMethod(), params.getRequestUrl(), paramObject == null ? "" : paramObject.toString(), params.getHttpHead(), params.isReadHttpCodeErrorMessage());
                }
                break;
            case HttpParams.UPLOAD:
                if (paramObject instanceof Map) {
                    Map<String, Objects> files = (Map<String, Objects>) paramObject;
                    for (String key : files.keySet()) {
                        final String filePath = String.valueOf(files.get(key));
                        str = _HttpURLRequests.uploadFile(new File(filePath), params.getRequestUrl(), null, params.getHttpHead(), new _HttpURLRequests.OnProgressListener() {
                            @Override
                            public void onProgress(int progress, int currentSize, int totalSize) {
                                onRequestProgress(id, filePath, progress, currentSize, totalSize);
                            }

                            @Override
                            public boolean isCanceled() {
                                return params.isCancel;
                            }
                        });
                    }
                } else {
                    str = _HttpURLRequests.uploadFile(new File((String) params.getParams()), params.getRequestUrl(), null, params.getHttpHead(), new _HttpURLRequests.OnProgressListener() {
                        @Override
                        public void onProgress(int progress, int currentSize, int totalSize) {
                            onRequestProgress(id, (String) params.getParams(), progress, currentSize, totalSize);
                        }

                        @Override
                        public boolean isCanceled() {
                            return params.isCancel;
                        }
                    });
                }
                break;
            case HttpParams.DOWNLOAD:
                _HttpURLRequests.downloadFile(params.getRequestUrl(), (String) params.getParam(), new _HttpURLRequests.OnProgressListener() {
                    @Override
                    public void onProgress(int progress, int currentSize, int totalSize) {
                        onRequestProgress(id, params.getRequestUrl(), progress, currentSize, totalSize);
                    }

                    @Override
                    public boolean isCanceled() {
                        return params.isCancel;
                    }
                });
                str = String.valueOf(params.getParam());
                break;
            default:
                throw new IllegalArgumentException("没有此请求方法");
        }
        return str;
    }

    private void onRequestProgress(final Id id, final String url, final int progress, final int currentSize, final int totalSize) {
        pHandler.post(new Runnable() {
            @Override
            public void run() {
                __onLoadProgress(id, url, progress, currentSize, totalSize);
            }
        });
    }

    /**
     * 上传下载进度
     */
    protected void __onLoadProgress(Id id, String url, int progress, int currentSize, int totalSize) {
    }

    protected final void onRequestStart(Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> listeners, HttpRequest<Parameter> request) {
        pIsDownloading = true;
        __onStart(pId, request);
        for (OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> listener : listeners) {
            if (listener != null) {
                listener.onLoadStart(pId, request);
            }
        }
    }

    protected final void onRequestError(@Nullable HttpResult<Result> result, boolean isApiError, String error_message, Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> listeners) {
        pIsDownloading = false;
        __onError(pId, pLastRequestData, result, isApiError, error_message);
        for (OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> listener : listeners) {
            if (listener != null) {
                listener.onLoadError(pId, pLastRequestData, result, isApiError, error_message);
            }
        }
    }

    protected final void onRequestComplete(HttpResult<Result> bean, Set<OnHttpLoadingListener<Id, HttpResult<Result>, Parameter>> listeners) {
        pIsDownloading = false;
        __onComplete(pId, pLastRequestData, bean);
        for (OnHttpLoadingListener<Id, HttpResult<Result>, Parameter> listener : listeners) {
            if (listener != null) {
                listener.onLoadComplete(pId, pLastRequestData, bean);
            }
        }
    }

    /**
     * 构造请求参数
     */
    protected abstract HttpParams getHttpParams(Id id, Parameter... objects);

    /**
     * @param currentDownloadText Http请求的数据
     * @param lastData            上一次 parseStr()方法返回的数据
     * @return 会在onComplete()中回调出去
     */
    protected abstract HttpResult<Result> parseStr(Id id,
                                                   String currentDownloadText,
                                                   @Nullable HttpResult<Result> lastData) throws Exception;

    /**
     * 解析HttpCode 不等于 200 的错误信息
     * httpCode  大于 200  且 不等于HttpException.ERROR_CODE_CANCEL 为http错误码
     */
    protected void __parseReadHttpCodeError(Id id, HttpReturn http, String errorMessage) throws Exception {
        http.message = errorMessage;
    }


    protected void __onStart(Id id, HttpRequest<Parameter> request) {
    }

    protected void __onError(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result, boolean var4, String errorMessage) {
    }

    protected void __onComplete(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result) {
    }

    /**
     * 拿到下一页 页码
     */
    @Override
    public final int _getNextPage() {
        if (currentPage == -1) {
            return __getDefaultPage(pId);
        }
        if (pLastRequestData.isRefresh || !_hasCache()) {
            return __getDefaultPage(pId);
        }
        return currentPage + 1;
    }

    protected int __getDefaultPage(Id id) {
        return 1;
    }

    @Override
    public boolean hasMoreData() {
        if (_isLoading()) {
            return false;
        }
        if (!_hasCache()) {
            return true;
        }
        HttpResult<Result> result = _getLastData();
        if (result.getData() instanceof IPageData) {
            IPageData impl = (IPageData) result.getData();
            if (impl.getTotalPageCount() > 0 && impl.getTotalPageCount() >= (currentPage + 1)) {
                return true;
            }
            return false;
        } else {
            if (LogUtil.DEBUG) {
                LogUtil.e(String.valueOf(_getRequestID()) + "T 必须实现 IPageData 接口");
            }
        }
        return true;
    }
}

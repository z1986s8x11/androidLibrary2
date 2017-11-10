package com.zhusx.retrofit2;

import com.zhusx.core.app.Lib_BaseActivity;
import com.zhusx.core.helper.Lib_Subscribes;
import com.zhusx.core.interfaces.IHttpResult;
import com.zhusx.core.network.HttpRequest;
import com.zhusx.core.network.Lib_BaseRequestData;
import com.zhusx.core.network.Request;
import com.zhusx.core.network.Response;
import com.zhusx.core.utils._Requests;

import java.net.SocketTimeoutException;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/11/10 11:57
 */

public class LoadData2<Id, Result, Parameter, Transform extends IHttpResult<Result>> extends Lib_BaseRequestData<Id, Result, Parameter, Transform> {
    Lib_BaseActivity activity;

    public LoadData2(Id id) {
        super(id);
    }

    @Override
    protected void __requestProtocol(final Id id, Parameter[] parameters) {
        _onStart();
        Lib_Subscribes.subscribe(new Lib_Subscribes.Subscriber<Transform>() {
            @Override
            public Transform doInBackground() throws Exception {
                Response response = _Requests.request(Request.create().setUrl("http://www.baidu.com").setMethod(Request.GET).build());

                return null;
            }

            @Override
            public void onComplete(Transform result) {
                _onComplete(result);
            }

            @Override
            public void onError(Throwable t) {
                _onError(getErrorMessage(t));
            }
        }, activity);
    }

    @Override
    protected void __onStart(Id id, HttpRequest<Parameter> request) {
    }

    @Override
    protected void __onError(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result, boolean isAPIError, String errorMessage) {
    }

    @Override
    protected void __onComplete(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result) {
    }


    public String getErrorMessage(Throwable t) {
        String err;
        if (t instanceof ClassCastException) {
            err = "参数类型错误";
        } else if (t instanceof SocketTimeoutException) {
            err = "请求超时";
        } else {
            err = "发生未知异常";
        }
        return err;
    }
}

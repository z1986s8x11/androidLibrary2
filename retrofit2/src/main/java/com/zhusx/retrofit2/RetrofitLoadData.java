package com.zhusx.retrofit2;

import com.zhusx.core.network.HttpResult;

import rx.Observable;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/7/25 13:58
 */

public class RetrofitLoadData<Result> extends BaseRetrofitLoadData<Integer, Result, Object, JSONResult<Result>> {

    public RetrofitLoadData(Integer integer) {
        super(integer);
    }

    @Override
    protected Observable<JSONResult<Result>> getHttpParams(Integer var1, Object... var2) {
        return null;
    }

    @Override
    protected HttpResult<Result> switchResult(JSONResult<Result> data) {
        HttpResult<Result> result = new HttpResult<>();
        result.setSuccess(true);
        result.setData(data.data);
        result.setMessage(data.message);
        return result;
    }
}

package com.zhusx.retrofit2;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/11/10 11:26
 */

//public class LoadData<Id, Result, Parameter, Transform extends IHttpResult<Result>> extends Lib_BaseLoadData<Id, Result, Parameter, Transform> {
//    CompositeSubscription mCompositeSubscription;
//
//    public LoadData(Id id) {
//        super(id);
//        this.mCompositeSubscription = new CompositeSubscription();
//    }
//
//    @Override
//    public void _cancelLoadData() {
//        super._cancelLoadData();
//        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
//            mCompositeSubscription.unsubscribe();
//            mCompositeSubscription = null;
//        }
//    }
//
//    @Override
//    protected void __requestProtocol(final Id id, Parameter[] parameters) {
//        Observable<Transform> observable = null;
//        _onStart();
//        this.mCompositeSubscription.add(
//                observable
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<Transform>() {
//                            @Override
//                            public void onCompleted() {
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                if (LogUtil.DEBUG) {
//                                    LogUtil.e(e);
//                                }
//                                String errorMessage = __parseErrorMessage(e);
//                                _onError(errorMessage);
//                            }
//
//                            @Override
//                            public void onNext(Transform data) {
//                                _onComplete(data);
//                            }
//                        }));
//    }
//
//    @Override
//    protected void __onStart(Id id, HttpRequest<Parameter> request) {
//    }
//
//    @Override
//    protected void __onError(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result, boolean isAPIError, String errorMessage) {
//    }
//
//    @Override
//    protected void __onComplete(Id id, HttpRequest<Parameter> request, IHttpResult<Result> result) {
//    }
//
//    protected String __parseErrorMessage(Throwable e) {
//        String errorMessage = null;
//        if (e instanceof retrofit2.adapter.rxjava.HttpException) {
//            retrofit2.Response<?> response = ((retrofit2.adapter.rxjava.HttpException) e).response();
//            if (response != null) {
//                ResponseBody responseBody = response.errorBody();
//                try {
//                    errorMessage = responseBody.string();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//        } else if (e instanceof SocketException) {
//            errorMessage = "网络错误，请检查网络";
//        }
//        if (TextUtils.isEmpty(errorMessage)) {
//            errorMessage = "服务繁忙,请稍后重试";
//        }
//        return errorMessage;
//    }
//}

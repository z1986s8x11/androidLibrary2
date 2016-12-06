package com.zhusx.retrofit2;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/9/27 10:28
 */

//public class LoadData<T> extends BaseLoadData<LoadData.Api, HttpResult<T>, Object> {
//    private BaseActivity activity;
//
//    public LoadData(Api api, BaseActivity activity) {
//        super(api);
//        this.activity = activity;
//        activity._addOnCycleListener(new Lib_OnCycleListener() {
//            @Override
//            public void onResume() {
//            }
//
//            @Override
//            public void onPause() {
//            }
//
//            @Override
//            public void onDestroy() {
//                _cancelLoadData();
//            }
//        });
//    }
//
//    public enum Api {
//        购物车列表, 修改购物车商品数量, 批量删除购物车商品, 获取收货地址, 保存收货地址, 订单列表, 支付方式, 可用优惠券, 创建订单, 获取支付链接, 删除订单, 取消订单, 评论订单, 订单详情, 物流信息, 确认订单, 发送短信, 验证短信, 预计收货时间
//    }
//
//    @Override
//    protected Observable<HttpResult<T>> getHttpParams(Api api, Object... var2) {
//        switch (api) {
//            case 购物车列表:
//                return cast(RetrofitUtil.createApi(ShopCartService.class).getCarts());
//            case 修改购物车商品数量:
//                return cast(RetrofitUtil.createApi(ShopCartService.class).changeCartNum(String.valueOf(var2[0]), String.valueOf(var2[1])));
//            case 批量删除购物车商品:
//                return cast(RetrofitUtil.createApi(ShopCartService.class).deleteCart(String.valueOf(var2[0])));
//            case 获取收货地址:
//                return cast(RetrofitUtil.createApi(AddressService.class).getAddress());
//            case 支付方式:
//                return cast(RetrofitUtil.createApi(OrderService.class).getPaymentsList(String.valueOf(var2[0])));
//            case 可用优惠券:
//                return cast(RetrofitUtil.createApi(CouponService.class).getCoupon(String.valueOf(var2[0])));
//            case 创建订单:
//                return cast(RetrofitUtil.createApi(OrderService.class).createOrder(String.valueOf(var2[0]), String.valueOf(var2[1]), String.valueOf(var2[2])));
//            case 保存收货地址:
//                return cast(RetrofitUtil.createApi(AddressService.class).saveAddress(String.valueOf(var2[0]), String.valueOf(var2[1]), String.valueOf(var2[2]), String.valueOf(var2[3]), String.valueOf(var2[4]), String.valueOf(var2[5])));
//            case 订单列表:
//                return cast(RetrofitUtil.createApi(OrderService.class).getOrderList(String.valueOf(var2[0]), getNextPage()));
//            case 获取支付链接:
//                return cast(RetrofitUtil.createApi(OrderService.class).payString(String.valueOf(var2[0]), String.valueOf(var2[1]), String.valueOf(var2[2])));
//            case 物流信息:
//                return cast(RetrofitUtil.createApi(OrderService.class).logistics(String.valueOf(var2[0])));
//            case 删除订单:
//                return cast(RetrofitUtil.createApi(OrderService.class).deleteOrder(String.valueOf(var2[0])));
//            case 取消订单:
//                return cast(RetrofitUtil.createApi(OrderService.class).cancelOrder(String.valueOf(var2[0]), String.valueOf(var2[1])));
//            case 评论订单:
//                return cast(RetrofitUtil.createApi(OrderService.class).commentOrder(String.valueOf(var2[0]), String.valueOf(var2[1])));
//            case 订单详情:
//                return cast(RetrofitUtil.createApi(OrderService.class).getOrderDetail(String.valueOf(var2[0])));
//            case 确认订单:
//                return cast(RetrofitUtil.createApi(OrderService.class).confirmOrder(String.valueOf(var2[0])));
//            case 发送短信:
//                return cast(RetrofitUtil.createApi(OrderService.class).sendSms(String.valueOf(var2[0]), "1", String.valueOf(var2[1])));
//            case 验证短信:
//                return cast(RetrofitUtil.createApi(OrderService.class).verificationCode(String.valueOf(var2[0]), "1", String.valueOf(var2[1])));
//            case 预计收货时间:
//                return cast(RetrofitUtil.createApi(OrderService.class).deliveryTime(String.valueOf(var2[0])));
//        }
//        return null;
//    }
//
//    private Toast mToast;
//    private LoadingProgressDialog progressDialog;
//
//    @Override
//    protected void onLoadStart(Api api, HttpRequest<Object> request) {
//        boolean isCanCancel = true;
//        switch (api) {
//            case 修改购物车商品数量:
//            case 批量删除购物车商品:
//            case 获取收货地址:
//            case 创建订单:
//            case 获取支付链接:
//            case 删除订单:
//            case 取消订单:
//            case 评论订单:
//            case 确认订单:
//            case 验证短信:
//                isCanCancel = false;
//            case 保存收货地址:
//                if (progressDialog == null) {
//                    progressDialog = new LoadingProgressDialog(activity);
//                }
//                progressDialog.setCanceledOnTouchOutside(isCanCancel);
//                progressDialog.show();
//                break;
//        }
//    }
//
//    @Override
//    protected void onLoadComplete(Api api, HttpRequest<Object> request, HttpResult<T> tHttpResult) {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }
//
//    @Override
//    protected void onLoadError(Api api, HttpRequest<Object> request, HttpResult<T> result, boolean var4, String errorMessage) {
//        switch (api) {
//            case 购物车列表:
//            case 修改购物车商品数量:
//            case 批量删除购物车商品:
//            case 支付方式:
//            case 可用优惠券:
//            case 创建订单:
//            case 保存收货地址:
//            case 订单列表:
//            case 获取支付链接:
//            case 删除订单:
//            case 取消订单:
//            case 评论订单:
//            case 确认订单:
//            case 发送短信:
//            case 验证短信:
//                if (mToast == null) {
//                    mToast = Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT);
//                } else {
//                    mToast.setText(errorMessage);
//                }
//                mToast.show();
//                break;
//        }
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//    }
//
//    public static <T> T cast(Object o) {
//        return (T) o;
//    }
//
//    @Override
//    protected boolean isSuccess(Api api, HttpResult<T> data) {
//        return data.code == 200;
//    }
//
//    @Override
//    protected String getMessage(Api api, HttpResult<T> data) {
//        return data.message;
//    }
//
//    public boolean hasMoreData() {
//        if (!_hasCache()) {
//            return true;
//        }
//        HttpResult result = _getLastData();
//        if (result.data instanceof IPageData) {
//            IPageData impl = (IPageData) result.data;
//            if (impl.getTotalPageCount() > 0 && impl.getTotalPageCount() >= currentPage + 1) {
//                return true;
//            }
//            return false;
//        } else {
//            if (LogUtil.DEBUG) {
//                LogUtil.e(this, String.valueOf(_getRequestID()) + "T 必须实现 IPageData 接口");
//            }
//        }
//        return true;
//    }
//}

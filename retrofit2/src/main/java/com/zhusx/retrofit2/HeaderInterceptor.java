package com.zhusx.retrofit2;

/**
 * 添加通用header头以及token失效（403）的处理
 *
 * @author zhangbb
 * @date 2016/8/5.
 */
//public class HeaderInterceptor implements Interceptor {
//
//    private static final String TAG = HeaderInterceptor.class.getSimpleName();
//    private Context context;
//
//    public HeaderInterceptor(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    public Response intercept(final Chain chain) throws IOException {
//        Request request = chain.request();
//        String url = request.url().uri().getPath();
//        Response originalResponse;
//        if (url.equals("/token/obtain"))
//            originalResponse = chain.proceed(request);
//        else
//            originalResponse = chain.proceed(RequestHelper.getCommonHeader(chain, context));
//        if (originalResponse.code() == 403) {
//            final String errStr = new String(bytes(originalResponse.body()), charset(originalResponse.body()));
//            Integer code = JSON.parseObject(errStr).getInteger("code");
//            switch (code) {
//                case 1002: //token失效
//                    Request newRequest = getRequest(request);
//                    originalResponse.body().close();
//                    return chain.proceed(newRequest);
//                case 1001:
//                    Intent intent = new Intent(MainActivity.GO_TO_LOGIN_ACTION);
//                    intent.putExtra("message", JSON.parseObject(errStr).getString("message"));
//                    context.sendBroadcast(intent);
//                    break;
//            }
//        }
//        return originalResponse;
//    }
//
//    /**
//     * token失效是构造新的request
//     *
//     * @param request
//     * @return
//     * @throws IOException
//     */
//    private Request getRequest(Request request) throws IOException {
//        Retrofit build = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .baseUrl(Constant.HOST)
//                .build();
//        Call<HttpResult> httpResultCall = build.create(DeviceDataService.class)
//                .refreshDeviceToken(RequestHelper.generateTokenHeader(context, true));
//        HttpResult body = httpResultCall.execute().body();
//        try {
//            JSONObject json = new JSONObject(body.data.toString());
//            String token = json.getString("token");
//            UserManager.getInstance().setToken(token);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return RequestHelper.addCommonHeader(request, context);
//    }
//
//
//    private final byte[] bytes(ResponseBody body) throws IOException {
//        long contentLength = body.contentLength();
//        if (contentLength > Integer.MAX_VALUE) {
//            throw new IOException("Cannot buffer entire body for content length: " + contentLength);
//        }
//
//        BufferedSource source = body.source();
//        byte[] bytes;
//        try {
//            bytes = source.readByteArray();
//        } finally {
//
//        }
//        if (contentLength != -1 && contentLength != bytes.length) {
//            throw new IOException("Content-Length and stream length disagree");
//        }
//
//        source.buffer().write(bytes);
//        return bytes;
//    }
//
//    private Charset charset(ResponseBody body) {
//        MediaType contentType = body.contentType();
//        return contentType != null ? contentType.charset(UTF_8) : UTF_8;
//    }
//}A

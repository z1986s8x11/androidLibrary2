package com.zhusx.retrofit2;

import android.app.Application;
import android.text.TextUtils;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.utils._Encryptions;
import com.zhusx.core.utils._Systems;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofits 工具类
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/6 9:07
 */
public class Retrofits {
    private volatile static Retrofit retrofit;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    public static Application application;
    public static final String BASE_HOST = "http://stag.api.m.xgqqg.com";

    static {
        OkHttpClient.Builder client = new OkHttpClient().newBuilder();
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                String versionName = _Systems.getAppVersionName(application);
                long time = System.currentTimeMillis();
                Request request = chain.request();
                Request.Builder builder = request.newBuilder();
                String path = request.url().encodedPath();
                if ("/sites/token".equals(path)) {
                    builder.addHeader("X-Agent", "version=" + versionName + ",platform=android,time=" + time + ",uuid=" + _Encryptions.encodeHex(_Encryptions.Symmetry.AES_ECB_PKCS5, "4a835aaf596dd6750daca758b79d89bc", _Systems.getUUID(application)));
                    return chain.proceed(builder.build());
                } else {
                    String param = "";
                    RequestBody requestBody = request.body();
                    if (requestBody != null) {
                        Buffer buffer = new Buffer();
                        requestBody.writeTo(buffer);
                        if (isPlaintext(buffer)) {
                            MediaType contentType = requestBody.contentType();
                            Charset charset = UTF8;
                            if (contentType != null) {
                                charset = contentType.charset(UTF8);
                            }
                            param = buffer.readString(charset);
                            if (!TextUtils.isEmpty(param)) {
                                String[] params = param.split("&");
                                if (params.length > 1) {
                                    Arrays.sort(params, new Comparator<String>() {
                                        @Override
                                        public int compare(String o1, String o2) {
                                            return o1.compareTo(o2);
                                        }
                                    });
                                    param = "";
                                    for (String s : params) {
                                        param += "&" + s;
                                    }
                                    param = param.substring(1, param.length());
                                }
                            }
                        }
                    }
                    builder.addHeader("X-Agent", "version=" + versionName + ",platform=android,time=" + time)
                            .addHeader("X-Signature", _Encryptions.encodeHex(_Encryptions.Digest.MD5, _Encryptions.encodeHex(_Encryptions.Digest.MD5, param) + _Systems.getUUID(application) + time))
                            .addHeader("X-Token", "");
                }
                Response response = chain.proceed(builder.build());
//                if (response.code() == 403) {
//                    try {
//                        int code = new JSONObject(response.body().string()).getInt("code");
//                        if (code == 1002) {
//                            Call<HttpResult> call = retrofit.create(ApiService.Base.class).callSwapToken();
//                            HttpResult body = call.execute().body();
//                            String resultStr = body.getData().toString();
//                            if (LogUtil.DEBUG) {
//                                LogUtil.e("Refresh Token", resultStr);
//                            }
//                            JSONObject json = new JSONObject(resultStr);
//                            TOKEN = json.getString("token");
//                            response.body().close();
//                            return chain.proceed(builder.build());
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
                return response;
            }
        });
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (LogUtil.DEBUG) {
                    LogUtil.e(message);
                }
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .client(client.addInterceptor(logging).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_HOST)
                .build();
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}

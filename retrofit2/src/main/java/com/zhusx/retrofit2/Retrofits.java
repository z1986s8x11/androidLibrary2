package com.zhusx.retrofit2;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/6 9:07
 */
public class Retrofits {
    public static final String BASE_URL = "";
    private volatile static Retrofit retrofit;

    static {
        OkHttpClient.Builder client = new OkHttpClient().newBuilder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .client(client
                        .addInterceptor(logging)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }
}

package com.zhusx.retrofit2;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
* Author        zhusx
* Email         327270607@qq.com
* Created       2016/12/6 9:07
*/
public class RetrofitUtil {

    private volatile static Retrofit retrofit;

    /**
     * 初始化，默认设置
     */
    public static void initialize(String url) {
        initRetrofit(null, url);
    }

    /**
     * 初始化，自定义配置
     *
     * @param okHttpClient
     */
    public static void initialize(OkHttpClient okHttpClient, String url) {
        initRetrofit(okHttpClient, url);
    }


    private static void initRetrofit(OkHttpClient okHttpClient, String url) {
        if (retrofit == null) {
            synchronized (RetrofitUtil.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .baseUrl(url)
                            .build();
                }
            }
        }
    }

    /**
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createApi(Class<T> clazz) {
        return retrofit.create(clazz);
    }


}

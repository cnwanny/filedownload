package com.android.hifosystem.hifoevaluatevalue.framework_net.retrofit;

import com.android.hifosystem.hifoevaluatevalue.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 文件名： AppClient
 * 功能：   Retrofit 像是代理其实使用的不是代理，是反射加接口的回调。
 * 作者： wanny
 * 时间： 16:00 2016/8/5
 */
public class AppClient {

    public static Retrofit mRetrofit;

    /**
     * 构建Retrofit对象，设置方法属性。
     * @return Retrofit
     */
    public static Retrofit retrofit() {
        //Retrofit 配置OkHttp来实现网络请求
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //debug模式下加入调试。
            if (BuildConfig.DEBUG) {
                // Log信息拦截器
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                builder.addInterceptor(loggingInterceptor);
            }
            //
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(ApiStores.API_SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())//自带的gson解析器
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加数据回调相应
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

}

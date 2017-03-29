package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 类名： OkHttpUtils
 * 工鞥： 封装Ok框架的请求操作
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class OkHttpUtils {


    private final static OkHttpClient okHttpClient = new OkHttpClient();

    static {
        okHttpClient.setConnectTimeout(45, TimeUnit.SECONDS);
    }

    /**
     * 同步请求，一般是在线程中执行
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static Response execute(Request request) throws IOException {
        return okHttpClient.newCall(request).execute();
    }




    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        okHttpClient.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    public static void enqueue(Request request) {
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response arg0) throws IOException {

            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {

            }
        });
    }

    public static String getStringFromServer(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    private static final String CHARSET_NAME = "UTF-8";




    /**
     * 为HttpGet 的 url 方便的添加1个name value 参数。
     *
     * @param url
     * @param name
     * @param value
     * @return
     */
    public static String attachHttpGetParam(String url, String name, String value) {
        return url + "?" + name + "=" + value;
    }


//    /**
//     * 关闭连接
//     */
//
//    public void close(){
//        if(okHttpClient != null){
//            okHttpClient.c
//        }
//    }
}

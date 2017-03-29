package com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 文件名： SubscribCallBack
 * 功能：  实现网络成功还是失败的回掉
 * 作者： wanny
 * 时间： 16:28 2016/8/5
 */
public class SubscribCallBack<T> extends Subscriber<T> {

    private ApiCallback<T> apiCallback;

    public SubscribCallBack(ApiCallback<T> apiCallback) {
        this.apiCallback = apiCallback;
    }

    @Override
    public void onCompleted() {
        apiCallback.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            //httpException.response().errorBody().string()
            int code = httpException.code();
            String msg = httpException.getMessage();
            if (code == 504) {
                msg = "网络不给力";
            }
            apiCallback.onFailure(code, msg);
        } else {
            apiCallback.onFailure(0, e.getMessage());
        }
        apiCallback.onCompleted();
    }

    @Override
    public void onNext(T t) {
        apiCallback.onSuccess(t);
    }
}

package com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava;

/**
 * 文件名： ApiCallback
 * 功能：  获取数据成功回调接口
 * 作者： wanny
 * 时间： 16:28 2016/8/5
 */
public interface ApiCallback<T> {

    void onSuccess(T model);

    void onFailure(int code, String msg);

    void onCompleted();

}

package com.android.hifosystem.hifoevaluatevalue.framework_care;

/**
 * 文件名： BaseOperateView
 * 功能：
 * 作者： wanny
 * 时间： 19:06 2016/8/17
 */
public interface BaseOperateView<T> {

    void getDataSuccess(T t);
    //失败提示信息
    void getDataFail(String msg);
    //显示加载的dialog

}

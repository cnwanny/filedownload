package com.android.hifosystem.hifoevaluatevalue.sharepackage;

/**
 * 文件名： ShareQQListener
 * 功能：
 * 作者： wanny
 * 时间： 10:19 2016/12/19
 */
public interface ShareQQListener {
    //QQ分享成功后的回调提示
    void setShare(boolean istrue);
    //启动调用json
    void sendLocation(String json);
    //支付成功后
    void payResult(String value);
}

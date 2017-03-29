package com.android.hifosystem.hifoevaluatevalue.sharepackage;

import android.app.Activity;

import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.tencent.connect.common.UIListenerManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * 文件名： BaseUiListener
 * 功能：
 * 作者： wanny
 * 时间： 17:27 2016/12/15
 */
public class BaseUiListener implements IUiListener {
    private Activity activity;

    public BaseUiListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onComplete(Object o) {
        new HiFoToast(activity, "分享完成");
    }

    @Override
    public void onError(UiError uiError) {
        new HiFoToast(activity, "分享失败");
    }

    @Override
    public void onCancel() {
        new HiFoToast(activity, "取消分享");
    }
}

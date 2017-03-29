package com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic;

import android.os.Bundle;

import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_care.BaseActivity;


/**
 * 文件名： MvpActivity
 * 功能：
 * 作者： wanny
 * 时间： 15:37 2016/8/5
 */
public abstract  class MvpActivity<P extends BasePresenter> extends BaseActivity {
   //创建 Presenter对象，
   public P mvpPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mvpPresenter = createPresenter();
        super.onCreate(savedInstanceState);
        ActivityStackManager.getInstance().addActivity(this);

    }
    //提供统一的接口
    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

}

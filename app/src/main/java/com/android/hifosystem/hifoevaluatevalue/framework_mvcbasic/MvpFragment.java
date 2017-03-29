package com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.android.hifosystem.hifoevaluatevalue.framework_care.BaseFragment;


/**
 * 文件名： MvpFragment
 * 功能：
 * 作者： wanny
 * 时间： 15:47 2016/8/5
 */
public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mvpPresenter;
    //绑定数据
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mvpPresenter = createPresenter();
    }

    protected abstract P createPresenter();

   //解除数据的绑定
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mvpPresenter != null) {
            mvpPresenter.detachView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

package com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp;


import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.BasePresenter;

/**
 * 文件名： CameraPresenter
 * 功能：
 * 作者： wanny
 * 时间： 16:48 2016/8/29
 */
public class CameraPresenter extends BasePresenter<CameraView> {


    public CameraPresenter(CameraView view){
        attachView(view);
    }


}

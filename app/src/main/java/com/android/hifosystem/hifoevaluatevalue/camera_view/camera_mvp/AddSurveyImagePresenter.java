package com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp;


import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.BasePresenter;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.ApiCallback;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.SubscribCallBack;

/**
 * 文件名： AddSurveyImagePresenter
 * 功能：
 * 作者： wanny
 * 时间： 9:51 2016/11/23
 */
public class AddSurveyImagePresenter extends BasePresenter<AddSurveyView> {


    public AddSurveyImagePresenter(AddSurveyView view) {
        attachView(view);
    }

    //获取服务器上已经存在的附件
    public void getSurveyFile(String fid) {
        mvpView.showLoading("正在加载");
        addSubscription(apiStores.getSurveyFileList(fid), new SubscribCallBack<>(new ApiCallback<SurveyFileModel>() {
            @Override
            public void onSuccess(SurveyFileModel model) {
                mvpView.getDataSuccess(model);
            }

            @Override
            public void onFailure(int code, String msg) {
                mvpView.getDataFail(msg);
            }

            @Override
            public void onCompleted() {
                mvpView.hideLoading();
            }
        }));
    }
}

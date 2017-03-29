package com.android.hifosystem.hifoevaluatevalue.notupload_mvp;


import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.FileEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FilePostModel;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.BasePresenter;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.ApiCallback;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.SubscribCallBack;

import java.util.ArrayList;

/**
 * 文件名： NotUpLoadPresenter
 * 功能：
 * 作者： wanny
 * 时间： 10:31 2016/9/5
 */
public class NotUpLoadPresenter extends BasePresenter<NotUpLoadView> {


    public NotUpLoadPresenter(NotUpLoadView view){
        attachView(view);
    }

    public void submitFileInfo(ArrayList<FileEntity> entity, final LocalFileEntity localFileEntity) {
        addSubscription(apiStores.submitFileInfo(entity), new SubscribCallBack<>(new ApiCallback<FilePostModel>() {
            @Override
            public void onSuccess(FilePostModel model) {
                mvpView.filePostSuccess(model,localFileEntity);
            }
            @Override
            public void onFailure(int code, String msg) {

            }

            @Override
            public void onCompleted() {

            }
        }));
    }

}

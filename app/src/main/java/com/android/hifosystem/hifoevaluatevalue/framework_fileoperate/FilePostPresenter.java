package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;



import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.FileEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.BasePresenter;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.ApiCallback;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.SubscribCallBack;

import java.util.ArrayList;

/**
 * 文件名： FilePostPresenter
 * 功能：
 * 作者： wanny
 * 时间： 13:54 2016/8/26
 */
public class FilePostPresenter extends BasePresenter<FilePostPresenterView> {
    private FilePostPresenterView filePostPresenterView;

    public FilePostPresenter(FilePostPresenterView filePostPresenterView) {
        this.filePostPresenterView = filePostPresenterView;
    }
    public void submitFileInfo(ArrayList<FileEntity> entity, final LocalFileEntity localFileEntity) {
        addSubscription(apiStores.submitFileInfo(entity), new SubscribCallBack<>(new ApiCallback<FilePostModel>() {
            @Override
            public void onSuccess(FilePostModel model) {
                if(filePostPresenterView != null){
                   filePostPresenterView.filePostSuccess(model,localFileEntity);
                }
            }
            @Override
            public void onFailure(int code, String msg) {
//                if(filePostPresenterView != null){
//                    filePostPresenterView.getDataFail(msg);
//                }
            }

            @Override
            public void onCompleted() {

            }
        }));
    }
}

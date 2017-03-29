package com.android.hifosystem.hifoevaluatevalue.location_service;

import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.BasePresenter;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.ApiCallback;
import com.android.hifosystem.hifoevaluatevalue.framework_net.rxjava.SubscribCallBack;

/**
 * 文件名： LocationServicePresenter
 * 功能：
 * 作者： wanny
 * 时间： 9:44 2017/3/23
 */
public class LocationServicePresenter extends BasePresenter<LocationUploadImpl> {

    LocationUploadImpl locationUpload;
    public LocationServicePresenter(LocationUploadImpl locationUpload){
       this.locationUpload = locationUpload;
    }
    public void saveLocation(String token , double lon , double lat) {
        addSubscription(apiStores.saveLocation(token,lon,lat), new SubscribCallBack<>(new ApiCallback<ResultEntity>() {
            @Override
            public void onSuccess(ResultEntity model) {
                if(locationUpload != null){
                    locationUpload.success(model);
                }
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

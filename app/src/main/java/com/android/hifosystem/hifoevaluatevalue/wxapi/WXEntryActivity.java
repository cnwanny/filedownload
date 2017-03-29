
package com.android.hifosystem.hifoevaluatevalue.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 类名： ${type_name}
 * 工鞥：
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppContent.APP_ID, false);
        api.registerApp(AppContent.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
//        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//            switch (baseResp.errCode) {
//                case BaseResp.ErrCode.ERR_OK:
//                    //分享成功
//                    new HiFoToast(this, "支付成功");
//                    break;
//                case BaseResp.ErrCode.ERR_USER_CANCEL:
//                    //分享取消
//                    new HiFoToast(this, "取消支付");
//                    break;
//                case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                    //分享拒绝
//                    new HiFoToast(this, "支付拒绝");
//                    break;
//            }
//
//        } else{
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                //分享成功
                new HiFoToast(this, "分享成功");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                //分享取消
                new HiFoToast(this, "取消分享");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //分享拒绝
                new HiFoToast(this, "分享拒绝");
                break;
//            }
        }
        this.finish();
    }

}


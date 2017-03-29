
package com.android.hifosystem.hifoevaluatevalue.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;

import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.GongPingOldActivity;
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


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;
    private GongPingOldActivity.WXPayReceiver payReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBroadcastReceiver();
        api = WXAPIFactory.createWXAPI(this, "");
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
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX){
            if(baseResp.errCode == 0){
                //支付成功
                new HiFoToast(getApplicationContext(),"支付成功");
                Intent intent = new Intent();
                intent.setAction(AppContent.WXPAY);
                intent.putExtra("result","1");
                sendBroadcast(intent);
//                if(gongPingOldActivity.mWebView != null){
//                    new HiFoToast(getApplicationContext(),"回调");
//                    gongPingOldActivity.mWebView.loadUrl("javascript:goSearchHose(' " + "1" + "')");
//                }
            }else if(baseResp.errCode == -1){
                //支付失败
                new HiFoToast(getApplicationContext(),"支付失败");
                Intent intent = new Intent();
                intent.setAction(AppContent.WXPAY);
                intent.putExtra("result","0");
                sendBroadcast(intent);
            }else if(baseResp.errCode == -2){
                //取消支付
                new HiFoToast(getApplicationContext(),"取消支付");
                Intent intent = new Intent();
                intent.setAction(AppContent.WXPAY);
                intent.putExtra("result","0");
                sendBroadcast(intent);
            }
        }
        finish();
        //让当前的页面休息二秒
//       Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        },2000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(payReceiver != null){
            unregisterReceiver(payReceiver);
            payReceiver = null;
        }
    }


    @Override
    protected void onDestroy() {
        if(payReceiver != null){
            unregisterReceiver(payReceiver);
            payReceiver = null;
        }
        super.onDestroy();
    }

    private void registerBroadcastReceiver(){
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AppContent.WXPAY);
        if(payReceiver == null){
            payReceiver = new GongPingOldActivity.WXPayReceiver();
        }
        registerReceiver(payReceiver,intentFilter);
    }
}


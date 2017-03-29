package com.android.hifosystem.hifoevaluatevalue.location_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;


/**
 * 类名： ${type_name}
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class SendLocationRecever extends BroadcastReceiver {
    //接收消息 ；
    @Override
    public void onReceive(Context context, Intent intent) {
     if(intent != null){
         if(intent.getAction().equals(AppConstant.LOCATIONREVEIVER)){
           //启动服务
             Intent intents = new Intent(context, LocationService.class);  // 要启动的控件
             intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             intent.putExtra("end","data");
             context.startService(intents);
             LogUtil.log("sss", "接收到命令执行");
         }
     }
    }
}

package com.android.hifosystem.hifoevaluatevalue.framework_care;

/**
 * 文件名： AppContent
 * 功能：
 * 作者： wanny
 * 时间： 16:06 2016/8/12
 */
public interface AppContent {

    //权限请求 照相机
    int CAMREA_REQUESTCODE = 0x0001;
    //电话
    int PHONE_REQUESTCODE = 0x0002;
    //读写权限
    int STORAGE_REQUESTCODE = 0x0003;
    //位置信息
    int LOCATION_REQUESTCODE = 0x0004;
    //录音requestcode;
    int RECODEAUDIO_REQUESTCODE = 0x0005;
    //图片类型匹配
    String[] IMAGE_TYPE = new String[]{".jpg",".jpeg",".png",};

    //停止上传附件广播标示
    String StopReceiverAction = "com.wanny.stop.receiver";  //com.wanny.stop.receiver
    //附件上传成功广播标示
    String UploadSuccess = "com.android.success.upload";
    //微信分享
    String APP_ID = "wx0081e41c3700bf14";
    //QQ分享
    String QQ_ID = "1104813657";
    //微信支付成功后回调
    String WXPAY = "com.android.wanny.wxpay";
}


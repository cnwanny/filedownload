package com.android.hifosystem.hifoevaluatevalue.Utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;


import com.alipay.sdk.app.PayTask;
import com.android.hifosystem.hifoevaluatevalue.AddImageActivity;
import com.android.hifosystem.hifoevaluatevalue.AttchmentEntity;
import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.FileOperateData;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.GongPingOldActivity;
import com.android.hifosystem.hifoevaluatevalue.NotCompleteUploadActivity;
import com.android.hifosystem.hifoevaluatevalue.R;
import com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent;
import com.android.hifosystem.hifoevaluatevalue.sharepackage.BaseUiListener;
import com.android.hifosystem.hifoevaluatevalue.sharepackage.ShareDialog;
import com.android.hifosystem.hifoevaluatevalue.sharepackage.ShareEntity;
import com.android.hifosystem.hifoevaluatevalue.sharepackage.ShareImp;
import com.android.hifosystem.hifoevaluatevalue.sharepackage.ShareQQListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.psnl.hzq.tool.TimeEx;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent.APP_ID;
import static com.tencent.open.utils.Global.getPackageName;

/**
 * 文件名： JsOperator
 * 功能：
 * 作者： wanny
 * 时间： 10:59 2016/10/31
 */
public class JsOperator {
    private Activity activity;

    public JsOperator(Activity context, ShareQQListener shareQQListener) {
        this.activity = context;
        this.shareQQListener = shareQQListener;
        regWx();
    }

    private ShareQQListener shareQQListener;

    /**
     * 弹出消息对话框
     */
    @JavascriptInterface
    public void showDialog(String message) {
        String username = "";
        String surveyId = "";
        ArrayList<AttchmentEntity> resultData = null;
        if (!TextUtils.isEmpty(message)) {
            try {
                JSONObject jsonObject = new JSONObject(message);
                if (jsonObject.has("UserAccount")) {
                    username = jsonObject.getString("UserAccount");
                }
                if (jsonObject.has("SurveryID")) {
                    surveyId = jsonObject.getString("SurveryID");
                }
                if (jsonObject.has("AttchmentCategory")) {
                    JSONObject house = jsonObject.getJSONObject("AttchmentCategory");
                    if (house != null && house.has("house")) {
                        JSONArray jsonArray = house.getJSONArray("house");
                        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
                        resultData = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<AttchmentEntity>>() {
                        }.getType());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(surveyId) && (resultData != null && resultData.size() > 0)) {
            Intent intent = new Intent(activity, AddImageActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("surveyId", surveyId);
            intent.putParcelableArrayListExtra("category", resultData);
            activity.startActivityForResult(intent, 0x0005);
        }
    }


    @JavascriptInterface
    public String getLoginInfo() {
        return "1";
    }


    //网址
    String webpageUrl = "";
    //标题
    String title = "";
    //
    String imageSrc = "";

    @JavascriptInterface
    public void shareWeixin(String url) {
        if (!TextUtils.isEmpty(url)) {
            try {
                JSONObject jsonObject = new JSONObject(url);
                if (jsonObject.has("shareUrl")) {
                    webpageUrl = jsonObject.getString("shareUrl");
                }
                if (jsonObject.has("title")) {
                    title = jsonObject.getString("title");
                }
                if (jsonObject.has("imgsrc")) {
                    imageSrc = jsonObject.getString("imgsrc");
                }
                createShareDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @JavascriptInterface
    public void startUpLoad(String userName) {
        if (!TextUtils.isEmpty(userName)) {
            //查询一下是否存在未上传的附件
            //如果有提示未上传的附加
            FileOperateData fileOperateData = FileOperateData.getInstance(activity);
            ArrayList<LocalFileEntity> result = fileOperateData.findByUserName(userName);
            if (result != null && result.size() > 0) {
                Intent intentreceiver = new Intent();
                LogUtil.log("点击待上传附件发送广播", "未上传的附件进入，停止广播");
                intentreceiver.setAction(AppContent.StopReceiverAction);     //设置Action
                activity.sendBroadcast(intentreceiver);
                LogUtil.log("点击待上传附件发送广播", "NotComplete");
                Intent intent = new Intent(activity, NotCompleteUploadActivity.class);//
                intent.putExtra("username", userName);
                activity.startActivity(intent);
            } else {
                new HiFoToast(activity, "没有未上传的附件");
                return;
            }
        }
    }

    //创建分享的dialog
    private ShareDialog shareDialog;
    private void createShareDialog() {
        ArrayList<ShareEntity> data = new ArrayList<>();
        ShareEntity entity = new ShareEntity();
        entity.setType("微信朋友圈");
        entity.setTypeBgId(R.mipmap.icon_share_wx_friends);
        data.add(entity);

        entity = new ShareEntity();
        entity.setType("微信好友");
        entity.setTypeBgId(R.mipmap.icon_share_wx_friend);
        data.add(entity);

        entity = new ShareEntity();
        entity.setType("QQ");
        entity.setTypeBgId(R.mipmap.icon_share_qq);
        data.add(entity);

        entity = new ShareEntity();
        entity.setType("QQ空间");
        entity.setTypeBgId(R.mipmap.icon_share_qqspace);
        data.add(entity);

//        entity = new ShareEntity();
//        entity.setType("新浪微博");
//        entity.setTypeBgId(R.mipmap.icon_share_sina);
//        data.add(entity);

        entity = new ShareEntity();
        entity.setType("复制链接");
        entity.setTypeBgId(R.mipmap.icon_share_copyline);
        data.add(entity);
        if (shareDialog == null) {
            shareDialog = new ShareDialog(activity, R.style.dialog, data, activity);
            shareDialog.setShareImp(shareImp);
            shareDialog.show();
        } else {
            if (shareDialog != null) {
                if (!shareDialog.isShowing()) {
                    shareDialog.show();
                }
            }
        }
    }

    private ShareImp shareImp = new ShareImp() {
        @Override
        public void shareQQ() {
            if (shareDialog != null) {
                if (shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
            }
            onClickShare(title, webpageUrl, imageSrc);
        }

        @Override
        public void shareWeixinFriends() {

            if (shareDialog != null) {
                if (shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
            }
            shareWX(webpageUrl, title, SendMessageToWX.Req.WXSceneTimeline);
        }

        @Override
        public void shareWeixin() {
            if (shareDialog != null) {
                if (shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
            }
            shareWX(webpageUrl, title, SendMessageToWX.Req.WXSceneSession);
        }

        @Override
        public void shareQQFriendsSpace() {
            if (shareDialog != null) {
                if (shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
            }
            shareToQzone(title, webpageUrl, imageSrc);
        }

        @Override
        public void copyURL() {
            if (shareDialog != null) {
                if (shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
            }
            ClipboardManager cmb = (ClipboardManager) activity.getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(webpageUrl.trim());
            new HiFoToast(activity, "已复制到剪切板");
        }

        @Override
        public void shareSina() {

        }

        @Override
        public void cancel() {
            if (shareDialog != null) {
                if (shareDialog.isShowing()) {
                    shareDialog.dismiss();
                }
            }
        }
    };


    private void shareWX(String url, String title, int type) {
        if (!iwxapi.isWXAppInstalled()) {
            new HiFoToast(activity, "您还未安装微信客户端,暂不能执行该功能");
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage mediaMessage = new WXMediaMessage(webpage);
        mediaMessage.title = title;
        mediaMessage.description = title;
//        Bitmap thumb = BitmapFactory.de(activity.getResources(), R.mipmap.app_icon_96);
//        mediaMessage.thumbData = ImageUtils.bmpToByteArray(thumb, true);
        if (getInputStream(imageSrc) != null) {
            mediaMessage.thumbData = ImageUtils.bmpToByteArray(getInputStream(imageSrc), true);
        } else {
            Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.app_icon_96);
            mediaMessage.thumbData = ImageUtils.bmpToByteArray(thumb, true);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = type;
        req.transaction = String.valueOf(TimeEx.getSecTime());
        req.message = mediaMessage;
        iwxapi.sendReq(req);
    }


    //获取网络图片并且
    private Bitmap getInputStream(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();
            if (inputStream != null) {
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                Bitmap resoutce = Bitmap.createScaledBitmap(bmp, 120, 120, true);
                if (bmp != null) {
                    bmp.recycle();
                }
                if (resoutce != null) {
                    return resoutce;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //分享到QQ;
    private void onClickShare(String title, String target_url, String imageUrl) {
        //也是分享到空间
        if (GongPingOldActivity.mTencent != null) {
            final Bundle params = new Bundle();
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, title);
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, target_url);
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
            if (shareQQListener != null) {
                shareQQListener.setShare(true);
            }
            GongPingOldActivity.mTencent.shareToQQ(activity, params, new BaseUiListener(activity));
        }
    }

    //分享到QQ空间
    private void shareToQzone(String title, String target_url, String imageUrl) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, title);//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, target_url);//必填
        ArrayList<String> imagelist = new ArrayList<>();
        imagelist.add(imageUrl);
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imagelist);
        if (GongPingOldActivity.mTencent != null) {
            if (shareQQListener != null) {
                shareQQListener.setShare(true);
            }
            GongPingOldActivity.mTencent.shareToQzone(activity, params, new BaseUiListener(activity));
        }
    }

    //注册到微信
    private IWXAPI iwxapi;
    boolean hasInstalled = true;
    private void regWx() {
        iwxapi = WXAPIFactory.createWXAPI(activity, AppContent.APP_ID);
        hasInstalled = iwxapi.isWXAppInstalled() && iwxapi.isWXAppSupportAPI();
    }


    @JavascriptInterface
    public void payWX(String dataJson) {
        if (!iwxapi.isWXAppInstalled()) {
            new HiFoToast(activity, "您还未安装微信客户端,暂不能执行该功能");
            return;
        }
        if (!TextUtils.isEmpty(dataJson)) {
            LogUtil.log(dataJson);
            try {
                JSONObject jsonObject = new JSONObject(dataJson);
//                IWXAPI api = WXAPIFactory.createWXAPI(activity, AppContent.APP_ID, false);
//                // 将该app注册到微信
//                api.registerApp(APP_ID);
                PayReq request = new PayReq();
                request.appId = AppContent.APP_ID;
                request.partnerId = jsonObject.getString("partnerId");
                request.prepayId = jsonObject.getString("prepayId");
                request.packageValue = "Sign=WXPay";
                request.nonceStr = jsonObject.getString("nonceStr");
                request.timeStamp = jsonObject.getString("timeStamp");
                request.sign = jsonObject.getString("sign");
                iwxapi.sendReq(request);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @JavascriptInterface
    public void alipay(final String orderInfo) {
//        final String orderInfo = "";
        LogUtil.log("支付宝订单号", orderInfo);
        // 订单信息
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
//                Map result = alipay.payV2(orderInfo, true);
                String result = alipay.pay(orderInfo);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public static final int SDK_PAY_FLAG = 0x0009;
    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x0005) {
                if (!TextUtils.isEmpty((String) msg.obj)) {
                    if (shareQQListener != null) {
                        shareQQListener.sendLocation((String) msg.obj);
                    }
                }
            } else if (msg.what == SDK_PAY_FLAG) {
                if (msg.obj instanceof String) {
                    String code = (String) msg.obj;
                    if (code.length() > 18) {
                        String resultcode = code.substring(14, 18);
                        if (resultcode.equals("9000")) {
                            new HiFoToast(activity, "订单支付成功");
                            if (shareQQListener != null) {
                                shareQQListener.payResult("1");
                            }
                            //支付成功的话，
                        } else if (resultcode.equals("8000")) {
                            new HiFoToast(activity, "正在处理中");
                            if (shareQQListener != null) {
                                shareQQListener.payResult("0");
                            }
                        } else if (resultcode.equals("4000")) {
                            new HiFoToast(activity, "订单支付失败");
                            if (shareQQListener != null) {
                                shareQQListener.payResult("0");
                            }
                        } else if (resultcode.equals("6001")) {
                            new HiFoToast(activity, "用户中途取消");
                            if (shareQQListener != null) {
                                shareQQListener.payResult("0");
                            }
                        } else if (resultcode.equals("6002")) {
                            new HiFoToast(activity, "网络连接出错");
                            if (shareQQListener != null) {
                                shareQQListener.payResult("0");
                            }
                        }
                    }
                }

//                if (msg.obj instanceof Map) {
//                    if (((Map) msg.obj).containsKey("")) {
//                          String resultCode = (String) ((Map) msg.obj).get("resultStatus");
//                         if(resultCode.equals("9000")){
//                            new HiFoToast(activity,"订单支付成功");
//                         }else if(resultCode.equals("8000")){
//                             new HiFoToast(activity,"正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态");
//                         }else if(resultCode.equals("4000")){
//                             new HiFoToast(activity,"订单支付失败");
//                         }else if(resultCode.equals("5000")){
//                             new HiFoToast(activity,"重复请求");
//                         }else if(resultCode.equals("6001")){
//                             new HiFoToast(activity,"用户中途取消");
//                         }else if(resultCode.equals("6002")){
//                             new HiFoToast(activity,"网络连接出错");
//                         }else if(resultCode.equals("6004")){
//                             new HiFoToast(activity,"支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态");
//                         }
//                    }
//                }
            }
        }
    };

//
//    @JavascriptInterface
//    public int versionCode() {
//        PackageInfo packInfo = null;
//        try {
//            PackageManager packageManager = activity.getPackageManager();
//            packInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return packInfo.versionCode;
//    }


    boolean isRunning = false;

    @JavascriptInterface
    public void startLocation() {
        //启动定位
        if (!isRunning) {
            isRunning = true;
            getSignInLocation();
        }
    }

    LocationClient mLocationClient;

    protected void getSignInLocation() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(activity);     //声明LocationClient类
            mLocationClient.registerLocationListener(new MyLocationListener());    //注册监听函数
            initLocation();
        }
        new HiFoToast(activity, "正在定位，请稍等");
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        //int span = 1000;
        //option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    boolean isLocationSuccess = false;

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                isLocationSuccess = true;
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                isLocationSuccess = true;
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("离线定位成功，离线定位结果也是有效的");
                isLocationSuccess = true;
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                isLocationSuccess = false;
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                isLocationSuccess = false;
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                isLocationSuccess = false;
            } else {
                isLocationSuccess = false;
            }
            if (isLocationSuccess) {
                if (location != null) {
                    new HiFoToast(activity, "定位成功");
                    LogUtil.log("定位到的位置", location.getAddrStr());
                    HashMap<String, Double> value = new HashMap<>();
                    value.put("lat", location.getLatitude());
                    value.put("lng", location.getLongitude());
                    Gson gson = new Gson();
                    String json = gson.toJson(value);
                    Message message = new Message();
                    message.what = 0x0005;
                    message.obj = json;
                    mHandler.sendMessage(message);
                    if (mLocationClient != null) {
                        mLocationClient.stop();
                    }
                }
            } else {
                new HiFoToast(activity, "定位失败");
                Message message = new Message();
                message.what = 0x0005;
                message.obj = "-1";
                mHandler.sendMessage(message);
            }
            isRunning = false;
        }
    }
}


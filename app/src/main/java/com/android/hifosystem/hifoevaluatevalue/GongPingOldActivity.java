package com.android.hifosystem.hifoevaluatevalue;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.a17gp.filedownlibs.listener.DownOperateImp;
import com.a17gp.filedownlibs.operate.DownFileInfo;
import com.a17gp.filedownlibs.operate.HttpDownManager;
import com.a17gp.filedownlibs.operate.UIOperateHandler;
import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.AutoView.ImageSelectDialog;
import com.android.hifosystem.hifoevaluatevalue.AutoView.MyDialog;
import com.android.hifosystem.hifoevaluatevalue.AutoView.WaitDialog;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.ImageUtil;
import com.android.hifosystem.hifoevaluatevalue.Utils.JsOperator;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.Utils.PermissionUtil;
import com.android.hifosystem.hifoevaluatevalue.Utils.PreferenceUtil;
import com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.StoragePath;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.WordExcelPDFIntent;
import com.android.hifosystem.hifoevaluatevalue.framework_net.netwebservice.TaskSendNewWork;
import com.android.hifosystem.hifoevaluatevalue.framework_net.retrofit.ApiStores;
import com.android.hifosystem.hifoevaluatevalue.location_service.AppConstant;
import com.android.hifosystem.hifoevaluatevalue.location_service.SendLocationRecever;
import com.android.hifosystem.hifoevaluatevalue.sharepackage.BaseUiListener;
import com.android.hifosystem.hifoevaluatevalue.sharepackage.ShareQQListener;
import com.psnl.hzq.tool.TimeEx;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent.APP_ID;
import static com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent.QQ_ID;

/**
 * WebView 调用摄像头拍照,上传图片, 或 从相册选择图片上传.
 *
 * @author AlexTam
 *         created at 2016/10/14 9:58
 */
public class GongPingOldActivity extends Activity implements MyWebChomeClient.OpenFileChooserCallBack, ShareQQListener {
    private static final String TAG = "wanny";
    public static WebView mWebView;
    private static final int REQUEST_CODE_PICK_IMAGE = 0;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private Intent mSourceIntent;
    private ValueCallback<Uri> mUploadMsg;
    public ValueCallback<Uri[]> mUploadMsgForAndroid5;
    // permission Code
    private static final int P_CODE_PERMISSIONS = 101;
    private Animation animation;
    //    private RelativeLayout relativeLayout;
    public static Tencent mTencent;
    private boolean isFrist = true;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //申请权限
        requestPermissionsAndroidM();
//        relativeLayout = (RelativeLayout) findViewById(R.id.root_relativelayout);
        imageView = (ImageView) findViewById(R.id.app_notdata_index);
        imageView.setImageResource(R.mipmap.icon_splash_bg);
        AppUtils.showView(imageView);
        mWebView = (WebView) findViewById(R.id.webview);
        //启动动画
        startAniams();
        //设置webview
        setWebviewSetting();
        //创建附件路径
        fixDirPath();
        // set target url
        mWebView.loadUrl(ApiStores.HTML_URL);
        //
        registerReceiver();
        //微信注册操作
        if (mTencent == null) {
            mTencent = Tencent.createInstance(QQ_ID, this.getApplicationContext());
        }
        startLocation();
    }




    private void startLocation() {
        Intent intent = new Intent(GongPingOldActivity.this, SendLocationRecever.class);
        intent.setAction(AppConstant.LOCATIONREVEIVER);
        //如果已经有的话中止之前的设置新的
        PendingIntent sender = PendingIntent.getBroadcast(GongPingOldActivity.this, 0x0001, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        LogUtil.log("sss", "发送重复广播");
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 100*1000, sender);
    }

    /**
     * setting webview property include support JavaScript , Location and set UserAgent;
     */
    @SuppressLint("NewApi")
    private void setWebviewSetting() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        String ua = webSettings.getUserAgentString();
        webSettings.setUserAgentString(ua + "; hifogroup");


        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//启用地理定位
        webSettings.setGeolocationEnabled(true);
//设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
//最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebChromeClient(new MyWebChomeClient(GongPingOldActivity.this));
        mWebView.addJavascriptInterface(new JsOperator(this, this), "JsInteraction");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if ((request.getUrl().toString()).startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            request.getUrl());
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    CookieSyncManager.getInstance().sync();
                } else {
                    CookieManager.getInstance().flush();
                }
                CookieManager cookieManager = CookieManager.getInstance();
                String cookieStr = cookieManager.getCookie(url);
                startSaveToken(cookieStr);
//                LogUtil.log("CookieStr==", CookieStr);
            }
        });
    }


    private void startSaveToken(String result) {
        String token = "";
        if (!TextUtils.isEmpty(result)) {
            String[] arrays = result.split(";");
            for (int i = 0; i < arrays.length; i++) {
                if (arrays[i].contains("Token=")) {
                    token = arrays[i].substring(7);
                    break;
                }
            }
        }
        if(!TextUtils.isEmpty(token)){
            PreferenceUtil.getInstance(getApplicationContext()).saveString("token",token);
        }else{
            PreferenceUtil.getInstance(getApplicationContext()).saveString("token","");
        }
    }


    //判定是不是QQ分享。
    boolean hasQQ = false;

    @Override
    public void setShare(boolean istrue) {
        hasQQ = true;
    }

    //发送位置信息
    @Override
    public void sendLocation(String json) {
//        new HiFoToast(this,"要传递的位置" +json);
        mWebView.loadUrl("javascript:callBackGetPosition(' " + json + "')");
    }

    //支付宝支付结果回调
    @Override
    public void payResult(String value) {
        mWebView.loadUrl("javascript:goSearchHose(' " + value + "')");
    }

    /**
     * 设置微信的注册
     */
    @Override
    protected void onResume() {
        super.onResume();
//        regWx();
    }

    //当前加载的进度。。。
    @Override
    public void onProgressChanged(WebView view, int progress) {
        if (progress == 100) {
            loadComplete = true;
            if (hasLoaded) {
                AppUtils.showView(mWebView);
                AppUtils.notShowView(imageView);
//                relativeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            }
        } else {
            loadComplete = false;
        }
    }

    private boolean hasLoaded = false;
    private boolean loadComplete = false;


    //有关版本更新

    //版本更新
    private int getVersionCode() {
        int versionCode = 0;
        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    this.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    //网络上版本的信息
    private int netFalg = 0;
    //app存放的地址
    private String appUpdateUrl = "";
    //appName
    //更新文件描述
    private String conentUpdata = "";
    //版本名称
    private String versionName = "";

    //{"Data":{"AppInnerVersion":"11","AppUpdateAddress":"http:\/\/183.230.7.247:2410\/HiFoSurvrey.apk","AppUpdateDescription":"",
//"AppVersion":"1.0.0.4"},"ErrorMessage":"","Status":true}
    //获取网络上面的额版本

    public void versionProcess() {
        new TaskSendNewWork(ApiStores.appUpdate, ApiStores.GETNEWESTVERSION, null, false) {
            @Override
            public void netDataResult(String data) {
                if (data != null) {
                    LogUtil.log(data);
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.has("Version")) {
                            if (getVersionCode() != Integer.valueOf(jsonObject.getString("Version"))) {
                                netFalg = 1;
                                versionName = jsonObject.getString("Version");
                                if (jsonObject.has("Url")) {
                                    appUpdateUrl = jsonObject.getString("Url");
                                }
                                if (jsonObject.has("Description")) {
                                    conentUpdata = jsonObject.getString("Description");
                                }

                            } else {
                                netFalg = 0;
                            }
                        } else {
                            netFalg = 0;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void executeResult(Object data) {
                if (netFalg > 0) {
                    if (!TextUtils.isEmpty(appUpdateUrl)) {
                        createUpDataApp();
                    }
                }
            }
        }.execute();
    }

    private MyDialog versionUpdateDialog;

    private void createUpDataApp() {
        if (versionUpdateDialog == null) {
            String value = "";
            if (conentUpdata.contains("\\")) {
                value = conentUpdata.replace('\\', ' ');
                value = value.replace('n', '\n');
            } else {
                value = conentUpdata;
            }
            versionUpdateDialog = new MyDialog(this, R.style.dialog, value, "", this);
            versionUpdateDialog.setClickListener(dialogUpdatelistener);
            versionUpdateDialog.show();
        }
    }

    // 服务商取消认领候的操作
    private MyDialog.ClickListenerInterface dialogUpdatelistener = new MyDialog.ClickListenerInterface() {
        @Override
        public void sure(String editdata, String pricecallback) {
            if (versionUpdateDialog != null) {
                if (versionUpdateDialog.isShowing()) {
                    versionUpdateDialog.dismiss();
                    versionUpdateDialog = null;
                }
            }
//			启动下载
            Intent updateIntent = new Intent(GongPingOldActivity.this, AppUpDateService.class);
            if (!TextUtils.isEmpty(appUpdateUrl)) {
                updateIntent.putExtra("appurl", appUpdateUrl);
            } else {
                return;
            }
            if (!TextUtils.isEmpty(versionName)) {
                updateIntent.putExtra("serverVersionName", versionName);
            } else {
                return;
            }
//            updateIntent.putExtra("serverVersionName", serverVersionName);
            startService(updateIntent);
            new HiFoToast(getApplicationContext(), "后台开始下载...");
        }

        @Override
        public void cancel() {
            if (versionUpdateDialog != null) {
                if (versionUpdateDialog.isShowing()) {
                    versionUpdateDialog.dismiss();
                    versionUpdateDialog = null;
                }
            }
        }
    };

    /**
     * 启动动画  start animation
     */
    private void startAniams() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_alph_anim);
        if (animation != null) {
//            relativeLayout.startAnimation(animation);
            imageView.startAnimation(animation);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                hasLoaded = true;
                isFrist = PreferenceUtil.getInstance(getApplicationContext()).getBoolean("isFirst", true);
                if (isFrist) {
                    Intent intent = new Intent(GongPingOldActivity.this, GuildActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 0x0005);
                } else {
                    if (loadComplete) {
                        AppUtils.notShowView(imageView);
                        AppUtils.showView(mWebView);
//                        relativeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    }
                }
//                Intent intent = new Intent(GongPingOldActivity.this, GongPingOldActivity.class);
//                startActivity(intent);
//                ActivityStackManager.getInstance().exitActivity(this);
//                new android.os.Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private ImageSelectDialog dialog;

    private void startDialog() {
        if (dialog == null) {
            dialog = new ImageSelectDialog(GongPingOldActivity.this, R.style.dialog,
                    getResources().getString(R.string.takepicture), getResources().getString(R.string.albumpicture));
        }
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.AnimBottom);
        dialog.show();
        dialog.setOnCancelListener(new DialogOnCancelListener());
        dialog.setClickListener(new ImageSelectDialog.ClickListenerInterface() {
            @Override
            public void dotakePicture() {
                // end
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                addTakePhotos();
            }

            @Override
            public void docancel() {
                // 取消
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                restoreUploadMsg();
            }

            @Override
            public void doalbum() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                addSelectPhoto();
            }
        });
    }


    //注册到微信

    private void addSelectPhoto() {
        if (PermissionUtil.isOverMarshmallow()) {
            if (!PermissionUtil.isPermissionValid(GongPingOldActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(GongPingOldActivity.this, "请去\"设置\"中开启本应用的图片媒体访问权限", Toast.LENGTH_SHORT).show();
                restoreUploadMsg();
                requestPermissionsAndroidM();
                return;
            }
        }
        try {
            mSourceIntent = ImageUtil.choosePicture();
            startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(GongPingOldActivity.this, "请去\"设置\"中开启本应用的图片媒体访问权限", Toast.LENGTH_SHORT).show();
            restoreUploadMsg();
        }
    }

    /**
     * take  picture invoke camera
     */
    private void addTakePhotos() {
        if (PermissionUtil.isOverMarshmallow()) {
            if (!PermissionUtil.isPermissionValid(GongPingOldActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(GongPingOldActivity.this, "请去\"设置\"中开启本应用的图片媒体访问权限",
                        Toast.LENGTH_SHORT).show();
                restoreUploadMsg();
                requestPermissionsAndroidM();
                return;
            }
            if (!PermissionUtil.isPermissionValid(GongPingOldActivity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(GongPingOldActivity.this, "请去\"设置\"中开启本应用的相机权限", Toast.LENGTH_SHORT).show();
                restoreUploadMsg();
                requestPermissionsAndroidM();
                return;
            }
        }
        try {
            mSourceIntent = ImageUtil.takeBigPicture();
            startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(GongPingOldActivity.this, "请去\"设置\"中开启本应用的相机和图片媒体访问权限", Toast.LENGTH_SHORT).show();
            restoreUploadMsg();
        }
    }

    private boolean isshare = false;

    public boolean isshare() {
        return isshare;
    }

    public void setIsshare(boolean isshare) {
        this.isshare = isshare;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //回调启动
        if (requestCode == 0x0005) {
            AppUtils.showView(mWebView);
            AppUtils.notShowView(imageView);
//            relativeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            return;
        }
        //QQ分享回调提示
        if (mTencent != null) {
            if (hasQQ) {
                hasQQ = false;
                mTencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener(this));
            }
        }
        //如果什么都没有选的话要回调js.
        if (resultCode != Activity.RESULT_OK) {
            if (mUploadMsg != null) {
                mUploadMsg.onReceiveValue(null);
            }
            if (mUploadMsgForAndroid5 != null) {         //for android 5.0+
                mUploadMsgForAndroid5.onReceiveValue(null);
            }
            return;
        }
        //回调结果处理
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:

            case REQUEST_CODE_PICK_IMAGE: {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsg == null) {
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsg.onReceiveValue(uri);

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsgForAndroid5 == null) {        // for android 5.0+
                            return;
                        }
                        String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, data);
                        if (TextUtils.isEmpty(sourcePath) || !new File(sourcePath).exists()) {
                            Log.e(TAG, "sourcePath empty or not exists.");
                            break;
                        }
                        Uri uri = Uri.fromFile(new File(sourcePath));
                        mUploadMsgForAndroid5.onReceiveValue(new Uri[]{uri});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void openFileChooserCallBack(ValueCallback<Uri> uploadMsg, String acceptType) {
        mUploadMsg = uploadMsg;
        startDialog();
//        showOptions();
    }

    @Override
    public boolean openFileChooserCallBackAndroid5
            (WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        mUploadMsgForAndroid5 = filePathCallback;
        startDialog();
//        showOptions();
        return true;
    }

    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private class DialogOnCancelListener implements DialogInterface.OnCancelListener {
        @Override
        public void onCancel(DialogInterface dialogInterface) {
            restoreUploadMsg();
        }
    }

    private void restoreUploadMsg() {
        if (mUploadMsg != null) {
            mUploadMsg.onReceiveValue(null);
            mUploadMsg = null;

        } else if (mUploadMsgForAndroid5 != null) {
            mUploadMsgForAndroid5.onReceiveValue(null);
            mUploadMsgForAndroid5 = null;
        }
    }


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            LogUtil.log("链接地址==", url);
            DownFileInfo downFileInfo = new DownFileInfo();
            if (TextUtils.isEmpty(StoragePath.cacheDir)) {
                StoragePath.createDirs();
            }
            //首先删除然后重新创建

//            String type = url.substring(url.lastIndexOf("."), url.length());
            String name = url.substring(url.lastIndexOf('/'), url.length());
            File file = new File(StoragePath.cacheDir + File.separator + name);
            if (file.exists()) {
                startIntent(file.getAbsolutePath());
            } else {
                downFileInfo.setSavePath(file.getAbsolutePath());
                downFileInfo.setUrl(url);
                startDownFile(downFileInfo);
            }
//下载下来然后从新设置
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        }
    }

    private HttpDownManager httpDownManager;

    private void startDownFile(DownFileInfo downFileInfo) {
        if (uiOperateHandler == null) {
            uiOperateHandler = new UIOperateHandler(downListener, getMainLooper());
        }
        if (httpDownManager == null) {
            httpDownManager = HttpDownManager.getInstance();
            httpDownManager.setmHandler(uiOperateHandler);
        }
        httpDownManager.startDown(downFileInfo);
    }

    private UIOperateHandler uiOperateHandler;

    private DownOperateImp<DownFileInfo> downListener = new DownOperateImp<DownFileInfo>() {
        @Override
        public void start() {
            //当前开始下载
            LogUtil.log("开始下载");
            createWaitDialog();
        }

        @Override
        public void stop() {
            LogUtil.log("停止下载");
            if (waitDialog != null) {
                if (waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    waitDialog = null;
                }
            }
        }

        @Override
        public void success(DownFileInfo downFileInfo) {
            LogUtil.log("下载成功");
        }

        @Override
        public void fail(String error) {
            LogUtil.log("停止下载");
            if (waitDialog != null) {
                if (waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    waitDialog = null;
                }
            }
        }

        @Override
        public void saveLocal(DownFileInfo downFileInfo) {
            LogUtil.log("已经保存到凹本地");
            if (waitDialog != null) {
                if (waitDialog.isShowing()) {
                    waitDialog.dismiss();
                    waitDialog = null;
                }
            }
            startIntent(downFileInfo.getSavePath());
            //下载完成后的操作；
//            Intent intent = new Intent("android.intent.action.VIEW");
//            intent.addCategory("android.intent.category.DEFAULT");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Uri uri = Uri.fromFile(new File(downFileInfo.getSavePath()));
//            intent.setDataAndType(uri, "application/msword");
//            startActivity(intent);
        }

        @Override
        public void loading(DownFileInfo progress) {
            LogUtil.log("正在下载");
            LogUtil.log("当前的进度", progress.getProgress() + "");
        }
    };

    //正在加载
    private WaitDialog waitDialog;

    private void createWaitDialog() {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(this, R.style.wait_dialog, "正在加载");
            waitDialog.show();
        } else {
            if (!waitDialog.isShowing()) {
                waitDialog.show();
            }
        }
    }


    private void startIntent(String localPath) {
        if (!TextUtils.isEmpty(localPath)) {
            String type = localPath.substring(localPath.lastIndexOf("."), localPath.length());
            if (!TextUtils.isEmpty(type)) {
                if (type.toLowerCase().contains("doc")) {
                    startActivity(WordExcelPDFIntent.getWordFileIntent(localPath));
                } else if (type.toLowerCase().contains("excel")) {
                    startActivity(WordExcelPDFIntent.getExcelFileIntent(localPath));
                } else if (type.toLowerCase().contains("pdf")) {
                    startActivity(WordExcelPDFIntent.getPdfFileIntent(localPath));
                } else if (type.toLowerCase().contains("ppt")) {
                    startActivity(WordExcelPDFIntent.getPptFileIntent(localPath));
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case P_CODE_PERMISSIONS:
                requestResult(permissions, grantResults);
                restoreUploadMsg();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void requestPermissionsAndroidM() {
        versionProcess();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> needPermissionList = new ArrayList<>();
            needPermissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            needPermissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            needPermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            needPermissionList.add(Manifest.permission.CAMERA);
            PermissionUtil.requestPermissions(GongPingOldActivity.this, P_CODE_PERMISSIONS, needPermissionList);
        }
//        else {
//            versionProcess();
//            return;
//        }
    }

    public void requestResult(String[] permissions, int[] grantResults) {
        ArrayList<String> needPermissions = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (PermissionUtil.isOverMarshmallow()) {
                    needPermissions.add(permissions[i]);
                }
            }
        }
        if (needPermissions.size() > 0) {
            StringBuilder permissionsMsg = new StringBuilder();
            for (int i = 0; i < needPermissions.size(); i++) {
                String strPermissons = needPermissions.get(i);

                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_storage));

                } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_storage));

                } else if (Manifest.permission.CAMERA.equals(strPermissons)) {
                    permissionsMsg.append("," + getString(R.string.permission_camera));
                }
            }
            String strMessage = "请允许使用\"" + permissionsMsg.substring(1).toString() + "\"权限, 以正常使用APP的所有功能.";
            Toast.makeText(GongPingOldActivity.this, strMessage, Toast.LENGTH_SHORT).show();
        } else {
            setWebviewSetting();
            //启动更新
            versionProcess();
            return;
        }
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 800) {// 如果两次按键时间间隔大于800毫秒，则不退出
                    Toast.makeText(getApplicationContext(), "再按一次退出公评网", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;// 更新firstTime
                    return true;
                } else {
                    System.exit(0);// 否则退出程序
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    ConnectionChangeReceiver myReceiver;

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        if (myReceiver != null) {
            unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        super.onDestroy();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] mobNetInfo = connectivityManager.getAllNetworks();
                boolean hasNet = false;
                if (mobNetInfo != null && mobNetInfo.length > 0) {
                    for (int i = 0; i < mobNetInfo.length; i++) {
                        Network network = mobNetInfo[i];
                        if (network != null) {
                            NetworkInfo info = connectivityManager.getNetworkInfo(network);
                            if (info.isConnected()) {
                                hasNet = true;
                                break;
                            } else {
                                continue;
                            }
                        }
                    }
                } else {
                    AppUtils.showView(imageView);
                    imageView.setImageResource(R.mipmap.icon_notnet_bg);
                    AppUtils.notShowView(mWebView);
                    return;
                }
                if (!hasNet) {
                    //网络不可用
//					new HiFoToast(mActivity, "网络不可用,请先检查网络再试");
//                    AppUtils.showView(imageView);
//                    imageView.setImageResource(R.mipmap.icon_notnet_bg);
//                    AppUtils.notShowView(mWebView);
                    showHasNet(false);
                } else {
                    showHasNet(true);
//                    AppUtils.showView(mWebView);
//                    AppUtils.notShowView(imageView);
                }
            } else {
                NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (!mobileInfo.isConnected() && !wifiInfo.isConnected()) {
                    showHasNet(false);
//                    AppUtils.showView(imageView);
//                    imageView.setImageResource(R.mipmap.icon_notnet_bg);
//                    AppUtils.notShowView(mWebView);
//					new HiFoToast(mActivity, "网络不可用,请先检查网络再试");
//					ActivityManagers.getInstance().exitActivity(mActivity);
                } else {
                    showHasNet(true);
//                    AppUtils.showView(mWebView);
//                    AppUtils.notShowView(imageView);
                }
            }
        }
    }

    private void showHasNet(boolean netstate) {
        if (hasLoaded || loadComplete) {
            if (netstate) {
                AppUtils.showView(mWebView);
                AppUtils.notShowView(imageView);
            } else {
                AppUtils.showView(imageView);
                imageView.setImageResource(R.mipmap.icon_notnet_bg);
                AppUtils.notShowView(mWebView);
            }
        }
    }

    //动态注册广播
    public static class WXPayReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (intent.getAction().equals(AppContent.WXPAY)) {
                    if (intent.hasExtra("result")) {
                        String result = intent.getStringExtra("result");
                        mWebView.loadUrl("javascript:goSearchHose(' " + result + "')");
                    }
                }
            }
        }
    }


//    private boolean isInstall = false;
//    //判定是否安装wps
//    public void isInstall() {
//        List<PackageInfo> list = getPackageManager().getInstalledPackages(
//                PackageManager.GET_PERMISSIONS);
//
//        StringBuilder stringBuilder = new StringBuilder();
//
//        for (PackageInfo packageInfo : list) {
//            stringBuilder.append("package name:" + packageInfo.packageName
//                    + "\n");
//            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//            stringBuilder.append("应用名称:"
//                    + applicationInfo.loadLabel(getPackageManager()) + "\n");
//            if (packageInfo.permissions != null) {
//
//                for (PermissionInfo p : packageInfo.permissions) {
//                    stringBuilder.append("权限包括:" + p.name + "\n");
//                }
//            }
//            stringBuilder.append("\n");
//            if ("cn.wps.moffice_eng".equals(packageInfo.packageName)) {
//                isInstall = true;
//                LogUtil.log(isInstall +"");
//            }
//        }
//       LogUtil.log( stringBuilder.toString() +"");
//    }
}

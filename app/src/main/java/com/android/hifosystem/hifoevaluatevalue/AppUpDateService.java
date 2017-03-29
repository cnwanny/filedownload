package com.android.hifosystem.hifoevaluatevalue;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.Utils.PreferenceUtil;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.StoragePath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 类名： ${type_name}
 * 工鞥：
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class AppUpDateService extends Service {

    private Context context;
    private NotificationCompat.Builder builder;
    private NotificationManager nManager;
    private PendingIntent pendingIntent;
    private RemoteViews remoteViews;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private int sourceIcon;
    private String showAppname = "";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra("appurl")) {
                String appurl = intent.getStringExtra("appurl");
                if (intent.hasExtra("serverVersionName")) {
                    String serverVersionName = intent.getStringExtra("serverVersionName");
                    appName = "公评网" + serverVersionName + ".apk";
                    LogUtil.log("appName-->" + appName);
                    //创建消息通知设置
                    createInformation();
                    startDownLoad(appurl);
                }
//                if(intent.hasExtra("showName")){
//                    //显示名称
//                    showAppname = intent.getStringExtra("showName");
//                }
//                if(intent.hasExtra("sourceIcon")){
//                    sourceIcon = intent.getIntExtra("sourceIcon",0);
//                }
            }
        }
//        versionProcess();
        return super.onStartCommand(intent, flags, startId);
    }


    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x0001) {
                int value = 0;
                if (msg.obj != null) {
                    value = (Integer) msg.obj;
                }
                remoteViews.setTextViewText(R.id.progress_dialog_name, "正在下载公评网");
                remoteViews.setTextViewText(R.id.progress_dialog_progress_value, value + "%");
                remoteViews.setProgressBar(R.id.progress_dialog_progress, 100, value, false);
                builder.setContent(remoteViews);
                nManager.notify(100, builder.build());
            } else if (msg.what == 0x0002) {
                remoteViews.setTextViewText(R.id.progress_dialog_name, "下载完成");
                remoteViews.setTextViewText(R.id.progress_dialog_progress_value, 100 + "%");
                remoteViews.setProgressBar(R.id.progress_dialog_progress, 100, 100, false);
                builder.setContent(remoteViews);
                nManager.notify(100, builder.build());
//                notification.contentView.setTextViewText(R.id.progress_dialog_name, "下载完成");
//                notification.contentView.setTextViewText(R.id.progress_dialog_progress_value, 100 + "%");
//                notification.contentView.setProgressBar(R.id.progress_dialog_progress, 100, 100, false);
//                nManager.notify(100, notification);
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(0x0001);
            handler.removeMessages(0x0002);
            handler = null;
        }
    }


    //创建通知
    public void createInformation() {
        //定义一个PendingIntent，当用户点击通知时，跳转到某个Activity(也可以发送广播等)
        Intent intent = new Intent();
        pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.app_icon_48);
        nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.progress_content_view_dialog);
        builder.setContent(remoteViews);
        remoteViews.setTextViewText(R.id.progress_dialog_name, "开始下载");
        remoteViews.setTextViewText(R.id.progress_dialog_progress_value, "");
        remoteViews.setProgressBar(R.id.progress_dialog_progress, 100, 0, false);
        builder.setContentIntent(pendingIntent);
        nManager.notify(100, builder.build());

        Message msg = new Message();
        msg.what = 0x0001;
        msg.obj = 50;
        handler.sendMessageDelayed(msg,5000);

        Message msg2 = new Message();
        msg2.what = 0x0002;
        msg2.obj = 100;
        handler.sendMessageDelayed(msg2,10000);

    }



    private String appName = "";

    private void startDownLoad(String appUpdateUrl) {
        if (TextUtils.isEmpty(appUpdateUrl)) {
            new HiFoToast(getApplicationContext(), "下载路径不能为空");
        } else {
            String localpath;
            if (!TextUtils.isEmpty(StoragePath.apkDir)) {
                File file = new File(StoragePath.apkDir, appName);
                if (file.exists()) {
                    file.delete();
                }
                localpath = StoragePath.apkDir + "/" + appName;
            } else {
                StoragePath.createDirs();
                localpath = StoragePath.apkDir + "/" + appName;
            }
            File locaFile = new File(localpath);
            startLoading(appUpdateUrl, locaFile);
        }
    }


    private void startLoading(final String httpurl, final File localpath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int downnum = 0;//已下载的大小
                    int downcount = 0;//下载百分比
                    URL url = new URL(httpurl);
                    URLConnection connection = url.openConnection();
                    //输入流
                    InputStream inputStream = connection.getInputStream();
                    byte[] bs = new byte[1024];
                    // 读取到的数据长度
                    int length = connection.getContentLength();
                    // 输出的文件流
                    OutputStream os = new FileOutputStream(localpath);
                    // 开始读取
                    int readsize = 0;
                    while ((readsize = inputStream.read(bs)) != -1) {
                        os.write(bs, 0, readsize);
                        downnum += readsize;
                        if ((downcount == 0) || (int) (downnum * 100 / length) - 1 > downcount) {
                            downcount += 1;
                            Message msg = new Message();
                            msg.what = 0x0001;
                            msg.obj = (int) downnum * 100 / length;
                            handler.sendMessage(msg);
                        }
                        if (downnum == length) {
                            Message msg = new Message();
                            msg.what = 0x0002;
                            handler.sendMessage(msg);
                        }
                    }
                    // 完毕，关闭所有链接
                    os.close();
                    inputStream.close();
                    //执行完毕后。
                    installApk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //自动安装apk
    private void installApk() {
        File apkFile = new File(StoragePath.apkDir + "/" + appName);
        if (!apkFile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PreferenceUtil.getInstance(getApplicationContext()).saveBoolean("isFirst", false);
        i.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                "application/vnd.android.package-archive");
        startActivity(i);
    }

}



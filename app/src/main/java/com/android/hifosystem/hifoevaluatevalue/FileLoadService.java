package com.android.hifosystem.hifoevaluatevalue;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;


import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.FileOperateData;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.FileEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FileOkHttpLoadUtil;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FilePostModel;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FilePostPresenter;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FilePostPresenterView;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.StopUploadInterface;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.UploadSuccessInterface;

import java.util.ArrayList;

/**
 * 类名： FileLoadService
 * 工鞥： 上传图片功能，能够实现图片的后台上传。
 * 作者： wanny
 * 时间：20160301
 */

public class FileLoadService extends IntentService implements UploadSuccessInterface, StopUploadInterface, FilePostPresenterView {


    private static FileOkHttpLoadUtil fileUpLoadUtils;
    public FileLoadService() {
        super("FileLoadService");
    }
    //执行上传的提交的操作
    FilePostPresenter postPresenter;

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.log("FileLoadService===onCreate()");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
//        registerReceiver();
        postPresenter = new FilePostPresenter(this);
        LogUtil.log("FileLoadService===onStart()");
        if (intent != null) {
            if (intent.hasExtra("uploadFile")) {
                final ArrayList<LocalFileEntity> data = (ArrayList<LocalFileEntity>) intent.getSerializableExtra("uploadFile");
                if (data.size() > 0) {
                    fileUpLoadUtils = new FileOkHttpLoadUtil(getApplicationContext(), data);
                    fileUpLoadUtils.stopUpLoad();
                    fileUpLoadUtils.setRunState();
                    fileUpLoadUtils.setUploadSuccessInterface(FileLoadService.this);
                    fileUpLoadUtils.setStopUploadInterface(FileLoadService.this);
                    if (fileUpLoadUtils != null) {
                        fileUpLoadUtils.uoloadFile();
                    }
                }
            }
        }
    }

    @Override
    public void filePostSuccess(FilePostModel model, LocalFileEntity localFileEntity) {
        if (model != null) {
            if (model.isStatus()) {
                index++;
                LogUtil.log("提交成功////提交数量", "" + index);
                //上传成功后删除本地的文件
                //删除数据库中的文件
                deleteOndeData(localFileEntity, getBaseContext());
            } else {
                //如果没有上传成功的话。
                LogUtil.log("提交失败", "" + index);
//            submitFileInfo(fileEntity,newremotepath);
            }
        } else {
            //如果没有上传成功的话。
            LogUtil.log("提交失败", "" + index);
//            submitFileInfo(fileEntity,newremotepath);
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.log("FileLoadService===onStartCommand()");
        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.log("FileLoadService===onDestroy()");
//        if (fileUpLoadUtils != null) {
//            fileUpLoadUtils.stopUpLoad();
//        }
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.log("FileLoadService ===onLowMemory()");
    }

    @Override
    public void uploadsuccess(LocalFileEntity fileEntity, String newremotepath, int position) {
        LogUtil.log("new remote-path === " + newremotepath);
        if (!TextUtils.isEmpty(newremotepath) && newremotepath.contains(".")) {
            if (fileUpLoadUtils != null) {
                if (fileUpLoadUtils.getStatus()) {
                    postPresenter.submitFileInfo(setPostData(fileEntity, newremotepath), fileEntity);
                }
            }
        }
    }

    @Override
    public void startUpload(LocalFileEntity fileEntity, int position) {

    }

    /**
     * 停止附件的上传
     */
    public void stopThread() {
        if (fileUpLoadUtils != null) {
            fileUpLoadUtils.stopUpLoad();
        }

    }

    private int index = 0;

    private synchronized void deleteOndeData(LocalFileEntity fileEntity, Context context) {
        FileOperateData fileOperateData = FileOperateData.getInstance(context.getApplicationContext());
        fileOperateData.deleteOneFile(fileEntity);

    }
    private String fid = "";
    //设置上传的传递对象；
    private ArrayList<FileEntity> setPostData(LocalFileEntity localFileEntity, String newFile) {
        ArrayList<FileEntity> result = new ArrayList<>();
        FileEntity fileEntity = new FileEntity();
        fileEntity.setCategoryName(localFileEntity.getCategoryName());
        fileEntity.setCategoryId(localFileEntity.getCategoryId());
        fileEntity.setFastPath(newFile);
        fileEntity.setFID(localFileEntity.getFID());
        fileEntity.setExtension(localFileEntity.getExtension());
        fileEntity.setFileName(localFileEntity.getFileName());
        fileEntity.setUserAccount(localFileEntity.getUserAccount());
        fileEntity.setUserName(localFileEntity.getUserName());
        result.add(fileEntity);
        fid = localFileEntity.getFID();
        return result;
    }


    public static class StopUploadReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.log("收到广播");
            //发送广播的时候要判定是不是停止已经上传完成。
            if (fileUpLoadUtils != null) {
                fileUpLoadUtils.stopUpLoad();
                fileUpLoadUtils = null;
                //停止上传的是不是当前执行的。
//                if(intent.hasExtra("pid")){
//                    String fid = intent.getStringExtra("pid");
//                    if(fid.equals(fid)){
//                    }
//                }
//                //停止上传清空数据；
//                fileUpLoadUtils.stopUpLoad();
                LogUtil.log("停止上传附件广播");
            } else {
                LogUtil.log("没有附件要上传");
            }
        }
    }

    @Override
    public void stop() {
        stopThread();
    }
}

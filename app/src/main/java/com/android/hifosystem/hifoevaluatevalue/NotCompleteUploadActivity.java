package com.android.hifosystem.hifoevaluatevalue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.AutoView.WaitDialog;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.FileOperateData;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.FileEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FileOkHttpLoadUtil;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FilePostModel;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.StopUploadInterface;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.UploadSuccessInterface;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.MvpActivity;
import com.android.hifosystem.hifoevaluatevalue.notupload_mvp.NotUpLoadPresenter;
import com.android.hifosystem.hifoevaluatevalue.notupload_mvp.NotUpLoadView;
import com.android.hifosystem.hifoevaluatevalue.notupload_mvp.UpLoadImageAdapter;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名： NotCompleteUploadActivity
 * 工鞥： 未上传的图片界面
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class NotCompleteUploadActivity extends MvpActivity<NotUpLoadPresenter> implements UploadSuccessInterface, StopUploadInterface, NotUpLoadView {

    //返回
    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;
    //标题
    @BindView(R.id.title_title_text)
    TextView titleTitleText;
    //完成列表
    @BindView(R.id.notupload_complete_listview)
    ListView notuploadCompleteListview;
    //没有数据
    @BindView(R.id.notupload_complete_notdata)
    TextView notuploadCompleteNotdata;
    private ArrayList<LocalFileEntity> data;
    //
    private UpLoadImageAdapter adapter;
    //
    private String username = "";
    //上传文件的封装
    private FileOkHttpLoadUtil fileUpLoadUtils;
    private FileOperateData fileOperateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_complete_image_activity_view);
        ButterKnife.bind(this);
        fileOperateData = FileOperateData.getInstance(mContext);
        initData();
        fileUpLoadUtils = new FileOkHttpLoadUtil(getApplicationContext());
    }

    private void initData() {
        if (getIntent() != null) {
            if(getIntent().hasExtra("username")){
                username = getIntent().getStringExtra("username");
            }
        }
        data = new ArrayList<>();
        adapter = new UpLoadImageAdapter(data, mContext, mActivity);
        if (adapter != null) {
            notuploadCompleteListview.setAdapter(adapter);
        }
        if (titleTitleText != null) {
            titleTitleText.setText("未上传的照片");
        }
        new CheckDataThread().start();
    }


    /**
     * 内部类启动线程来查询数据
     */
    private class CheckDataThread extends Thread {
        @Override
        public void run() {
            super.run();
            //线程开始运行
            ArrayList<LocalFileEntity> result;
            if (!TextUtils.isEmpty(username)) {
                result = fileOperateData.findByUserName(username);
            } else{
                return;
            }
            data.addAll(result);
            Message message = new Message();
            message.what = 0x0001;
            if (mHandler != null) {
                mHandler.sendMessage(message);
            }
        }
    }

    //加载完成后开始上传
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x0001) {
                if (data.size() > 0) {
                    AppUtils.notShowView(notuploadCompleteNotdata);
                } else {
                    AppUtils.showView(notuploadCompleteNotdata);
                }
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                //开始上传操作
                startUpLoad();
            } else if (msg.what == 0x0003) {
                adapter.updataView(0, notuploadCompleteListview, 0, "0%");
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(0x0006, 100);
                }
            } else if (msg.what == 0x0006) {
                adapter.updataView(0, notuploadCompleteListview, 50, "50%");
                this.sendEmptyMessageDelayed(0x0008, 100);
            } else if (msg.what == 0x0008) {
                adapter.updataView(0, notuploadCompleteListview, 75, "75%");
            } else if (msg.what == 0x0009) {
                adapter.updataView(0, notuploadCompleteListview, 100, "100%");
                if (data.size() > 0) {
                    data.remove(0);
                    adapter.notifyDataSetChanged();
                } else {
                    AppUtils.showView(notuploadCompleteNotdata);
                    new HiFoToast(mContext, "图片已经上传完成");
                }
            }
        }
    };

    @OnClick(R.id.title_left_image)
    void backActivity(View view) {
        if (fileUpLoadUtils != null) {
            fileUpLoadUtils.stopUpLoad();
        }
        if (data.size() > 0) {
            checkData();
        }
//        checkData();
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

    private void startUpLoad() {
        //先停止掉后再启动。
        fileUpLoadUtils.setRunState();
        fileUpLoadUtils.setUploadFile(data);
        fileUpLoadUtils.setUploadSuccessInterface(NotCompleteUploadActivity.this);
        fileUpLoadUtils.setStopUploadInterface(NotCompleteUploadActivity.this);
        if (fileUpLoadUtils != null) {
            fileUpLoadUtils.uoloadFile();
        }
    }

    @Override
    public void uploadsuccess(LocalFileEntity fileEntity, String newremotepath, int position) {
        //更新数据
        if (mHandler != null) {
            mHandler.sendEmptyMessage(0x0009);
        }
        //同时上传操作
        if (!TextUtils.isEmpty(newremotepath) && newremotepath.contains(".")) {
            mvpPresenter.submitFileInfo(setPostData(fileEntity, newremotepath), fileEntity);
        }
    }


    //设置上传的传递对象；
    private String fid = "";

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


    //开始上传。
    @Override
    public void startUpload(LocalFileEntity fileEntity, int position) {
        if (position != -1) {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(0x0003);
            }

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fileUpLoadUtils != null) {
            fileUpLoadUtils.stopUpLoad();
        }
        if (mHandler != null) {
            mHandler.removeMessages(0x0001);
            mHandler.removeMessages(0x0003);
            mHandler.removeMessages(0x0006);
            mHandler.removeMessages(0x0008);
            mHandler.removeMessages(0x0009);
            mHandler = null;
        }
    }

    @Override
    public void stop() {
        if (fileUpLoadUtils != null) {
            fileUpLoadUtils.stopUpLoad();
        }
    }

    private int index = 0;

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
                LogUtil.log("提交失败,重新提交", "" + index);
                deleteOndeData(localFileEntity, getBaseContext());
            }
        } else {
            //如果没有上传成功的话。
            LogUtil.log("提交失败,重新提交", "" + index);
            deleteOndeData(localFileEntity, getBaseContext());

        }
    }

    //判定是不是最后一条数据如果是的话，执行完成的那个操作



    private synchronized void deleteOndeData(LocalFileEntity fileEntity, Context context) {
        FileOperateData fileOperateData = FileOperateData.getInstance(context.getApplicationContext());
        fileOperateData.deleteOneFile(fileEntity);
//        boolean isLastDate = fileOperateData.findHasImage(fileEntity.getFID());

    }


    @Override
    protected NotUpLoadPresenter createPresenter() {
        return new NotUpLoadPresenter(this);
    }

    private WaitDialog waitDialog;

    private void checkData() {
        if (waitDialog == null) {
            waitDialog = new WaitDialog(mActivity, R.style.dialog, "正在获取上传附件信息");
        } else {
            if (waitDialog != null) {
                if (waitDialog.isShowing()) {
                    waitDialog.dismiss();
                }
            }
            waitDialog.show();
        }
        ArrayList<LocalFileEntity> result;
        if (!TextUtils.isEmpty(username)) {
            result = fileOperateData.findByUserName(username);
        }else{
            return;
        }
        if (result != null && result.size() > 0) {
            Intent intent = new Intent(NotCompleteUploadActivity.this, FileLoadService.class);
            intent.putExtra("uploadFile", result);
            startService(intent);
        } else {
            new HiFoToast(mContext, "照片已经上传完成");
        }
        if (waitDialog != null) {
            if (waitDialog.isShowing()) {
                waitDialog.dismiss();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            点击返回的话先让线程给停止了，然后再
            if (fileUpLoadUtils != null) {
                fileUpLoadUtils.stopUpLoad();
            }
            if (data.size() > 0) {
                checkData();
            }
            ActivityStackManager.getInstance().exitActivity(mActivity);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

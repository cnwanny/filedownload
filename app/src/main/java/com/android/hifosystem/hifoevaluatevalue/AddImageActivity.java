package com.android.hifosystem.hifoevaluatevalue;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.FileOperateData;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.AddSurveyImagePresenter;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.AddSurveyView;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.SurveyFileModel;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.FileEntity;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.PhotoInfoEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.FileOpt;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.MvpActivity;
import com.android.hifosystem.hifoevaluatevalue.recycle_pages.AdapterListener;
import com.android.hifosystem.hifoevaluatevalue.recycle_pages.HeaderAndBottomAdapter;
import com.android.hifosystem.hifoevaluatevalue.recycle_pages.RecycleItemDeciration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名： AddImageActivity
 * 功能：
 * 作者： wanny
 * 时间： 17:25 2016/11/21
 */
public class AddImageActivity extends MvpActivity<AddSurveyImagePresenter> implements AddSurveyView, HeaderAndBottomAdapter.AddAndDeleteSurveyImgeListener, AdapterListener {

    @BindView(R.id.title_left_image)
    ImageView titleLeftImage;

    @BindView(R.id.title_title_text)
    TextView titleTitleText;
    //
    @BindView(R.id.survey_addimage_recyclerView)
    RecyclerView surveyAddimageRecyclerView;

    @BindView(R.id.title_right_text)
    TextView titleRightText;
    private FileOperateData fileOperateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_addimage_activity);
        ButterKnife.bind(this);
        initData();
        initView();
        //获取服务器附件
        mvpPresenter.getSurveyFile(surveyId);
    }

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void getDataSuccess(SurveyFileModel surveyFileModel) {
        if (surveyFileModel != null) {
            if (!surveyFileModel.isStatus()) {
                return;
            }
            if (surveyFileModel.getResult() == null) {
                return;
            }
            ArrayList<FileEntity> entities = surveyFileModel.getResult();
            if (entities.size() > 0) {
                existSurveyFiles.addAll(entities);
                titleRightText.setText("已存在附件(" + existSurveyFiles.size() + ")");
            }
        }

    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    protected AddSurveyImagePresenter createPresenter() {
        return new AddSurveyImagePresenter(this);
    }

    private void initView() {
        if (titleTitleText != null) {
            titleTitleText.setText("房屋状况");
        }
        if (titleRightText != null) {
            AppUtils.showView(titleRightText);
            titleRightText.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        }
    }

    private ArrayList<LocalFileEntity> datas;
    private String username;
    private String surveyId;
    private ArrayList<AttchmentEntity> housr;
    private ArrayList<FileEntity> existSurveyFiles;

    private void initData() {
        fileOperateData = FileOperateData.getInstance(getApplication());
        if (datas == null) {
            datas = new ArrayList<>();
        } else {
            datas.clear();
        }
        if (existSurveyFiles == null) {
            existSurveyFiles = new ArrayList<>();
        }
        if (housr == null) {
            housr = new ArrayList<>();
        }
        if (getIntent().hasExtra("username")) {
            username = getIntent().getStringExtra("username");
        }
        if (getIntent().hasExtra("surveyId")) {
            surveyId = getIntent().getStringExtra("surveyId");
        }
        if (getIntent().hasExtra("category")) {
            housr = getIntent().getParcelableArrayListExtra("category");
        }
        ArrayList<LocalFileEntity> value = fileOperateData.find(surveyId, username);
        if (value.size() > 0 ) {
            datas.clear();
            datas.addAll(value);
        }
        LocalFileEntity localFileEntity = new LocalFileEntity();
        localFileEntity.setCategoryName("addImage");
        datas.add(localFileEntity);
        setRecyclerViewData();
    }


    private HeaderAndBottomAdapter adapter;

    private void setRecyclerViewData() {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getBaseContext(), 4);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridLayoutManager.setReverseLayout(false);
        surveyAddimageRecyclerView.setLayoutManager(gridLayoutManager);
        adapter = new HeaderAndBottomAdapter(datas, this);
        if (adapter != null) {
            surveyAddimageRecyclerView.setAdapter(adapter);
            adapter.setAddSurveyImgeListener(this);
            adapter.setAdapterListener(this);
        }
        surveyAddimageRecyclerView.addItemDecoration(new RecycleItemDeciration(this, RecycleItemDeciration.VERTICAL_LIST));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (adapter.isHeaderView(position) || adapter.isBottomView(position)) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }


    //添加附件
    @Override
    public void addImage(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(AddImageActivity.this, AutoCamera2Activity.class);
            intent.putParcelableArrayListExtra("typelist", housr);
            intent.putExtra("fid", surveyId);
            intent.putExtra("username", username);
            startActivityForResult(intent, 0X0005);
        } else {
            Intent intent = new Intent(AddImageActivity.this, AutoCameraActivity.class);
            intent.putParcelableArrayListExtra("typelist", housr);
            intent.putExtra("fid", surveyId);
            intent.putExtra("username", username);
            startActivityForResult(intent, 0X0005);
        }
    }


    //提交查勘
    @Override
    public void submit() {
        Message message = new Message();
        message.what = 0x0009;
        if (mHandler != null) {
            mHandler.sendMessage(message);
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x0009) {
                final ArrayList<LocalFileEntity> value = fileOperateData.findByUserName(username);
                //如果此时有附件正在后台上传的话，发送广播先停止附件的上传，然后再启动新的附件上传操作。
                if (value != null && value.size() > 0) {
                    Intent intentreceiver = new Intent();
                    intentreceiver.setAction(AppContent.StopReceiverAction);     //设置Action
                    sendBroadcast(intentreceiver);
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(AddImageActivity.this, FileLoadService.class);
                        intent.putParcelableArrayListExtra("uploadFile", value);
                        //开始启动后台上传
                        startService(intent);
                        new HiFoToast(mActivity, "附件将会在后台上传");
                        setResult(0x0005);
                        ActivityStackManager.getInstance().exitActivity(mActivity);
                    }
                }, 500);
            }
        }
    };


    private boolean isOperateSuccess = false;

    @Override
    public void delete(int position) {
        //删除文件
        LogUtil.log("开始回调删除", "启动删除");
        if (!isOperateSuccess) {
            isOperateSuccess = true;
            deleteImage(position);
        }
    }

    @Override
    public void onLongClick() {
        adapter.setOperateMode(HeaderAndBottomAdapter.OPETATE_EDIT);
        operateMode = HeaderAndBottomAdapter.OPETATE_EDIT;
        titleRightText.setText("完成");
    }

    @Override
    public void skanImage(int mode, int position) {
        if (position != -1 && position != datas.size() - 1) {
            Intent intent = new Intent(AddImageActivity.this, GallaryBigMapActivity.class);
            intent.putExtra("local_path", "local_path");
            ArrayList<PhotoInfoEntity> photoInfoEntities = new ArrayList<>();
            PhotoInfoEntity photoInfoEntity;
            for (LocalFileEntity entity : datas) {
                if (!TextUtils.isEmpty(entity.getLocalFilePath())) {
                    photoInfoEntity = new PhotoInfoEntity();
                    photoInfoEntity.setPath_local(entity.getLocalFilePath());
                    photoInfoEntities.add(photoInfoEntity);
                }
            }
            intent.putExtra("position", position);
            intent.putParcelableArrayListExtra("imagelist", photoInfoEntities);
            startActivity(intent);
        }
    }

    @OnClick(R.id.title_right_text)
    void setDelete(View view) {
        if (operateMode == HeaderAndBottomAdapter.OPERATE_NORMAl) {
            if (existSurveyFiles.size() > 0) {
                startImageDetail();
            }
        } else {
            if (adapter != null) {
                adapter.setOperateMode(HeaderAndBottomAdapter.OPERATE_NORMAl);
                operateMode = HeaderAndBottomAdapter.OPERATE_NORMAl;
                titleRightText.setText("已存在附件(" + existSurveyFiles.size() + ")");
            }
        }
    }


    private void startImageDetail() {
        if (existSurveyFiles.size() > 0) {
            ArrayList<PhotoInfoEntity> authorityImages = new ArrayList<>();
            for (FileEntity entity : existSurveyFiles) {
                PhotoInfoEntity value = new PhotoInfoEntity();
                if (!TextUtils.isEmpty(entity.getExtension()) && (entity.getExtension().toLowerCase().equals(".png") || entity.getExtension().toLowerCase().equals(".jpg"))) {
                    value.setCategoryName(entity.getCategoryName());
                    value.setPath_local(entity.getFastPath());
                    authorityImages.add(value);
                }
            }
            Intent intent = new Intent(AddImageActivity.this, ImageAddActivity.class);
            intent.putExtra("mode", HeaderAndBottomAdapter.MODE_SHOW);
            intent.putParcelableArrayListExtra("images", authorityImages);
            intent.putExtra("classify", "已经上传的附件");
            startActivity(intent);
        }
    }

    private int operateMode = HeaderAndBottomAdapter.OPERATE_NORMAl;

    private void deleteImage(int position) {
        if (position < datas.size()) {
            LocalFileEntity entity = datas.get(position);
            fileOperateData.deleteOneFile(entity);
            FileOpt.deleteFile(entity.getLocalFilePath());
            //同时要删除本地操作
            datas.remove(position);
            adapter.notifyDataSetChanged();
            if (datas.size() == 1) {
                operateMode = HeaderAndBottomAdapter.OPERATE_NORMAl;
                adapter.setOperateMode(HeaderAndBottomAdapter.OPERATE_NORMAl);
                titleRightText.setText("已存在附件(" + existSurveyFiles.size() + ")");
            }
            isOperateSuccess = false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x0005 && resultCode == 0x0003) {
            checkFileByFid();
        }
    }

    private void checkFileByFid() {
        //查询数据库
        ArrayList<LocalFileEntity> value = fileOperateData.find(surveyId, username);
        if (value.size() > 0 ) {
            datas.clear();
            datas.addAll(value);
            datas.add(datas.size(), new LocalFileEntity());
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//
//            return true;
//        }

        return super.onKeyDown(keyCode, event);
    }

    // 返回
    @OnClick(R.id.title_left_image)
    void onClickBack(View v) {
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

}

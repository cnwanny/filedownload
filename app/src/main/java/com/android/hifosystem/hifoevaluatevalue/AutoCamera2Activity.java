package com.android.hifosystem.hifoevaluatevalue;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.FileOperateData;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.FileOpt;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.Utils.NewPremissionUtils;
import com.android.hifosystem.hifoevaluatevalue.camera_view.ColumnHorizontalScrollView;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera.Camera2Interface;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera.Camera2SurfaceText;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.CameraPresenter;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.CameraView;
import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_care.AppContent;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.SDCardImageLoader;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.MvpActivity;
import com.psnl.hzq.tool.TimeEx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名： ${type_name}
 * 工鞥：
 * 作者： wanny
 * 时间：${date} ${time}
 */


public class AutoCamera2Activity extends MvpActivity<CameraPresenter> implements CameraView, TextureView.SurfaceTextureListener {

    //拍照预览页面
    @BindView(R.id.auto_surfaceView2)
    Camera2SurfaceText autoSurfaceView;
    //radiogroup布局
    @BindView(R.id.mRadioGroup_content2)
    LinearLayout mRadioGroupContent;
    //横向滚动的scrollview
    @BindView(R.id.mColumnHorizontalScrollView2)
    ColumnHorizontalScrollView mColumnHorizontalScrollView;
    /**
     * 左阴影部分
     */
    @BindView(R.id.shade_left2)
    ImageView shadeLeft;
    /**
     * 左阴影部分
     */
    @BindView(R.id.shade_right2)
    ImageView shadeRight;
    //更多的操作
    @BindView(R.id.rl2_column)
    RelativeLayout rlColumn;
    //预览
    @BindView(R.id.skan_image2)
    ImageView skanImage;
    //拍照
    @BindView(R.id.take_picture_image2)
    ImageView takePictureImage;
    //取消拍照
    @BindView(R.id.cancel_takepicture2)
    TextView cancelTakepicture;
    //底部布局
    @BindView(R.id.auto2_bottom)
    LinearLayout autoBottom;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;
    /**
     * Item宽度
     */
    private int mItemWidth = 0;
    /**
     * 当前选中的栏目
     */
    private int columnSelectIndex = 0;
    private ArrayList<AttchmentEntity> typechannelList;
    //当前的选中的key
    private int currentKey;
    //当前选中的分类
    private String currentCategory = "";

    //相机类
    private float previewRate;
    //分类文件
    private String projectId = "";
    private String userName = "";
    private SDCardImageLoader imageLoader;
    private Camera2Interface camera2Operate;
    private CameraShowHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_camera2_activity_view);
        ButterKnife.bind(this);
        isFirst = true;
        imageLoader = new SDCardImageLoader();
        initData();
        initCamera();
        requsetPremission();
        //获取分类
        mColumnHorizontalScrollView.setScrollViewChangeListener(scrollViewChangeListener);
    }


    @Override
    protected CameraPresenter createPresenter() {
        return new CameraPresenter(this);
    }

    private void setCamera() {
        if (mBackgroundHandler != null) {
            camera2Operate = new Camera2Interface(mContext, mActivity, mBackgroundHandler, autoSurfaceView);
            camera2Operate.setmHandler(mBackgroundHandler);
        } else {
            camera2Operate = new Camera2Interface(mContext, mActivity, null, autoSurfaceView);
        }
        autoSurfaceView.setSurfaceTextureListener(this);
        camera2Operate.setTakePictureListener(takePictureListener);

        if (autoSurfaceView.isAvailable()) {
            camera2Operate.openCamera(autoSurfaceView.getWidth(), autoSurfaceView.getHeight());
        } else {
            autoSurfaceView.setSurfaceTextureListener(this);
        }
    }

    //是否开启预览模式
    private boolean hasCanve = false;
    //第一次启动
    private boolean isFirst = false;

    private void initData() {
        if (typechannelList == null) {
            typechannelList = new ArrayList<>();
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra("fid")) {
                projectId = getIntent().getStringExtra("fid");
            }
            if (getIntent().hasExtra("typelist")) {
                typechannelList = getIntent().getParcelableArrayListExtra("typelist");
            }
            if (getIntent().hasExtra("username")) {
                userName = getIntent().getStringExtra("username");
            }
        }
        mScreenWidth = AppUtils.getScreenWidthPiex(mActivity);
        mItemWidth = mScreenWidth / 3;//单个的宽度
        initTabColumn();
    }


    //拍照后保存图片；
    private Camera2Interface.TakePictureResultCallBack takePictureListener = new Camera2Interface.TakePictureResultCallBack() {
        @Override
        public void takePicture(ImageReader imageReader) {
            //            //图片拍照成功
            LogUtil.log("takeSuccess_path");
            //获取了照片然后将图片添加到数据库中。
            if (imageReader != null) {
                mBackgroundHandler.post(new SaveImage(imageReader));
            } else {
                isRunning = false;
            }
        }
    };


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (camera2Operate != null) {
            camera2Operate.closeCamera();
        }
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    protected void onStop() {
        hasCanve = false;
        isFirst = false;
        super.onStop();
    }


    private NewPremissionUtils newPremissionUtils;
    private boolean hasCamera;

    private void requsetPremission() {
        if (newPremissionUtils == null) {
            newPremissionUtils = new NewPremissionUtils(mActivity);
        }
        boolean isNeed = newPremissionUtils.hasNeedReqset();
        if (isNeed) {
            hasCamera = newPremissionUtils.requestSDCardCameraPremission(0x0009, AppContent.CAMREA_REQUESTCODE, AppContent.STORAGE_REQUESTCODE);
            if (hasCamera) {
                setCamera();
            }
        } else {
            setCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        operatePremission(requestCode, grantResults);
    }

    private void operatePremission(int requestCode, int[] grantResults) {
        if (requestCode == AppContent.CAMREA_REQUESTCODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                new HiFoToast(mActivity, "请手动授权拍照权限，否则不能拍照");
                ActivityStackManager.getInstance().exitActivity(mActivity);
            } else {
                hasCamera = true;
                setCamera();
            }
        } else if (requestCode == AppContent.STORAGE_REQUESTCODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                new HiFoToast(mActivity, "请手动开启文件读写权限，再试");
                ActivityStackManager.getInstance().exitActivity(mActivity);
            } else {
                hasCamera = true;
                setCamera();
            }
        } else if (requestCode == 0x0009) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                new HiFoToast(mActivity, "请手动授权文件读写和拍照，否则不能拍照");
                ActivityStackManager.getInstance().exitActivity(mActivity);
            } else {
                hasCamera = true;
                setCamera();
            }
        }
    }

    private boolean dataHasChange = false;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class SaveImage implements Runnable {

        private ImageReader imageReader;

        public SaveImage(ImageReader imageReader) {
            this.imageReader = imageReader;
        }

        @Override
        public void run() {
            String path = FileOpt.initPath();
            String imagePath = path + "/" + TimeEx.getStringTime14() + ".jpg";
            Image mImage = imageReader.acquireNextImage();
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(imagePath);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                isRunning = false;
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!TextUtils.isEmpty(imagePath)) {
                Message message = new Message();
                message.what = 0x0001;
                message.obj = imagePath;
                if (mHandler != null) {
                    mHandler.sendMessage(message);
                }
                //创建数据然后添加到数据库中
                //创建数据然后添加到数据库中
                LocalFileEntity fileEntity = new LocalFileEntity();
                fileEntity.setCategoryId(Integer.valueOf(currentKey));
                fileEntity.setFID(projectId);
                fileEntity.setCategoryName(currentCategory);
                fileEntity.setLocalFilePath(imagePath);
                String name = imagePath.substring(imagePath.lastIndexOf("/"), imagePath.length());
                fileEntity.setFileName(name);
                fileEntity.setUserAccount(userName);
                fileEntity.setUserName(userName);
                fileEntity.setExtension(name.substring(name.lastIndexOf("."), name.length()));
                FileOperateData fileOperateData = FileOperateData.getInstance(mContext);
                fileOperateData.inserData(fileEntity);
                dataHasChange = true;
            }
            isRunning = false;
        }
    }


    private void initCamera() {
        previewRate = AppUtils.getScreenRate(mContext);
        ViewGroup.LayoutParams params = autoSurfaceView.getLayoutParams();
        Point p = AppUtils.getScreenMetrics(mContext);
        params.width = p.x;
        params.height = p.y;
        previewRate = AppUtils.getScreenRate(this); //默认全屏的比例预览
        autoSurfaceView.setLayoutParams(params);
    }


    private HandlerThread mBackgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * Starts a background thread and its {@link Handler}.
     */

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
        mHandler = new CameraShowHandler(getMainLooper(), skanImage, mActivity);
    }


    /**
     * Stops the background thread and its {@link Handler}.
     */

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void stopBackgroundThread() {
        if (mHandler != null) {
            mHandler.removeMessages(0x0001);
            mHandler = null;
        }
        if (mBackgroundThread != null) {
            mBackgroundThread.quitSafely();
            try {
                mBackgroundThread.join();
                mBackgroundThread = null;
                mBackgroundHandler = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    @OnTouch(R.id.auto_surfaceView2)
//    boolean onTouch(View v, MotionEvent event) {
////        CameraInterface.getInstance().setmCameraAuto();
//        return false;
//    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        LogUtil.log("width==", i + "");
        LogUtil.log("height==", i1 + "");
        int width;
        int height;
        if (camera2Operate != null) {
            if (i >= 720) {
                width = 720;
                height = 1280;
            } else {
                width = i;
                height = i1;
            }
            camera2Operate.openCamera(width, height);
        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {
        LogUtil.log("width==", i + "");
        LogUtil.log("height==", i1 + "");
        int width;
        int height;
        if (camera2Operate != null) {
            if (i >= 720) {
                width = 720;
                height = 1280;
            } else {
                width = i;
                height = i1;
            }
            camera2Operate.configureTransform(width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    private ColumnHorizontalScrollView.ScrollViewChangeListener scrollViewChangeListener = new ColumnHorizontalScrollView.ScrollViewChangeListener() {
        @Override
        public void startChange(int lastX) {
            for (int i = 0; i < mRadioGroupContent.getChildCount(); i++) {
                View localView = mRadioGroupContent.getChildAt(i);
                //屏幕的正中央
                LogUtil.log("lastX", lastX + "");
                LogUtil.log("startLeft", localView.getLeft() + "");
                LogUtil.log("item_width", localView.getWidth() + "");
                if (Math.abs(lastX % localView.getWidth()) < localView.getMeasuredWidth()) {
                    int position = lastX / localView.getMeasuredWidth() + 1;
                    selectTab(position);
                    return;
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
    }

    private boolean isRunning = false;

    @OnClick(R.id.take_picture_image2)
    void startTakePicture(View view) {
        if (!isRunning) {
            try {
                camera2Operate.takePicture();
                isRunning = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化Column栏目项
     */

    private void initTabColumn() {
        mRadioGroupContent.removeAllViews();
        int count = typechannelList.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroupContent, shadeLeft, shadeRight, rlColumn);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
//			TextView localTextView = (TextView) mInflater.inflate(R.layout.column_radio_item, null);
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            //设置tag
            columnTextView.setTag(typechannelList.get(i).getId());
            //设置文本
            columnTextView.setText(typechannelList.get(i).getName());
            columnTextView.setTextColor(ContextCompat.getColor(mContext, R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
                currentKey = (int) columnTextView.getTag();
                currentCategory = columnTextView.getText().toString().trim();
            }
            columnTextView.setOnClickListener(radio_itemClickListener);
            mRadioGroupContent.addView(columnTextView, i, params);
        }
    }


    private View.OnClickListener radio_itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mRadioGroupContent.getChildCount(); i++) {
                TextView localView = (TextView) mRadioGroupContent.getChildAt(i);
                if (localView != v)
                    localView.setSelected(false);
                else {
                    localView.setSelected(true);
                    currentKey = (int) localView.getTag();
                    currentCategory = localView.getText().toString().trim();
                    LogUtil.log("keyvalue", currentKey + "");
                }
            }

        }
    };


    /**
     * 选择的Column里面的Tab
     */

    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroupContent.getChildCount(); i++) {
            View checkView = mRadioGroupContent.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j < mRadioGroupContent.getChildCount(); j++) {
            TextView checkView = (TextView) mRadioGroupContent.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
                currentKey = (int) checkView.getTag();
                currentCategory = checkView.getText().toString().trim();
                LogUtil.log("keyvalue", currentKey + "");
            } else {
                ischeck = false;

            }
            checkView.setSelected(ischeck);
        }
    }

    @OnClick(R.id.cancel_takepicture2)
    void backActivity(View view) {
        saveData();
    }

    private void saveData() {
        camera2Operate.closeCamera();
        if (dataHasChange) {
            setResult(0x0003);
        }
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveData();
        }
        return super.onKeyDown(keyCode, event);
    }

}


package com.android.hifosystem.hifoevaluatevalue;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.AutoView.HiFoToast;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.FileOperateData;
import com.android.hifosystem.hifoevaluatevalue.DBFileImage.FileDB.LocalFileEntity;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.android.hifosystem.hifoevaluatevalue.camera_view.ColumnHorizontalScrollView;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera.CameraInterface;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera.CameraSurfaceView;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.CameraPresenter;
import com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp.CameraView;
import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_fileoperate.SDCardImageLoader;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.MvpActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

/**
 * 类名： ${type_name}
 * 工鞥：
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class AutoCameraActivity extends MvpActivity<CameraPresenter> implements CameraView {


    @BindView(R.id.auto_surfaceView)
    CameraSurfaceView autoSurfaceView;
    //radiogroup布局
    @BindView(R.id.mRadioGroup_content)
    LinearLayout mRadioGroupContent;
    //横向滚动的scrollview
    @BindView(R.id.mColumnHorizontalScrollView)
    ColumnHorizontalScrollView mColumnHorizontalScrollView;
    /**
     * 左阴影部分
     */
    @BindView(R.id.shade_left)
    ImageView shadeLeft;
    /**
     * 右阴影部分
     */
    @BindView(R.id.shade_right)
    ImageView shadeRight;
    //更多的操作
    @BindView(R.id.rl_column)
    RelativeLayout rlColumn;
    //预览
    @BindView(R.id.skan_image)
    ImageView skanImage;
    //拍照
    @BindView(R.id.take_picture_image)
    ImageView takePictureImage;
    //取消拍照
    @BindView(R.id.cancel_takepicture)
    TextView cancelTakepicture;
    //底部布局
    @BindView(R.id.auto_bottom)
    LinearLayout autoBottom;
    private boolean dataHasChange = false;


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
    private WindowManager mWindowManager;
    private float previewRate;
    //分类文件
    private String projectId = "";
    private String userName = "";
    private SDCardImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startOpenCamera();
        isFirst = true;
        setContentView(R.layout.modify_camera_activity_view);
        ButterKnife.bind(this);
        imageLoader = new SDCardImageLoader();
        initData();
        initCamera();
        //图片拍照的回调
        CameraInterface.getInstance().setTakePictureListener(takePictureListener);
    }
    //是否开启预览模式
    private boolean hasCanve = false;
    //第一次启动
    private boolean isFirst = false;

    //打开预览通过线程
    private void startOpenCamera() {
        Thread openThread = new Thread() {
            @Override
            public void run() {
                //打开预览
                CameraInterface.getInstance().doOpenCamera(camOpenOverCallback);
            }
        };
        openThread.start();
    }

    //当前的拍照模式，如果是直接存数据库，和添加二种形式
    private void initData() {
        if (typechannelList == null) {
            typechannelList = new ArrayList<>();
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra("fid")) {
                projectId = getIntent().getStringExtra("fid");
            }
            if (getIntent().hasExtra("typelist")) {
                typechannelList.addAll((ArrayList) getIntent().getParcelableArrayListExtra("typelist"));
            }
            if (getIntent().hasExtra("username")) {
                userName = getIntent().getStringExtra("username");
            }
        }
        mColumnHorizontalScrollView.setScrollViewChangeListener(scrollViewChangeListener);
        mScreenWidth = AppUtils.getScreenWidthPiex(mActivity);
        mItemWidth = mScreenWidth / 3;//单个的宽度
        initTabColumn();
    }


    @Override
    protected CameraPresenter createPresenter() {
        return new CameraPresenter(this);
    }

    //开启预览
    private CameraInterface.CamOpenOverCallback camOpenOverCallback = new CameraInterface.CamOpenOverCallback() {
        @Override
        public void cameraHasOpened() {
            hasCanve = true;
            SurfaceHolder holder = autoSurfaceView.getSurfaceHolder();
            CameraInterface.getInstance().doStartPreview(holder, previewRate);
        }
    };

    //拍照后保存图片；
    private CameraInterface.TakePictureListener takePictureListener = new CameraInterface.TakePictureListener() {
        @Override
        public void takePicture(String picturePath) {
            //图片拍照成功
            LogUtil.log("takeSuccess_path", picturePath);
            //获取了照片然后将图片添加到数据库中。
            if (!TextUtils.isEmpty(picturePath)) {
                if (!currentCategory.equals("视频")) {
                    new SaveImage(picturePath).start();
                } else {
                    isRunning = false;
                }
            }
            skanImage.setTag(picturePath);
            int view_width = skanImage.getMeasuredWidth();
            int view_height = skanImage.getMeasuredHeight();
            imageLoader.loadImageBySize(view_width, view_height, picturePath, skanImage);
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!hasCanve && isFirst == false) {
            startOpenCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        hasCanve = false;
        isFirst = false;
        super.onStop();
    }

    private class SaveImage extends Thread {
        private String picturePath;

        public SaveImage(String picturePath) {
            this.picturePath = picturePath;
        }

        @Override
        public void run() {
            super.run();

            if (!TextUtils.isEmpty(picturePath)) {
                //创建数据然后添加到数据库中
                LocalFileEntity fileEntity = new LocalFileEntity();
                fileEntity.setCategoryId(Integer.valueOf(currentKey));
                fileEntity.setCategoryName(currentCategory);
                fileEntity.setFID(projectId);
                fileEntity.setLocalFilePath(picturePath);
                String name = picturePath.substring(picturePath.lastIndexOf("/"), picturePath.length());
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


    @OnTouch(R.id.auto_surfaceView)
    boolean onTouch(View v, MotionEvent event) {
        CameraInterface.getInstance().setmCameraAuto();
        return false;
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

    private boolean isRunning = false;

    @OnClick(R.id.take_picture_image)
    void startTakePicture(View view) {
        if (!isRunning) {
            if (currentCategory.equals("视频")) {
                new HiFoToast(mContext, "视频附件不能操作拍照");
                return;
            }
            CameraInterface.getInstance().doTakePicture();
            isRunning = true;
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

    @OnClick(R.id.cancel_takepicture)
    void backActivity(View view) {
        CameraInterface.getInstance().doStopCamera();
        saveData();
    }


    private void saveData() {
        if (dataHasChange) {
            setResult(0x0003);
        }
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveData();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

package com.android.hifosystem.hifoevaluatevalue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;
import com.android.hifosystem.hifoevaluatevalue.camera_view.ViewPagerFixed;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.FileEntity;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.GalleryModel;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.GalleryOperateView;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.GalleryPageAdapter;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.GalleryPresenter;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.PhotoInfoEntity;
import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.MvpActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类名： GallaryBigMapActivity
 * 工鞥： 预览大图片
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class GallaryBigMapActivity extends MvpActivity<GalleryPresenter> implements GalleryOperateView {
    //viewPager
    @BindView(R.id.add_picture_viewpager)
    ViewPagerFixed addPictureViewpager;
    //返回
    @BindView(R.id.gallery_left_image)
    ImageView galleryLeftImage;
    //标题
    @BindView(R.id.gallery_title_text)
    TextView galleryTitleText;
    //数量
    @BindView(R.id.gallery_right_text)
    TextView galleryRightText;
    //标题
    @BindView(R.id.gallery_title)
    RelativeLayout galleryTitle;
    private GalleryPageAdapter adapter;
    private int position = 0;
    private int number;
    private String flag = "";
    public ArrayList<PhotoInfoEntity> local_list = new ArrayList<>();
    public ArrayList<FileEntity> network_list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity_view);// 切屏到主界面
        ButterKnife.bind(this);
        getData();
        initData();
        addPictureViewpager.addOnPageChangeListener(pageChangeListener);
        adapter.setOnImageClickListener(onImageClickListener);
    }


    private GalleryPageAdapter.OnImageClickListener onImageClickListener = new GalleryPageAdapter.OnImageClickListener() {
        @Override
        public void imageClick(int position) {
            AppUtils.showView(galleryTitle);
        }
    };

    private void initData() {
        if(flag.equals("network")){
            adapter = new GalleryPageAdapter(mContext, mActivity,network_list,flag);
        }else{
            adapter = new GalleryPageAdapter(mContext, mActivity,local_list,flag);
        }
        AppUtils.showView(galleryTitle);
        addPictureViewpager.setAdapter(adapter);
        addPictureViewpager.setPageMargin((int) getResources().getDimensionPixelOffset(R.dimen.margin_5_dp));
        addPictureViewpager.setCurrentItem(position);
        number = addPictureViewpager.getCurrentItem();
        int location = number + 1;
        if (flag.equals("network")) {
            if (network_list.size() > 0) {
                galleryRightText.setText("(" + location + "/" + network_list.size() + ")");
            }
        } else {
            if (local_list.size() > 0) {
                galleryRightText.setText("(" + location + "/" + local_list.size() + ")");
            }
        }
        if (galleryTitleText != null) {
            galleryTitleText.setText("照片预览");
        }

    }


    //图片预览目前只加了三种来源，一种是预览，第二种是编辑功能，第三种是通过详情来显示大图片
    //第一种是在图片选择页面ImageSelectActivity,
    //第二种是在TaskSendActivity页面
    //第三种是在TaseDetailActivity();
    private void getData() {
        if (getIntent() != null) {
            //当为预览模式的时候。
            if (getIntent().hasExtra("local_path")) {
                flag = getIntent().getStringExtra("local_path");
                if (local_list != null) {
                    local_list.clear();
                }
                if (getIntent().getExtras() != null) {
                    local_list = getIntent().getParcelableArrayListExtra("imagelist");
                }
            } else if (getIntent().hasExtra("network")) {
                flag = getIntent().getStringExtra("network");
                if (network_list != null) {
                    network_list.clear();
                }
                if (getIntent().getExtras() != null) {
                    network_list =  getIntent().getParcelableArrayListExtra("imagelist");
                }
            }
            position = getIntent().getExtras().getInt("position");
        }
    }


    private void backToData() {
        ActivityStackManager.getInstance().exitActivity(mActivity);
    }

    // 返回
    @OnClick(R.id.gallery_left_image)
     void onClickBack(View v) {
        backToData();
    }


    @OnClick(R.id.add_picture_viewpager)
     void viewPageClick(View view) {
        AppUtils.showView(galleryTitle);
    }

    /**
     * 滚动监听事件
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        public void onPageSelected(int arg0) {
            AppUtils.notShowView(galleryTitle);
            number = arg0;
            int position = number + 1;
            if (flag.equals("network")) {
                galleryRightText.setText("(" + position + "/" + network_list.size() + ")");
            } else {
                galleryRightText.setText("(" + position + "/" + local_list.size() + ")");
            }
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };


    /**
     * 监听返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backToData();
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void getDataSuccess(GalleryModel galleryModel) {

    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    protected GalleryPresenter createPresenter() {
        return new GalleryPresenter(this);
    }
}

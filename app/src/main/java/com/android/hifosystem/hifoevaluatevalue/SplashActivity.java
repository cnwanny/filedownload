package com.android.hifosystem.hifoevaluatevalue;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.GalleryModel;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.GalleryOperateView;
import com.android.hifosystem.hifoevaluatevalue.camera_view.gallery_mvp.GalleryPresenter;
import com.android.hifosystem.hifoevaluatevalue.framework_care.ActivityStackManager;
import com.android.hifosystem.hifoevaluatevalue.framework_care.BaseActivity;
import com.android.hifosystem.hifoevaluatevalue.framework_mvcbasic.MvpActivity;

/**
 * 类名： ${type_name}
 * 工鞥：
 * 作者： wanny
 * 时间：${date} ${time}
 */

public class SplashActivity extends MvpActivity<GalleryPresenter> implements GalleryOperateView {


    private Animation animation;
    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity_view);
        relativeLayout = (RelativeLayout) findViewById(R.id.splash_relative);
        startAniams();
    }

    private void startAniams() {
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_alph_anim);
        if (animation != null) {
            relativeLayout.startAnimation(animation);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, GongPingOldActivity.class);
                        startActivity(intent);
                        ActivityStackManager.getInstance().exitActivity(mActivity);
                    }
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void getDataSuccess(GalleryModel galleryModel) {

    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    protected GalleryPresenter createPresenter() {
        return null;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.){
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}

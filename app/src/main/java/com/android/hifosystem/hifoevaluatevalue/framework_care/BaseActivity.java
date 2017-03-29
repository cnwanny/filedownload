package com.android.hifosystem.hifoevaluatevalue.framework_care;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 文件名： BaseActivity
 * 功能：
 * 作者： wanny
 * 时间： 17:00 2016/8/5
 */
public class BaseActivity extends AppCompatActivity {

    public Activity mActivity;
    public Context mContext;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.setContentView(layoutResID);
        LogUtil.log(this.getClass().getName()+"=setContentView(int layoutResID)");
//        ButterKnife.bind(this);
        mActivity = this;
        mContext =  getBaseContext();
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        LogUtil.log(this.getClass().getName()+"=setContentView(View view)");
//        ButterKnife.bind(this);
        mActivity = this;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
//        ButterKnife.bind(this);
        mActivity = this;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.log(this.getClass().getName()+"= onCreate()");
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        onUnsubscribe();
        LogUtil.log(this.getClass().getName()+"= onDestroy()");
        super.onDestroy();
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();//取消注册，以避免内存泄露
        }
    }

    @Override
    protected void onStop() {
        LogUtil.log(this.getClass().getName()+"= onStop()");
        super.onStop();
    }

    @Override
    protected void onPause() {
        LogUtil.log(this.getClass().getName()+"= onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        LogUtil.log(this.getClass().getName()+"= onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        LogUtil.log(this.getClass().getName()+"=onStart()");
        super.onStart();
    }

    @Override
    protected void onResumeFragments() {
        LogUtil.log(this.getClass().getName()+"= onResumeFragments()");
        super.onResumeFragments();
    }

    @Override
    protected void onRestart() {
        LogUtil.log(this.getClass().getName()+"= onRestart()");
        super.onRestart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //添加数据的监听操作
    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }

//    public Toolbar initToolBar(String title) {
//        //初始化toolbar
//        Toolbar toolbar = initToolBar();
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.ui_toolbar_title);
//        toolbar_title.setText(title);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//
//        }
//        return toolbar;
//    }

//    public Toolbar initToolBar(int title) {
//        Toolbar toolbar = initToolBar();
//        //初始化toolbar
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.ui_toolbar_title);
//        toolbar_title.setText(title);
//        return toolbar;
//    }
//
//    public Toolbar initToolBar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//        return toolbar;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();//返回
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}

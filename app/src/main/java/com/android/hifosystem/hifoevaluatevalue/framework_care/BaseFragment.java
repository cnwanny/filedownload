package com.android.hifosystem.hifoevaluatevalue.framework_care;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 文件名： BaseFragment
 * 功能：
 * 作者： wanny
 * 时间： 17:05 2016/8/5
 */
public class BaseFragment extends Fragment {

    public Activity mActivity;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        LogUtil.log(this.getClass().getName()+"=onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mActivity = getActivity();
    }

//    public Toolbar initToolBar(View view, String title) {
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.ui_toolbar_title);
//        toolbar_title.setText(title);
//        return toolbar;
//    }


    public void toastShow(int resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }

    public void toastShow(String resId) {
        Toast.makeText(mActivity, resId, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAttach(Context context) {
        LogUtil.log(this.getClass().getName()+"=onAttach()");
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtil.log(this.getClass().getName()+"=onCreate()");
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.log(this.getClass().getName()+"=onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.log(this.getClass().getName()+"=onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        LogUtil.log(this.getClass().getName()+"=onResume()");
        super.onResume();
    }

    @Override
    public void onStart() {
        LogUtil.log(this.getClass().getName()+"=onStart()");
        super.onStart();
    }




    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        LogUtil.log(this.getClass().getName()+"=onViewStateRestored()");

        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.log(this.getClass().getName()+"=onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onPause() {
        LogUtil.log(this.getClass().getName()+"=onPause()");
        super.onPause();
    }
    @Override
    public void onStop() {
        LogUtil.log(this.getClass().getName()+"=onStop()");
        super.onStop();
    }


    @Override
    public void onDestroyView() {
        LogUtil.log(this.getClass().getName()+"=onDestroyView()");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.log(this.getClass().getName()+"=onDestroy()");
        super.onDestroy();
        onUnsubscribe();
    }

    private CompositeSubscription mCompositeSubscription;

    public void onUnsubscribe() {
        //取消注册，以避免内存泄露
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
        mCompositeSubscription = new CompositeSubscription();
//        }
        mCompositeSubscription.add(subscription);
    }
}

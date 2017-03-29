package com.android.hifosystem.hifoevaluatevalue.sharepackage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

/**
 * 文件名： share_dialog_view
 * 功能：
 * 作者： wanny
 * 时间： 11:26 2016/12/14
 */
public class ShareDialog extends Dialog {


    private GridView shareGridview;

    private TextView shareDialogViewCancel;

    //数据结构
    private ArrayList<ShareEntity> data;
    //启动的activity
    private Activity activity;
    //context
    private Context context;
    private ShareDialogAdapter adapter;

    private ShareImp shareImp;

    public ShareDialog(Context context) {
        super(context);
    }

    public ShareDialog(Context context, int theme, ArrayList<ShareEntity> data, Activity activity) {
        super(context, theme);
        this.context = context;
        this.data = data;
        this.activity = activity;

    }

    protected ShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public ShareDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_dialog_view);
        setWindows();
        setData();
    }




    private void setData() {
        shareGridview = (GridView) findViewById(R.id.share_gridview);
        shareDialogViewCancel = (TextView) findViewById(R.id.share_dialog_view_cancel);
        shareGridview.setOnItemClickListener(onItemClickListener);
        shareDialogViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shareImp != null){
                    shareImp.cancel();
                }
            }
        });
        adapter = new ShareDialogAdapter(data, context);
        shareGridview.setAdapter(adapter);
    }

   //设置windows的属性
    private void setWindows(){
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        this.getWindow().setGravity(Gravity.BOTTOM);
        this.getWindow().setAttributes(params);
        this.getWindow().setWindowAnimations(R.style.AnimBottom);

    }
    //设置回调
    public void setShareImp(ShareImp shareImp) {
        this.shareImp = shareImp;
    }
    //点击监听
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
         if(position == 0){
             if(shareImp != null){
                 shareImp.shareWeixinFriends();
             }
         }else if(position == 1){
             if(shareImp != null){
                 shareImp.shareWeixin();
             }
         }else if(position == 2){
             if(shareImp != null){
                 shareImp.shareQQ();
             }
         }else if(position == 3){
             if(shareImp != null){
                 shareImp.shareQQFriendsSpace();
             }
         }else if(position == 4){
             if(shareImp != null){
                 shareImp.copyURL();
             }
         }else if(position == 5){

         }
        }
    };
}

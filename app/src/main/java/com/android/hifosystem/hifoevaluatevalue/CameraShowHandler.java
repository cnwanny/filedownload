package com.android.hifosystem.hifoevaluatevalue;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;

import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;
import com.bumptech.glide.Glide;

import java.io.File;

/**
 * 文件名： CameraShowHandler
 * 功能：  采用Handler来更新页面显示
 * 作者： wanny
 * 时间： 14:06 2016/9/7
 */
public class CameraShowHandler extends Handler {

    private ImageView imageView;
    private Activity activity;

    public CameraShowHandler(ImageView imageView, Activity activity) {
        this.imageView = imageView;
        this.activity = activity;
    }

    public CameraShowHandler(Looper looper, ImageView imageView, Activity activity) {
        super(looper);
        this.imageView = imageView;
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        LogUtil.log("开始显示拍到的图片");
        if (msg.what == 0x0001) {
            if (msg.obj != null && msg.obj instanceof String) {
                String picturePath = (String) msg.obj;
                if(!TextUtils.isEmpty(picturePath)){
                    Glide.with(activity).load(Uri.fromFile(new File(picturePath))).dontAnimate().into(imageView);
                }
//                imageView.setTag(picturePath);
//                int view_width = skanImage.getMeasuredWidth();
//                int view_height = skanImage.getMeasuredHeight();
//                imageLoader.loadImageBySize(view_width, view_height, picturePath, skanImage);
            }
        }
    }
}

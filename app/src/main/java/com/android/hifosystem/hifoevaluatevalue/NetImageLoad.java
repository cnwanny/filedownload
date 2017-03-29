package com.android.hifosystem.hifoevaluatevalue;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * 文件名： NetImageLoad
 * 功能：
 * 作者： wanny
 * 时间： 14:33 2016/11/28
 */
public class NetImageLoad implements GuildImp<String> {

    @Override
    public void setData(ArrayList<View> views, ArrayList<String> source) {
        if ((views != null && views.size() > 0) && (source != null && source.size() > 0)) {
            for (int i = 0; i < views.size(); i++) {
                View view = views.get(i);
                if (view instanceof ImageView) {
                    if (!TextUtils.isEmpty(source.get(i))) {
                        Glide.with(view.getContext()).load(source.get(i)).dontTransform().into((ImageView) view);
                    }
                }
            }
        }
    }
}

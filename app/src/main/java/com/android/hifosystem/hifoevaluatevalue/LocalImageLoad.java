package com.android.hifosystem.hifoevaluatevalue;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * 文件名： LocalImageLoad
 * 功能：  加载本地文件类
 * 作者： wanny
 * 时间： 14:23 2016/11/28
 */
public class LocalImageLoad implements GuildImp<Integer> {


    @Override
    public void setData(ArrayList<View> views, ArrayList<Integer> source) {
        if((views != null && views.size() > 0) && (source != null && source.size() > 0)){
             for(int i = 0 ; i < views.size() ; i ++){
                 View view = views.get(i);
                 if(view instanceof ImageView){
                     ((ImageView) view).setImageResource(source.get(i));
                 }
             }
        }
    }
}

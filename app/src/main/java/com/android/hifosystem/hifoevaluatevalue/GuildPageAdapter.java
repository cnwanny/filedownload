package com.android.hifosystem.hifoevaluatevalue;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 文件名： GuildPageAdapter
 * 功能：
 * 作者： wanny
 * 时间： 13:53 2016/11/28
 */
public class GuildPageAdapter extends PagerAdapter {



    private ArrayList<View> views ;
    public GuildPageAdapter(ArrayList<View> views) {
        this.views = views;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }



}

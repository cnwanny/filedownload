package com.android.hifosystem.hifoevaluatevalue;

import android.view.View;

import java.util.ArrayList;

/**
 * 文件名： GuildImp
 * 功能：
 * 作者： wanny
 * 时间： 14:21 2016/11/28
 */
public interface GuildImp<T> {
    void setData(ArrayList<View> views, ArrayList<T> source);
}

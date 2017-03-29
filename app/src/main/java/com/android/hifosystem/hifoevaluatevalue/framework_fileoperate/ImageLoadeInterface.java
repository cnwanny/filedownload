package com.android.hifosystem.hifoevaluatevalue.framework_fileoperate;

import android.graphics.Bitmap;

/**
 * 类名： HiFoSurveyApp
 * 功能：
 * 作者： wanny
 * 时间： 14:17 2016/5/26
 */
public interface ImageLoadeInterface {
    void put(String url, Bitmap bitmap);
    Bitmap get(String url);
}

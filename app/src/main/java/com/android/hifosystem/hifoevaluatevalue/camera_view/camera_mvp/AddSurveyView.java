package com.android.hifosystem.hifoevaluatevalue.camera_view.camera_mvp;


import com.android.hifosystem.hifoevaluatevalue.framework_care.BaseOperateView;

/**
 * 文件名： AddSurveyView
 * 功能：
 * 作者： wanny
 * 时间： 9:49 2016/11/23
 */
public interface AddSurveyView extends BaseOperateView<SurveyFileModel> {
    void showLoading(String title);
    void hideLoading();
}

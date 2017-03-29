package com.android.hifosystem.hifoevaluatevalue.camera_view.camera;

import android.util.Size;

/**
 * 文件名： CameraVedioCategory
 * 功能：
 * 作者： wanny
 * 时间： 11:40 2016/7/8
 */
public interface CameraVedioCategory {


    /**
     * 打开拍照也
     * @param width
     * @param height
     */
    void openCamera(int width, int height);

    /**
     * 横竖屏发生装换的话
     * @param width
     * @param height
     */
    void configChange(int width, int height);

    /**
     * 停止录制
     */
    void stopCamera();

    /**
     * 开始录制
     */
    void startVedio();

    /**
     * 获取最合适的Size
     * @param choices
     * @return
     */
    Size chooseVideoSize(Size[] choices);

    /**
     * 开始预览
     */
    void startPreview();

    /**
     * 更新预览
     */
    void upDatePreview();

    /**
     * 获取状态
     */
    boolean getStateVido();

    void closeCamera();


}

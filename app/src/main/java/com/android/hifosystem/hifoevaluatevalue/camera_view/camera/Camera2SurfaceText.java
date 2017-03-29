package com.android.hifosystem.hifoevaluatevalue.camera_view.camera;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

/**
 * 文件名： Camera2SurfaceText
 * 功能：
 * 作者： wanny
 * 时间： 13:51 2016/7/6
 */
public class Camera2SurfaceText  extends TextureView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
//    private MyTextureViewTouchEvent mMyTextureViewTouchEvent;
//    private FocusPositionTouchEvent mFocusPositionTouchEvent;

    public Camera2SurfaceText(Context context) {
        super(context);
    }

    public Camera2SurfaceText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Camera2SurfaceText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }

    public void setWindowsSize(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }



}

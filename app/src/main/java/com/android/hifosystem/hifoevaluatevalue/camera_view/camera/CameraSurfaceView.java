package com.android.hifosystem.hifoevaluatevalue.camera_view.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.hifosystem.hifoevaluatevalue.Utils.LogUtil;


/**
 * 类名： CameraSurfaceView
 * 工鞥： 自定义的相机类
 * 作者： wanny
 * 时间： 20160218
 */

public class CameraSurfaceView  extends SurfaceView implements SurfaceHolder.Callback {
        private static final String TAG = "wanny";
        Context mContext;
        SurfaceHolder mSurfaceHolder;
        public CameraSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
            // TODO Auto-generated constructor stub
            mContext = context;
            mSurfaceHolder = getHolder();
            mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);//translucent半透明 transparent透明
//            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mSurfaceHolder.addCallback(this);

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            LogUtil.log("surfaceCreated...");
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub
            LogUtil.log(TAG, "surfaceChanged...");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            LogUtil.log(TAG, "surfaceDestroyed...");
            CameraInterface.getInstance().doStopCamera();
        }
        public SurfaceHolder getSurfaceHolder(){
            return mSurfaceHolder;
        }

    }


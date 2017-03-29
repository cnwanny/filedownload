package com.android.hifosystem.hifoevaluatevalue.camera_view;

import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.View;

/**
 * 类名： Compat
 * 工鞥： 动画的适配版本
 * 作者： wanny
 * 时间：20160216
 */

public class Compat {

    private static final int SIXTY_FPS_INTERVAL = 1000 / 60;

    public static void postOnAnimation(View view, Runnable runnable) {
        if (VERSION.SDK_INT >= VERSION_CODES.BASE) {
            view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
        } else {
            view.postDelayed(runnable, SIXTY_FPS_INTERVAL);
        }
    }

}

package com.android.hifosystem.hifoevaluatevalue.framework_care;

import android.app.Activity;

import java.util.Stack;

/**
 * 文件名： ActivityManager
 * 功能：  管理Activity的栈
 * 作者： wanny
 * 时间： 9:51 2016/7/15
 */
public class ActivityStackManager {
    private static Stack<Activity> activityStack;
    private static ActivityStackManager instance;
    //通过单例的形式直接调用�?
    public ActivityStackManager() {

    }
    //单例
//	public static ActivityManagers getScreenManager() {
//		if (instance == null) {
//			instance = new ActivityManagers();
//		}
//		return instance;
//	}

    private static class ActivityHandler {
        private static final ActivityStackManager INSTANCE = new ActivityStackManager();
    }

    public static final ActivityStackManager getInstance() {
        return ActivityHandler.INSTANCE;
    }


    // �?��栈顶Activity
    public void exitActivity(Activity activity) {
        if (activity != null) {
            // 在从自定义集合中取出当前Activity时，也进行了Activity的关闭操�?
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    // 获得当前栈顶Activity
    public Activity currentActivity() {
        Activity activity = null;
        if(activityStack != null){
            if (!activityStack.empty())
                activity = activityStack.lastElement();
        }
        return activity;
    }

    // 将当前Activity推入栈中
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    //退出栈中所有存在的Activity
    public void exitAllActivityExceptOne( ) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            exitActivity(activity);
        }
    }


}

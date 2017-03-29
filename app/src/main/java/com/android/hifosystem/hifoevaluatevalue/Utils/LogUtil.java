package com.android.hifosystem.hifoevaluatevalue.Utils;

import android.util.Log;

/**
 * 类名：  @LogUtil.java
 * 作者：  wanny
 * 功能： 封装了日志的输出操作！
 * 时间：  2014年8月7日 下午4:14:12
 */
public abstract class LogUtil {

	public static final boolean DEBUG = false;
	public static void log( String msg) {
		if (DEBUG) {
			Log.i("wanny","====" + msg);
		}
	}
	public static void log(String log, String msg) {
		if (DEBUG) {
			Log.i("wanny", log + "=====" + msg);
		}
	}
	public static void log(String TAG , String msg , String data){
		if(DEBUG){
			Log.i("wanny", TAG  + "=====" + msg + "............."+data);
		}
	}
}

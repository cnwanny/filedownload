package com.android.hifosystem.hifoevaluatevalue.framework_net.netwebservice;

import android.app.Activity;
import android.os.AsyncTask;

import com.android.hifosystem.hifoevaluatevalue.AutoView.WaitDialog;
import com.android.hifosystem.hifoevaluatevalue.R;

import java.util.List;
import java.util.Map;

public abstract class TaskSendNewWork  extends AsyncTask<Object, Void, Object> {
		private String methodName;
		List<Map<String, Object>> par;
		private WaitDialog dialog;
		//dialog。数据
		private String dialogDisData;
		//点击取消
		private boolean cancelable = true;
	    private Activity activity ;
	    private String endPoint;
		/**
		 * 作者 ： wanny
		 * 时间 ：2015年7月2日下午3:44:29
		 */

        //"1.修改了界面流畅，修改不能修改密码的bug。/n  2.新增修改任务信息的功能，包括在发布任务编辑发布任务的对象信息。"

		public TaskSendNewWork(String endPoint , String methodName, List<Map<String, Object>> par,
							   boolean isCache) {
			this.methodName = methodName;
			this.par = par;
			this.endPoint = endPoint;
		}

		public TaskSendNewWork(String endPoint , Activity activity, String dialogDisData,
							   String methodName, List<Map<String, Object>> par) {
			this.activity = activity;
			this.methodName = methodName;
			this.dialogDisData = dialogDisData;
			this.par = par;
			this.endPoint = endPoint;
		}
		public TaskSendNewWork(String endPoint , Activity activity, String dialogDisData,
							   String methodName, List<Map<String, Object>> par, boolean cancelable) {
			this.activity = activity;
			this.methodName = methodName;
			this.dialogDisData = dialogDisData;
			this.par = par;
			this.cancelable = cancelable;
			this.endPoint = endPoint;
		}


		@Override
		protected void onPreExecute() {
			if (activity != null) {
				dialog = new WaitDialog(activity, R.style.dialog,dialogDisData);
				dialog.show();
			}
		}

		@Override
		protected String[] doInBackground(Object... arg0) {
			try {
				String data = TaskSendWebService.getWebServiceData(endPoint,methodName, par);
				netDataResult(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			try {
				if (dialog != null)
					dialog.dismiss();
				executeResult(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public abstract void netDataResult(String data);

		public abstract void executeResult(Object data);

	}


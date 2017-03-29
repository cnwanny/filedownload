package com.android.hifosystem.hifoevaluatevalue.AutoView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.android.hifosystem.hifoevaluatevalue.R;
import com.android.hifosystem.hifoevaluatevalue.Utils.AppUtils;

public class MyDialog extends Dialog {

	public TextView dialog_content;
	public TextView dialog_sure;
	public TextView dialog_cancel;
	private Context context;
	private String content;
	private EditText editcontent;
	private String flag;
    private Activity activity;
	private EditText editPrice_callback;

	public MyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public MyDialog(Context context, int theme, String content, String flag, Activity activity) {
		super(context, theme);
		this.content = content;
		this.context = context;
		this.flag = flag;
        this.activity = activity;
	}

	public MyDialog(Context context, String content, String flag, Activity activity) {
		super(context);
		this.content = content;
		this.context = context;
		this.flag = flag;
        this.activity = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.mydialog_view, null);
		setContentView(view);
		dialog_content = (TextView) view.findViewById(R.id.mydialog_view_content_tv);
		dialog_sure = (TextView) view.findViewById(R.id.mydialog_view_sure);
		dialog_cancel = (TextView) view.findViewById(R.id.mydialog_view_cancel);
		editcontent = (EditText)view.findViewById(R.id.mydialog_view_content_edit);
		editPrice_callback = (EditText) view.findViewById(R.id.mydialog_view_pricecallback_edit);
		if (dialog_content != null) {
			dialog_content.setText(content);
		}
		if(flag.equals("edit")){
			AppUtils.showView(editcontent);
		}else if(flag.equals("notedit")){
			AppUtils.notShowView(editcontent);
		}else if(flag.equals("price")){
			AppUtils.showView(editcontent);
			AppUtils.showView(editPrice_callback);
		}
		dialog_sure.setOnClickListener(new clickListener());
		dialog_cancel.setOnClickListener(new clickListener());
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width =(int)(AppUtils.getScreenWidthPiex(activity) * 0.7);
        this.getWindow().setAttributes(params);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		editcontent.addTextChangedListener(mActch);
		editPrice_callback.addTextChangedListener(pricewatch);
	}
      String value = "";
	  String price = "";
	  private TextWatcher pricewatch = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			price = s.toString();
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	 private TextWatcher mActch = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			value = s.toString();
		}

		@Override
		public void afterTextChanged(Editable s) {

		}
	};

	private ClickListenerInterface listener;

	public void setClickListener(ClickListenerInterface listener) {
		this.listener = listener;
	}

	public interface ClickListenerInterface {
		 void cancel();

		 void sure(String editdata, String pricecallback);
	}

	private class clickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.mydialog_view_sure:
				listener.sure(value,price);
				break;
			case R.id.mydialog_view_cancel:
				listener.cancel();
				break;
			}
		}
	}
}

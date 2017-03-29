package com.android.hifosystem.hifoevaluatevalue.AutoView;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.R;


public class WaitDialog extends Dialog {
	private TextView tv;
	private String title;
	public WaitDialog(Context context) {
		super(context);
	}
	public WaitDialog(Context context, int theme , String title) {
		super(context, theme);
		this.title = title;
	}
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.waitdialog);
			tv = (TextView)this.findViewById(R.id.tv);
			tv.setText(title);
			LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
			linearLayout.getBackground().setAlpha(210);
		}
	}



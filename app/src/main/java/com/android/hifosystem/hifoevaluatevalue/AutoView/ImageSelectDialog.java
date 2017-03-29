package com.android.hifosystem.hifoevaluatevalue.AutoView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.android.hifosystem.hifoevaluatevalue.R;


public class ImageSelectDialog extends Dialog {
	public TextView image_album;
	public TextView image_take;
	public TextView image_cancel;
	private Context context;
    private String title1;
    private String title2;
	public ImageSelectDialog(Context context, String title1, String title2) {
		super(context);
		this.title1 = title1;
		this.title2 = title2;
	}

	public ImageSelectDialog(Context context, int theme, String title1, String title2) {
		super(context, theme);
		this.context = context;
		this.title1 = title1;
		this.title2 = title2;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.imageselectdialog, null);
		setContentView(view);
		image_album = (TextView) view.findViewById(R.id.imageselect_album_tv);
		image_take = (TextView) view.findViewById(R.id.imageselect_take_tv);
		image_cancel = (TextView) view.findViewById(R.id.imageselect_cancel_tv);
		if(title1 != null){
			image_take.setText(title1);
		}
		if(title2 != null){
			image_album.setText(title2);
		}
		image_album.setOnClickListener(new clickListener());
		image_take.setOnClickListener(new clickListener());
		image_cancel.setOnClickListener(new clickListener());

	}

	private ClickListenerInterface listener;

	public void setClickListener(ClickListenerInterface listener) {
		this.listener = listener;
	}

	public interface ClickListenerInterface {
		 void dotakePicture();
		 void doalbum();
		 void docancel();
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.imageselect_album_tv:
				listener.doalbum();
				break;
			case R.id.imageselect_take_tv:
				listener.dotakePicture();
				break;
			case R.id.imageselect_cancel_tv:
				listener.docancel();
				break;
			}
		}
	};
}

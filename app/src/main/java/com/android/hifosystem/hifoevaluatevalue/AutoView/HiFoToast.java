package com.android.hifosystem.hifoevaluatevalue.AutoView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.hifosystem.hifoevaluatevalue.R;


public class HiFoToast extends Toast {
    private Context context ;
    private String content;
	public HiFoToast(Context context, String content) {
		super(context);
		this.context = context;
		this.content = content;
		intitView();
	}
	
	private void intitView(){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view_view = inflater.inflate(R.layout.hifo_toast_view, null);
		ImageView image_toast = (ImageView)view_view.findViewById(R.id.image_toast_hifo);
		TextView text_toast = (TextView)view_view.findViewById(R.id.text_toast_hifo);
		text_toast.setText(content);
		Animation leftanim = AnimationUtils.loadAnimation(context,R.anim.left_rotate);
		image_toast.setAnimation(leftanim);
		leftanim.start();
		this.setView(view_view);
		this.setDuration(Toast.LENGTH_SHORT);
		this.show();
	}
	
	@Override
	public void show() {
		super.show();
	}

	@Override
	public void setView(View view) {
		super.setView(view);
	}

	@Override
	public void setMargin(float horizontalMargin, float verticalMargin) {
		super.setMargin(horizontalMargin, verticalMargin);
	}

	@Override
	public void setGravity(int gravity, int xOffset, int yOffset) {
		super.setGravity(gravity, xOffset, yOffset);
	}

	@Override
	public void setText(int resId) {
		super.setText(resId);
	}

	@Override
	public void setText(CharSequence s) {
		super.setText(s);
	}
   
}

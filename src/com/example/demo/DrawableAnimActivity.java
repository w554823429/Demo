package com.example.demo;

import com.example.demo.annotation.InjectView;
import com.example.demo.annotation.InjectViewParser;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
/**
 * drawable∂Øª≠
 * @author MX
 *
 */
public class DrawableAnimActivity extends Activity {
	@InjectView(id = R.id.iv_anim)
	private ImageView iv_anim;
	@InjectView(id = R.id.btn_stop)
	private Button btn_stop;
	@InjectView(id = R.id.btn_start)
	private Button btn_start;
	private AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawable_anim);
		InjectViewParser.inject(this);// ≥ı ºªØ
		btn_stop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (animationDrawable.isRunning()) {
					animationDrawable.stop();
				}
			}
		});
		btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!animationDrawable.isRunning()) {
					animationDrawable.start();
				}
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		animationDrawable = new AnimationDrawable();
		animationDrawable.addFrame(
				getResources().getDrawable(R.drawable.loading1), 80);
		animationDrawable.addFrame(
				getResources().getDrawable(R.drawable.loading2), 80);
		animationDrawable.addFrame(
				getResources().getDrawable(R.drawable.loading3), 80);
		animationDrawable.addFrame(
				getResources().getDrawable(R.drawable.loading4), 80);
		animationDrawable.addFrame(
				getResources().getDrawable(R.drawable.loading5), 80);
		animationDrawable.addFrame(
				getResources().getDrawable(R.drawable.loading6), 80);
		iv_anim.setBackgroundDrawable(animationDrawable);
		animationDrawable.setOneShot(false);
		animationDrawable.start();
	}
}

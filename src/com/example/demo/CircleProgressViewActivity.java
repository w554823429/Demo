package com.example.demo;

import android.app.Activity;
import android.os.Bundle;

public class CircleProgressViewActivity extends Activity{
		CircleProgressView mCircleBar;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_circleprogress);
			mCircleBar = (CircleProgressView) findViewById(R.id.circleProgressbar);
	        mCircleBar.setProgress(80);
		}

}

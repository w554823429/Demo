package com.example.demo;

import com.example.demo.annotation.InjectView;
import com.example.demo.annotation.InjectViewParser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class InjectActivity extends Activity {

	@InjectView(id = R.id.tv_inject)
	private TextView tv_inject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inject);
		InjectViewParser.inject(this);//初始化
		tv_inject.setText("注解成功");
	}

}

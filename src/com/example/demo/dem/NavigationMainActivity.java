package com.example.demo.dem;

import com.example.demo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NavigationMainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
	}
	public void navigation(View view){
		Intent mIntent=new Intent(NavigationMainActivity.this,NavigationResultActivity.class);
		mIntent.putExtra("latitude", 31.3366002);
		mIntent.putExtra("longitude", 121.3894558);
//		mIntent.putExtra("latitude", 31.20195129492995);
//		mIntent.putExtra("longitude",121.55779143957028);
		startActivity(mIntent);
	}
}

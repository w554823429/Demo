package com.example.demo;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.RequestQueue;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

public class MyApplication extends Application {
	public static MyApplication mApp;
	@Override
	public void onCreate() {
		super.onCreate();
		NoHttp.init(this);
		mApp=this;
	}
}

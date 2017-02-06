package com.example.demo.listener;

import android.content.Context;
import android.widget.Toast;

import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

public class HttpResponseListener<T> implements OnResponseListener<T>{
	private HttpListener<T> callback;
	private Context mContext;
	
	public HttpResponseListener(Context context,HttpListener<T> httpCallback,boolean canCancel,boolean isLoading){
		this.callback=httpCallback;
		this.mContext=context;
	}

	@Override
	public void onFailed(int what, String url, Object tag, Exception exception,
			int resCode, long ms) {
		if(callback!=null){
			callback.onFailed(what, url, tag, exception, resCode, ms);
		}
	}

	@Override
	public void onFinish(int arg0) {
		Toast.makeText(mContext, "onFinish", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStart(int arg0) {
		Toast.makeText(mContext, "onStart", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSucceed(int what, Response<T> response) {
		if(callback!=null){
			callback.OnSucceed(what, response);
		}
	}

}

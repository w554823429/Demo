package com.example.demo.listener;

import com.yolanda.nohttp.rest.Response;

public interface HttpListener<T> {
	void OnSucceed(int what,Response<T> response);
	void onFailed(int what,String url,Object tag,Exception exception,int resCode,long ms);
}

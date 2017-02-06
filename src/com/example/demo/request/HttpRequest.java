package com.example.demo.request;

import android.content.Context;

import com.example.demo.listener.HttpListener;
import com.example.demo.listener.HttpResponseListener;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;

public class HttpRequest {
	private static HttpRequest httpRequest;
	private RequestQueue requestQueue;
	private HttpRequest(){
		requestQueue=NoHttp.newRequestQueue();
	}
	public synchronized static HttpRequest getRequestInstance(){
		if(httpRequest==null){
			httpRequest=new HttpRequest();
		}
		return httpRequest;
	}
	/**
	 * 添加一个请求到请求队列
	 * @param context
	 * @param what
	 * @param request
	 * @param callback
	 * @param canCancel
	 * @param isLoading
	 */
	public <T> void add(Context context,int what,Request<T> request,HttpListener<T> callback,boolean canCancel,boolean isLoading){
		requestQueue.add(what, request, new HttpResponseListener<T>(context, callback, canCancel, isLoading));
	}
	/**
	 * 取消这个sign标记的所有请求
	 * @param sign
	 */
	public void cancelBySign(Object sign){
		requestQueue.cancelBySign(sign);
	}
	/**
	 * 取消队列中所有请求
	 */
	public void cancelAll() {
        requestQueue.cancelAll();
    }
	/**
	 * 退出APP时停止所有请求
	 */
	public void stopAll() {
        requestQueue.stop();
    }
}

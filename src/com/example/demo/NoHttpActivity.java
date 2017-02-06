package com.example.demo;

import org.json.JSONObject;

import com.example.demo.listener.HttpListener;
import com.example.demo.request.HttpRequest;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
/**
 * noHttp的使用
 * @author MX
 *
 */
public class NoHttpActivity extends Activity {
	RequestQueue queue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String url="http://publicwelfare.duapp.com/UserLoginServlet";
		Request<JSONObject> request=NoHttp.createJsonObjectRequest(url,RequestMethod.POST);
		request.add("userName", "gd");
		request.add("passWord", "123");
		request.setConnectTimeout(1000);
		HttpRequest.getRequestInstance().add(NoHttpActivity.this, 1, request, new HttpListener<JSONObject>() {
			@Override
			public void OnSucceed(int what, Response<JSONObject> response) {
				Toast.makeText(NoHttpActivity.this, response.get().toString(), Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFailed(int what, String url, Object tag,
					Exception exception, int resCode, long ms) {
				Toast.makeText(NoHttpActivity.this, "onFailed", Toast.LENGTH_SHORT).show();
			}
		}, false, false);
		/*queue = NoHttp.newRequestQueue();
		queue.add(1, request, new OnResponseListener<JSONObject>() {

			@Override
			public void onFailed(int arg0, String arg1, Object arg2,
					Exception arg3, int arg4, long arg5) {
				Toast.makeText(NoHttpActivity.this, "onFailed", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onFinish(int arg0) {
				Toast.makeText(NoHttpActivity.this, "onFinish", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onStart(int arg0) {
				Toast.makeText(NoHttpActivity.this, "onStart", Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onSucceed(int arg0, Response<JSONObject> response) {
				Toast.makeText(NoHttpActivity.this, "onSucceed", Toast.LENGTH_SHORT).show();
				 // 请求成功
                JSONObject result = response.get();// 响应结果
                // 响应头
                Headers headers = response.getHeaders();
                headers.getResponseCode();// 响应码
                response.getNetworkMillis();// 请求花费的时间
				Toast.makeText(NoHttpActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
			}

		});*/
	}
	@Override
    protected void onDestroy() {
        super.onDestroy();
//        queue.cancelAll();// 退出APP时停止所有请求
//        queue.stop();// 退出APP时停止队列
        HttpRequest.getRequestInstance().cancelAll();
        HttpRequest.getRequestInstance().stopAll();
    }
}

package com.example.demo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import com.example.demo.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class AsyncTaskUtil extends AsyncTask<String, Double, Boolean> {

	public static final String TAG = "AsyncTaskUtil";
	
	public static final int NOTIFICATION_PROGRESS_UPDATE = 0x10;//���ڸ������ؽ��ȵı�־
	public static final int NOTIFICATION_PROGRESS_SUCCEED = 0x11;//��ʾ���سɹ�
	public static final int NOTIFICATION_PROGRESS_FAILED = 0x12;//��ʾ����ʧ��

	//URL
	private String mUrl;
	//activity
	private Context mContext;
	//����ʱ��
	private Timer mTimer;
	//��ʱ����
	private TimerTask mTask;
	//���̴߳��ݹ�����handler
	private Handler  mHandler;
	//��Ҫ���ص��ļ���С
	private long mFileSize;
	//�����ص��ļ���С
	private long mTotalReadSize;
	//AsyncTaskRunnableʵ����Runnable�ӿڣ����ڸ������ؽ��ȵ���ʾ
	private AsyncTaskRunnable mRunnable;

	//���췽��
	public AsyncTaskUtil(Context context, Handler handler) {
		mContext = context;
		mHandler = handler;

		mTimer = new Timer();
		mTask = new TimerTask() {//��run������ִ�ж�ʱ������
			@Override
			public void run() {
				//size��ʾ���ؽ��ȵİٷֱ�
				float size = (float) mTotalReadSize * 100 / (float) mFileSize;
				//ͨ��AsyncTaskRunnable��setDatas�������صĽ��Ⱥ�״̬�������С�ʧ�ܡ��ɹ���
				mRunnable.setDatas(NOTIFICATION_PROGRESS_UPDATE, size);
				//���½���
				mHandler.post(mRunnable);
			}
		};
		mRunnable = new AsyncTaskRunnable(mContext);
	}

	
	// ִ�к�ʱ����,params[0]Ϊurl��params[1]Ϊ�ļ���������д��null��
	@Override
	protected Boolean doInBackground(String... params) {
		
		//����ʱ��һ��Ҫ����
		mTimer.schedule(mTask, 0, 500);
		
		try {
			mUrl = params[0];
			//��������
			URLConnection connection = new URL(mUrl).openConnection();
			//��ȡ�ļ���С
			mFileSize = connection.getContentLength();
			Log.d(TAG, "the count of the url content length is : " + mFileSize);

			//���������
			InputStream is = connection.getInputStream();
			//�Ƚ����ļ���
			File fold = new File(getFolderPath());
			if (!fold.exists()) {
				fold.mkdirs();
			}
			
			String fileName = "";
			//�ж��ļ������û��Զ������url���
//			if(params[1] != null){
//				fileName = params[1];
//			} else{
				fileName = getFileName(params[0]);
//			}
			//�ļ������
			FileOutputStream fos = new FileOutputStream(new File(getFolderPath()
							+ fileName));

			byte[] buff = new byte[1024];
			int len;
			while ((len = is.read(buff)) != -1) {
				mTotalReadSize += len;
				fos.write(buff, 0, len);
			}
			fos.flush();
			fos.close();

		} catch (Exception e) {
			//�쳣,����ʧ��
			mRunnable.setDatas(NOTIFICATION_PROGRESS_FAILED, 0);
			//������ʾ����ʧ��
			mHandler.post(mRunnable);
			if(mTimer != null && mTask != null){
				mTimer.cancel();
				mTask.cancel();
			}
			e.printStackTrace();
			return false;
		}
		//���سɹ�
		mRunnable.setDatas(NOTIFICATION_PROGRESS_SUCCEED, 0);
		mHandler.post(mRunnable);
		if(mTimer != null && mTask != null){
			mTimer.cancel();
			mTask.cancel();
		}
		return true;
	}

	//��url����ļ���
	private String getFileName(String string) {
		return string.substring(string.lastIndexOf("/") + 1);
	}

	//�����ļ���·��
	private String getFolderPath() {
		return Environment.getExternalStorageDirectory().toString() + "/AsyncTaskDownload/";
	}


	// doInBackground����֮ǰ���ã���ʼ��UI
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	// ��doInBackground����֮�����
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result) {
			Toast.makeText(mContext, "Download Completed ! ", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "Download Failed ! ", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onProgressUpdate(Double... values) {
		super.onProgressUpdate(values);
	}


}

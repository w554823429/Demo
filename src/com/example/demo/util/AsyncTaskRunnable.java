package com.example.demo.util;

import java.text.DecimalFormat;

import com.example.demo.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class AsyncTaskRunnable implements Runnable{

	public static final String TAG = "AsyncTaskRunnable";
	//���̵߳�activity
	private Context mContext;
	//notification��״̬������ or ʧ�� or �ɹ�
	private int mStatus;
	//notification�����ر���
	private float mSize;
	//���������˵���֪ͨ��Ϣ
	private NotificationManager mNotificationManager;
	//�����˵���֪ͨ��Ϣ
	private Notification mNotification;
	//�����˵���֪ͨ��Ϣ��view
	private RemoteViews mRemoteViews;
    //�����˵���֪ͨ��Ϣ������id
	private static final int NOTIFICATION_ID = 10000;
	
	//���ñ���������
	public void setDatas(int status , float size) {
		this.mStatus = status;
		this.mSize = size;
	}
	//��ʼ��
	public AsyncTaskRunnable(Context context) {
		this.mContext = context;
		mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		//��ʼ�������˵���֪ͨ��Ϣ
		mNotification = new Notification();
		mNotification.icon = R.drawable.ic_launcher;//�������ؽ��ȵ�icon
		mNotification.tickerText = mContext.getResources().getString(R.string.app_name); //�������ؽ��ȵ�title
		mNotification.flags|= Notification.FLAG_NO_CLEAR;
		mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.down_notification);//����RemoteView��ʹ�ã���������Ҫ����google
		mRemoteViews.setImageViewResource(R.id.id_download_icon, R.drawable.ic_launcher);
	}
	
	@Override
	public void run() {//ͨ���жϲ�ͬ��״̬��������/����ʧ��/���سɹ� ���������˵���֪ͨ��Ϣ
		switch (mStatus) {
		case AsyncTaskUtil.NOTIFICATION_PROGRESS_FAILED://����ʧ��
			mNotificationManager.cancel(NOTIFICATION_ID);
			break;

		case AsyncTaskUtil.NOTIFICATION_PROGRESS_SUCCEED://���سɹ�
			mRemoteViews.setTextViewText(R.id.id_download_textview, "Download completed ! ");
			mRemoteViews.setProgressBar(R.id.id_download_progressbar, 100, 100, false);
			mNotification.contentView = mRemoteViews;
			mNotificationManager.notify(NOTIFICATION_ID, mNotification);
//			mNotificationManager.cancel(NOTIFICATION_ID);
			Toast.makeText(mContext, "Download completed ! ", Toast.LENGTH_SHORT).show();
			break;
			
		case AsyncTaskUtil.NOTIFICATION_PROGRESS_UPDATE://������
			DecimalFormat format = new DecimalFormat("0.00");//���ָ�ʽת��
			String progress = format.format(mSize);
			Log.d(TAG, "the progress of the download " + progress);
			mRemoteViews.setTextViewText(R.id.id_download_textview, "Download completed : " + progress + " %");
			mRemoteViews.setProgressBar(R.id.id_download_progressbar, 100, (int)mSize, false);
			mNotification.contentView = mRemoteViews;
			mNotificationManager.notify(NOTIFICATION_ID, mNotification);
			break;
		}
	}

}

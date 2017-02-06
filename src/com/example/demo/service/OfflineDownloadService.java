package com.example.demo.service;

import com.example.demo.MainActivity;
import com.example.demo.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

public class OfflineDownloadService extends Service {
	
	/**��������service��Ӧ�������ַ���*/
	public static final String OfflineDownloadServiceAction = "com.example.demo.action.OFFLINE_DOWNLOAD_SERVICE";
	/**����������ɹ㲥�����ļ�ҳ��UI*/
	public static final String OfflineDownloadBroadcastReceiverAction = "com.example.demo.action.OFFLINE_DOWNLOAD_BROADCAST";

	private static NotificationManager manager;
	
	@Override
	public void onCreate() {
		super.onCreate();
		manager=(NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.offline_download_notification);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int requestCode = (int) SystemClock.uptimeMillis();
		Notification notifications = new NotificationCompat.Builder(getApplicationContext()).setContent(remoteViews).build();
		manager.notify(requestCode, notifications);
	}
	
	/**
	 * ��ʼ���ļ�����֪ͨnotification
	 */
	private Notification initNotification() {
		// ������תintent
		Intent intent = new Intent(getApplicationContext(), this.getClass());
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent notificationIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

		// ����֪ͨ������֪ͨ���ݣ�
		Notification notification = new Notification(R.drawable.ic_launcher, "����ͼƬ����", 100);
		notification.flags |= Notification.FLAG_ONGOING_EVENT;

		// ����֪ͨ������ʽ
		RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.offline_download_notification);
		contentView.setTextViewText(R.id.offline_download_notification_text,"��ǰ����: " + 0 + "%");
		contentView.setProgressBar(R.id.offline_download_notification_progress,100, 0, false);
		notification.contentView = contentView;
		notification.contentIntent = notificationIntent;
		return notification;
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}

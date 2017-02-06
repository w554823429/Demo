package com.example.demo.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MX on 2016/12/9.
 */
/*public class NotificationUtil {
    private Context mContext;
    // NotificationManager 锛� 鏄姸鎬佹爮閫氱煡鐨勭鐞嗙被锛岃礋璐ｅ彂閫氱煡銆佹竻妤氶�氱煡绛夈��
    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    // 瀹氫箟Map鏉ヤ繚瀛楴otification瀵硅薄
    private Map<Integer, Notification> map = null;

    public NotificationUtil(Context context) {
        this.mContext = context;
        // NotificationManager 鏄竴涓郴缁烻ervice锛屽繀椤婚�氳繃 getSystemService()鏂规硶鏉ヨ幏鍙栥��
        manager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        map = new HashMap<>();
    }

    public void showNotification(int notificationId) {
        // 鍒ゆ柇瀵瑰簲id鐨凬otification鏄惁宸茬粡鏄剧ず锛� 浠ュ厤鍚屼竴涓狽otification鍑虹幇澶氭
        if (!map.containsKey(notificationId)) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);// 1. 鍒涘缓涓�涓� NotificationCompat.Builder 瀵硅薄锛屽苟瀵瑰叾杩涜璁剧疆
            builder.setSmallIcon(R.mipmap.learner_logo); // 璁剧疆閫氱煡鐨勫浘鏍�
            builder.setTicker("闂棬瀛﹁溅棰樺簱绂荤嚎鍖呬笅杞戒腑");
            builder.setWhen(System.currentTimeMillis());
            RemoteViews rv= new RemoteViews(MyApplication.mApp.context.getPackageName(), R.layout.offline_download_notification);//鑷畾涔�
            Notification notification = builder.build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.contentView = rv; // 璋冪敤 Notification 瀵硅薄鐨� contentView 鏂规硶鏉ヤ紶鍏ヨ嚜瀹氫箟鐨勫竷灞�
            manager.notify(notificationId, notification);
            map.put(notificationId, notification);// 瀛樺叆Map涓�
        }
    }
    *//**
     * 鍙栨秷閫氱煡鎿嶄綔
     *
     * @description锛�
     * @author ldm
     * @date 2016-5-3 涓婂崍9:32:47
     *//*
    public void cancel(int notificationId) {
        manager.cancel(notificationId);
        map.remove(notificationId);
    }

    public void updateProgress(int states,int notificationId, int progress) {
        Log.e("-------------","updateProgress");
        Notification notify = map.get(notificationId);
        if (null != notify) {
            if(states==1) {//涓嬭浇涓�
                notify.contentView.setTextViewText(R.id.offline_download_notification_title,"闂棬瀛﹁溅棰樺簱绂荤嚎鍖呬笅杞戒腑");
            }else if(states==2){//涓嬭浇瀹屾垚
                notify.flags = Notification.FLAG_AUTO_CANCEL;
//                cancel(notificationId);
                notify.contentView.setTextViewText(R.id.offline_download_notification_title,"涓嬭浇瀹屾垚");
            }else if(states==-1){//涓嬭浇鍑洪敊
                notify.flags = Notification.FLAG_AUTO_CANCEL;
                notify.contentView.setTextViewText(R.id.offline_download_notification_title,"涓嬭浇鍑洪敊");
            }

            notify.contentView.setTextViewText(R.id.offline_download_notification_text, progress+"%");
            notify.contentView.setProgressBar(R.id.offline_download_notification_progress, 100, progress, false);
//            builder.setProgress(100, progress, false);
            manager.notify(notificationId, notify);
        }
    }
}*/

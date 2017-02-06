package com.example.demo;



import com.example.demo.service.OfflineDownloadService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

public class MainActivity extends Activity {
	
	RoundProgressBar roundProgressBar2;
	NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	ProgressBar offline_download_notification_progress;
	RemoteViews rv;
	int i=1;
	private Holder holder;
	/** notification������ */
	private static NotificationManager manager;
	
	private ArcProgressBar arcProgressBar;
	private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //����Ĭ�ϵ�ImageLoader���ò���  
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration  
                .createDefault(this);  
          
        //Initialize ImageLoader with configuration.  
        ImageLoader.getInstance().init(configuration);  
		manager = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        setContentView(R.layout.activity_main);
        
        iv=(ImageView) findViewById(R.id.iv);
        
        
        
        String imageUrl = "https://office.unisback.com/img/subjectsImg/1340_small.jpg";  
        
        ImageLoader.getInstance().loadImage(imageUrl, new SimpleImageLoadingListener(){  
  
            @Override  
            public void onLoadingComplete(String imageUri, View view,  
                    Bitmap loadedImage) {  
            	WindowManager wm = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
                int width = wm.getDefaultDisplay().getWidth();
                int height=wm.getDefaultDisplay().getHeight();
                super.onLoadingComplete(imageUri, view, loadedImage);  
                iv.setImageBitmap(upImageSize(MainActivity.this,loadedImage,0,0));  
            }  
              
        });  
        
        roundProgressBar2=(RoundProgressBar) findViewById(R.id.roundProgressBar2);
        roundProgressBar2.setProgress(80);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(roundProgressBar2, "alpha", 1f, 0f, 1f);  
//        animator.setDuration(5000);  
//        animator.start();
        
        arcProgressBar = (ArcProgressBar) findViewById(R.id.arcProgressBar);
        arcProgressBar.setArcText("���ϸ�");
        arcProgressBar.setProgressDesc(50+"��");
        ValueAnimator valueAnimator =ValueAnimator.ofInt(0, 100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                arcProgressBar.setProgress((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(9000);
        valueAnimator.start();
        
    }
    
    /**
     * ��Ƭ�����ı�����̬����
     * @param context 
     * @param Ҫ���ŵ�ͼƬ
     * @param width ģ����
     * @param height ģ��߶�
     * @return
     */
    public Bitmap upImageSize(Context context,Bitmap bmp, int width,int height) {
        if(bmp==null){
            return null;
        }
//        // �������
//        float scaleX = (float)width / bmp.getWidth();// ��ı���
//        float scaleY = (float)height / bmp.getHeight();// �ߵı���
        float scaleX = 2;
        float scaleY =	2;	
        //�µĿ��
        int newW = 0;
        int newH = 0;
        if(scaleX > scaleY){
            newW = (int) (bmp.getWidth() * scaleX);
            newH = (int) (bmp.getHeight() * scaleX);
        }else if(scaleX <= scaleY){
            newW = (int) (bmp.getWidth() * scaleY);
            newH = (int) (bmp.getHeight() * scaleY);
        }
        return Bitmap.createScaledBitmap(bmp, newW, newH, true);
    }

    public void show(View view){
//    	Intent intent = new Intent(OfflineDownloadService.OfflineDownloadServiceAction);
//    	intent.setPackage(getPackageName());  
//    	startService(intent);
    	// 1. ����һ�� NotificationCompat.Builder ���󣬲������������
    	builder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.ic_launcher) // ����֪ͨ��ͼ��
                .setTicker("���Ʒ���һ����Ϣ"); // ���������� 5.0 ���ϵĻ�����ʾ�����Դ˴���Ч
//                .setContentTitle("����ѧ��������߰�����") // ���ñ���ı���
//                .setContentText("������"); // ���õı�������
//        		.setProgress(100,5,false);
    	rv = new RemoteViews(getPackageName(), R.layout.offline_download_notification);
    	// ֪ͨ�а���ת�� Activity �ľ��岽�裺
        //1) ����һ�� Intent ������������Ҫ��ת����Activity
        Intent intent = new Intent(MainActivity.this, NoHttpActivity.class);

        //2) ����һ�� PendingIntent ����
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //3) ���� setContentIntent ��������֪ͨ�� PendingIntent ������
        builder.setContentIntent(pendingIntent);

        // 2. ����һ�� Notification ���󣬲������������
        // ע�⣺�� NotificationCompat.Builder �����������֮��Ŵ�����
        // ����ᵼ��֮�� NotificationCompat.Builder �����������Ч
        Notification notification = builder.build();
        notification.contentView = rv; // ���� Notification ����� contentView �����������Զ���Ĳ���
//        notification.defaults = Notification.DEFAULT_ALL; // ����֪ͨ�𶯺�����ΪĬ��
        Message msg = new Message();
        holder = new Holder();
		holder.count = i;
		holder.notification = notification;

		msg.obj = holder;
		mHandler.sendMessageDelayed(msg,1000);
        


        // 3. ����һ�� NotificationManager ��������ȡ֪ͨ������
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    			
    			                 mNotificationManager = (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
    			                 mNotificationManager.notify(0, notification);
    }
    
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
        	final Holder data = (Holder) msg.obj;
//        	Log.i("88888888888888888888888", msg.what+"");
        	if(data.count<100){
        		++i;
        		Message msg1 = new Message();
                holder = new Holder();
        		holder.count = i;
        		holder.notification = data.notification;

        		msg1.obj = holder;
        		mHandler.sendMessage(msg1);
//        		mHandler.sendMessageDelayed(msg, i);//.sendEmptyMessageDelayed(i, 1000);
        	}
        	updateNotification(data.notification,data.count);
//        	offline_download_notification_progress.setProgress(msg.what);
        }
    };
    
    private Notification updateNotification(Notification notification,
			double count) {
		// Log.e(TAG, "count--->"+count);
		RemoteViews contentView = notification.contentView;
		rv.setProgressBar(R.id.offline_download_notification_progress,100, (int) count, false);
    	rv.setTextViewText(R.id.offline_download_notification_text, count+"%");
		notification.contentView = contentView;
		manager.notify(0, notification);
		return notification;
	}
    
    public class Holder {
		/** ��̨���ؽ�����ʾ֪ͨ */
		Notification notification;
		/** ���ؽ��� */
		double count;
		/** �������ص�������� */
		String title;
		/** ÿ��֪ͨ���еı�ʶ�ţ�����ѡ���ȡ����Ӧ֪ͨ */
		int flag;
		@Override
		public String toString() {
			return "Holder [notification=" + notification + ", count=" + count
					+ ", title=" + title + ", flag=" + flag + "]";
		}
	}
    
    public void close(View view){
    	mNotificationManager.cancel(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}

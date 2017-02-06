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
	/** notification管理类 */
	private static NotificationManager manager;
	
	private ArcProgressBar arcProgressBar;
	private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //创建默认的ImageLoader配置参数  
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
        arcProgressBar.setArcText("不合格");
        arcProgressBar.setProgressDesc(50+"分");
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
     * 相片按相框的比例动态缩放
     * @param context 
     * @param 要缩放的图片
     * @param width 模板宽度
     * @param height 模板高度
     * @return
     */
    public Bitmap upImageSize(Context context,Bitmap bmp, int width,int height) {
        if(bmp==null){
            return null;
        }
//        // 计算比例
//        float scaleX = (float)width / bmp.getWidth();// 宽的比例
//        float scaleY = (float)height / bmp.getHeight();// 高的比例
        float scaleX = 2;
        float scaleY =	2;	
        //新的宽高
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
    	// 1. 创建一个 NotificationCompat.Builder 对象，并对其进行设置
    	builder = new NotificationCompat.Builder(MainActivity.this)
                .setSmallIcon(R.drawable.ic_launcher) // 设置通知的图标
                .setTicker("马云发来一条消息"); // 由于我是在 5.0 以上的机子演示，所以此处无效
//                .setContentTitle("闭门学车题库离线包下载") // 设置标题的标题
//                .setContentText("下载中"); // 设置的标题内容
//        		.setProgress(100,5,false);
    	rv = new RemoteViews(getPackageName(), R.layout.offline_download_notification);
    	// 通知中绑定跳转的 Activity 的具体步骤：
        //1) 创建一个 Intent 对象，声明我们要跳转到的Activity
        Intent intent = new Intent(MainActivity.this, NoHttpActivity.class);

        //2) 创建一个 PendingIntent 对象
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //3) 调用 setContentIntent 方法，将通知与 PendingIntent 绑定起来
        builder.setContentIntent(pendingIntent);

        // 2. 创建一个 Notification 对象，并对其进行设置
        // 注意：当 NotificationCompat.Builder 对象设置完成之后才创建，
        // 否则会导致之后 NotificationCompat.Builder 对象的设置无效
        Notification notification = builder.build();
        notification.contentView = rv; // 调用 Notification 对象的 contentView 方法来传入自定义的布局
//        notification.defaults = Notification.DEFAULT_ALL; // 设置通知震动和声音为默认
        Message msg = new Message();
        holder = new Holder();
		holder.count = i;
		holder.notification = notification;

		msg.obj = holder;
		mHandler.sendMessageDelayed(msg,1000);
        


        // 3. 创建一个 NotificationManager 对象来获取通知管理器
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
		/** 后台下载进度显示通知 */
		Notification notification;
		/** 下载进度 */
		double count;
		/** 正在下载的项的名称 */
		String title;
		/** 每个通知独有的标识号，用于选择和取消对应通知 */
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

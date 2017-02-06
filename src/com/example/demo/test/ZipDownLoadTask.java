package com.example.demo.test;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 涓嬭浇璧勬簮zip鍖�
 * Created by MX on 2016/10/12.
 */
/*public class ZipDownLoadTask extends AsyncTask<Void, Integer, Long>{

    private final String TAG = "ZipDownLoadTask";
    
    private final int DOWNLOADING=1;
    private final int DOWNLOAD_FINSH=2;
    private final int DOWNLOAD_ERROR=-1;

    private String url;
    private File mFile; //涓嬭浇鏂囦欢瀛樻斁鐨勬枃浠�
    private String downLoadPath;//涓嬭浇鏂囦欢瀛樻斁鐨勮矾寰�
    private String downFileName;//瀛樻斁涓嬭浇鏂囦欢鐨勫悕瀛�
     绂荤嚎涓嬭浇notification蹇�熻鍙栫紦瀛樼被 
    private Holder holder;
    private int count=0;
    private int progress=0;  //杩涘害
    private int length=0; //涓嬭浇鏂囦欢鐨勬�婚暱搴�
    private boolean downflag=true;
    private Context mContext;

    private List<QuestionsBean> quesList=new ArrayList<>();
    private List<AnswersBean> ansList=new ArrayList<>();

    public ZipDownLoadTask(Context mContext,String url,List<QuestionsBean> quesList,List<AnswersBean> ansList){
        this.mContext=mContext;
        this.quesList=quesList;
        this.ansList=ansList;
        this.url=url;
        Log.e("-------------","ZipDownLoadTask");
    }
    //浠诲姟鎵ц涔嬪墠鍥炶皟
    @Override
    protected void onPreExecute() {
        Log.e("-------------","onPreExecute");
        sendMsg(DOWNLOADING,progress,Contants.NOTIFICATION_FLAG);
    }

    @Override
    protected Long doInBackground(Void... params) {
        Log.e("-------------","doInBackground");
        long bytesCopied = 0;
        try {
            // 鍒ゆ柇SD鍗℃槸鍚﹀瓨鍦紝骞朵笖鏄惁鍏锋湁璇诲啓鏉冮檺
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 鑾峰緱瀛樺偍鍗＄殑璺緞
                //                String sdpath = Environment.getExternalStorageDirectory() + "/"+ "BMXC";
                downLoadPath = Contants.OFFLINE_RESOURCE_PATH;
                File file=new File(downLoadPath);
                // 鍒ゆ柇鏂囦欢鐩綍鏄惁瀛樺湪
                if (!file.exists()) {
                    file.mkdir();
                }
                URL mUrl = new URL(url);
                downFileName = new File(mUrl.getFile()).getName();
                mFile = new File(downLoadPath, downFileName);
                Log.d(TAG, "out="+downLoadPath+", name="+downFileName+",mUrl.getFile()="+mUrl.getFile());

                URLConnection connection = mUrl.openConnection();
                //璁剧疆瓒呮椂鏃堕棿
                connection.setConnectTimeout(5000);
                length = connection.getContentLength();
                // 鍒涘缓杈撳叆娴�
                FileOutputStream fos = new FileOutputStream(mFile);
                bytesCopied =copy(connection.getInputStream(),fos);
                if(bytesCopied!=length&&length!=-1){
                    Log.e(TAG, "Download incomplete bytesCopied="+bytesCopied+", length"+length);
                }
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            downflag=false;
            sendMsg(DOWNLOAD_ERROR,progress,Contants.NOTIFICATION_FLAG);
        }
        return bytesCopied;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values[1]/ 1024 / 1024 - count >= 1) {
            Log.e("-------------","onProgressUpdate");
            sendMsg(DOWNLOADING,progress,Contants.NOTIFICATION_FLAG);
        }
        count=values[1]/ 1024 / 1024;
    }

    //浠诲姟缁撴潫鍚庡洖璋�
    @Override
    protected void onPostExecute(Long aLong) {
        Log.e("-------------","onPostExecute");
        if(downflag){
            sendMsg(DOWNLOAD_FINSH,progress,Contants.NOTIFICATION_FLAG);
            if (length == mFile.length()) {
                Log.d(TAG, "file " + mFile.getName() + " already exits!!");
                doZipExtractorWork();
            }
        }
    }
    //浠诲姟琚彇娑堟椂鍥炶皟
    @Override
    protected void onCancelled() {
        super.onCancelled();
        MyApplication.mApp.notificationUtil.cancel(Contants.NOTIFICATION_FLAG);
        Toast.makeText(MyApplication.mApp.context,"浠诲姟宸插彇娑�",Toast.LENGTH_SHORT).show();
    }

    public void doZipExtractorWork(){
        ZipExtractorTask task = new ZipExtractorTask(mContext,downLoadPath+"/"+downFileName, downLoadPath, true);
        task.execute();
    }

    Handler handlers = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Holder data = (Holder) msg.obj;
            if(MyApplication.mApp.notificationUtil!=null)
                MyApplication.mApp.notificationUtil.updateProgress(msg.what,data.notificationId,data.progress);
            if(msg.what==DOWNLOAD_FINSH){
                //灏嗛搴撴暟鎹啓鍒版湰鍦版暟鎹簱
                if(quesList.size()>0){
                    TheoryManager.insert_quesList(quesList,null,null);
                    TheoryManager.insert_answerList(ansList);
                    DataSaveModel.saveTheoryoffline(mContext,true);
                    DataSaveModel.saveTheoryUpdateTime(mContext, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }
            }
        }
    };

    *//**
     * 鏄剧ず閫氱煡鏍�
     *//*
    public Notification showNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);// 1. 鍒涘缓涓�涓� NotificationCompat.Builder 瀵硅薄锛屽苟瀵瑰叾杩涜璁剧疆
        builder.setSmallIcon(R.mipmap.learner_logo); // 璁剧疆閫氱煡鐨勫浘鏍�
        builder.setTicker("闂棬瀛﹁溅棰樺簱绂荤嚎鍖呬笅杞�");
        RemoteViews rv= new RemoteViews(MyApplication.mApp.context.getPackageName(), R.layout.offline_download_notification);//鑷畾涔�
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.contentView = rv; // 璋冪敤 Notification 瀵硅薄鐨� contentView 鏂规硶鏉ヤ紶鍏ヨ嚜瀹氫箟鐨勫竷灞�
        sendMsg(DOWNLOADING,progress,notification,flags,"涓嬭浇涓�");
        manager = (NotificationManager) MyApplication.mApp.context.getSystemService(MyApplication.mApp.context.NOTIFICATION_SERVICE);
        manager.notify(flags, notification);
        return notification;
    }

    *//**
     * 鏇存柊杩涘害鏉�
     * @param notification
     * @param count
     * @return
     *//*
    private Notification updateNotification(Notification notification,int count,String title) {
        Log.i("99999999999999999999",count+"");
        RemoteViews contentView = notification.contentView;
        contentView.setProgressBar(R.id.offline_download_notification_progress,100, count, false);
        contentView.setTextViewText(R.id.offline_download_notification_text, count+"%");
        contentView.setTextViewText(R.id.offline_download_notification_title,title);
        notification.contentView = contentView;
        manager.notify(flags, notification);
        return notification;
    }

    *//**
     * 灏佽浜嗗彂閫乭andler锛岄�氱煡鏇存柊UI瑙嗗浘鐨勬楠�
     * @param what  handler鎵ц鍒ゆ柇鏍囪瘑锛屾洿鏂拌繘搴︽垨鑰呬笅杞藉嚭閿欍�佸畬鎴�
     * @param progress 涓嬭浇杩涘害
     * @param notificationId  杩涘害閫氱煡鏍囪瘑鍙�
     *//*
    private void sendMsg(int what, int progress,int notificationId){
        Message msg = new Message();
        msg.what = what;
        holder = new Holder();
        holder.progress = progress;
        holder.notificationId = notificationId;
        msg.obj = holder;
        handlers.sendMessage(msg);
    }

    *//**
     * 鍚庡彴涓嬭浇notification蹇�熻鍙栫紦瀛樼被
     *//*
    public class Holder {
        *//**
         * 涓嬭浇杩涘害
         *//*
        int progress;
        *//**
         * 姣忎釜閫氱煡鐙湁鐨勬爣璇嗗彿锛岀敤浜庨�夋嫨鍜屽彇娑堝搴旈�氱煡
         *//*
        int notificationId;

        @Override
        public String toString() {
            return "Holder [progress=" + progress
                    +  ", flag=" + notificationId + "]";
        }
    }

    private int copy(InputStream input, OutputStream output){
        byte[] buffer = new byte[1024*2];
        BufferedInputStream in = new BufferedInputStream(input, 1024*2);
        BufferedOutputStream out  = new BufferedOutputStream(output, 1024*2);
        int count =0,n=0;
        try {
            while((n=in.read(buffer, 0, 1024*2))!=-1){
                out.write(buffer, 0, n);
                count+=n;
                //璁＄畻杩涘害
                progress = (int)((double)count / length * 100);//鍏堣绠楀嚭鐧惧垎姣斿湪杞崲鎴愭暣鍨�
                //鏇存柊杩涘害
                publishProgress(progress, count, length);
            }
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            downflag=false;
            sendMsg(DOWNLOAD_ERROR,Contants.NOTIFICATION_FLAG,progress);
        }
        return count;
    }
}*/

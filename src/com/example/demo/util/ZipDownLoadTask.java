package com.example.demo.util;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by MX on 2016/10/12.
 */
public class ZipDownLoadTask extends AsyncTask<Void, Integer, Long>{

    private final String TAG = "ZipDownLoadTask";
    
    private final int DOWNLOADING=1;
    private final int DOWNLOAD_FINSH=2;
    private final int DOWNLOAD_ERROR=-1;

    private String url;
    private File mFile; //涓嬭浇鏂囦欢瀛樻斁鐨勬枃浠�
    private String downLoadPath;//涓嬭浇鏂囦欢瀛樻斁鐨勮矾寰�
    private String downFileName;//瀛樻斁涓嬭浇鏂囦欢鐨勫悕瀛�
    private int count=0;
    private int progress=0;  //杩涘害
    private int length=0; //涓嬭浇鏂囦欢鐨勬�婚暱搴�
    private boolean downflag=true;
    private Context mContext;


    public ZipDownLoadTask(Context mContext,String url){
        this.mContext=mContext;
        this.url=url;

    }
    //浠诲姟鎵ц涔嬪墠鍥炶皟
    @Override
    protected void onPreExecute() {
        /*if(MyApplication.mApp.notification==null){
            MyApplication.mApp.notification =showNotification();
        }*/
    }

    @Override
    protected Long doInBackground(Void... params) {
        long bytesCopied = 0;
        try {
            // 鍒ゆ柇SD鍗℃槸鍚﹀瓨鍦紝骞朵笖鏄惁鍏锋湁璇诲啓鏉冮檺
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 鑾峰緱瀛樺偍鍗＄殑璺緞
                //                String sdpath = Environment.getExternalStorageDirectory() + "/"+ "BMXC";
                downLoadPath = Environment.getExternalStorageDirectory() + "/"+ "BMXC";
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
        }
        return bytesCopied;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values[1]/ 1024 / 1024 - count >= 1) {
        }
        count=values[1]/ 1024 / 1024;
        Toast.makeText(mContext, count+"", Toast.LENGTH_SHORT).show();
    }

    //浠诲姟缁撴潫鍚庡洖璋�
    @Override
    protected void onPostExecute(Long aLong) {
        if(downflag){
            if (length == mFile.length()) {
                Log.d(TAG, "file " + mFile.getName() + " already exits!!");
            }
        }
    }
    //浠诲姟琚彇娑堟椂鍥炶皟
    @Override
    protected void onCancelled() {
        super.onCancelled();
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

        }
        return count;
    }
}

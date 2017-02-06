package com.example.demo;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.util.AsyncTaskUtil;
import com.example.demo.util.ZipDownLoadTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogActivity extends Activity {
	
	private Button btn_show;
	private TextView tv_info;
	private List<String> mList=new ArrayList<String>();
	private Handler mHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog);
		btn_show=(Button) findViewById(R.id.btn_show);
		tv_info=(TextView) findViewById(R.id.tv_info);
		//AsyncTask异步下载任务
		AsyncTaskUtil mDownloadAsyncTask = new AsyncTaskUtil(DialogActivity.this, mHandler);
		mDownloadAsyncTask.execute("https://office.unisback.com/zip/resource.zip");
//		new ZipDownLoadTask(DialogActivity.this,"https://office.unisback.com/zip/resource.zip").execute();
		/*//创建下载任务,downloadUrl就是下载链接
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://office.unisback.com/zip/resource.zip"));
		//指定下载路径和下载文件名
		request.setDestinationInExternalPublicDir("/download/", "BMXC");
		//获取下载管理器
		DownloadManager downloadManager= (DownloadManager) DialogActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
		//将下载任务加入下载队列，否则不会进行下载
		downloadManager.enqueue(request);*/
		for(int i=0;i<60;i++){
			mList.add(i+"");
		}
		btn_show.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(getWinWidth(DialogActivity.this),getWinHeight(DialogActivity.this)*4/5);   
				 CancelDialog1(DialogActivity.this).show();
			}
		});
	}
	public Dialog CancelDialog1(Context context) {
        final Dialog dlg = new Dialog(context, R.style.CancelTheme_DataSheet);// 对话框主题
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.win_dialog_exp, null);
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM; 
        dlg.getWindow().setWindowAnimations(R.style.AnimBottom);
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setCancelable(true);
        dlg.setContentView(layout);
        // 设置宽高
        dlg.getWindow().setLayout(getWinWidth(DialogActivity.this), (int) (getWinHeight(DialogActivity.this)*0.9));
        dlg.show();
        return dlg;
    }
	public Dialog CancelDialog(Context context) {
        final Dialog dlg = new Dialog(context, R.style.CancelTheme_DataSheet);// 对话框主题
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.win_dialog, null);
        GridView gv_thepry_list = (GridView) layout.findViewById(R.id.gv_thepry_list);
        gv_thepry_list.setAdapter(new GridAdapter());
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM; 
        dlg.getWindow().setWindowAnimations(R.style.AnimBottom);
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setCancelable(true);
        dlg.setContentView(layout);
        // 设置宽高
        dlg.getWindow().setLayout(getWinWidth(DialogActivity.this), getWinHeight(DialogActivity.this)*4/5);
        dlg.show();
        return dlg;
    }
	
	 public int getWinWidth(Context mContext) {
	        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	        return wm.getDefaultDisplay().getWidth();
	    }

	    public int getWinHeight(Context mContext) {
	        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
	        return wm.getDefaultDisplay().getHeight();
	    }
	
	class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(DialogActivity.this).inflate(R.layout.layout_notdown_gv, null);
                viewHolder.tv_theory_notdown_list = (TextView) convertView.findViewById(R.id.tv_theory_notdown_list);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_theory_notdown_list.setText(position+1+"");
            return convertView;
        }

        class ViewHolder {
            TextView tv_theory_notdown_list;
        }
    }

}

package com.example.demo.dem;

import java.io.File;
import java.net.URISyntaxException;

import com.example.demo.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 纭瀵硅瘽妗?
 * Created by MX on 2015/11/4.
 */
public class NavigationDialog {

    public static Dialog CancelDialog(final boolean flag,final Context context,final String[] startLocation,final String[] endLocation) {
    	PackageManager pm = context.getPackageManager();
    	final Dialog dlg = new Dialog(context, R.style.CancelTheme_DataSheet);// 瀵硅瘽妗嗕富棰?
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.navigation_dialog, null);
        TextView tv_gaode = (TextView) layout.findViewById(R.id.tv_gaode);
        TextView tv_baidu = (TextView) layout.findViewById(R.id.tv_baidu);
        TextView tv_tengxun = (TextView) layout.findViewById(R.id.tv_tengxun);
//        if(isInstallByread("com.autonavi.minimap")){
        	tv_gaode.setVisibility(View.VISIBLE);
        	String name=getAppName(pm, "com.autonavi.minimap");
        	tv_gaode.setText(TextUtils.isEmpty(name)?"高德地图"+"(推荐)":name+"(推荐)");
				Drawable drawable = getAppIcon(pm, "com.autonavi.minimap");
				if(drawable!=null){
					// 这一步必须要做,否则不会显示.
		        	drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		        	tv_gaode.setCompoundDrawables(drawable,null,null,null);
				}
        	tv_gaode.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(flag){
						Intent intent = new Intent("android.intent.action.VIEW",android.net.Uri.parse("androidamap://viewMap?sourceApplication=闭门学车&poiname=顾村基地&lat="+endLocation[0]+"&lon="+endLocation[1]+"&dev=0"));
						intent.setPackage("com.autonavi.minimap");
						context.startActivity(intent); // 启动调用
					}else{//打开下载页面
						Toast.makeText(context, "您尚未安装高德地图", Toast.LENGTH_LONG).show();
                        Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");  
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);   
                        context.startActivity(intent);
					}
				}
			});
//        }
        if(isInstallByread("com.baidu.BaiduMap")){
        	tv_baidu.setVisibility(View.VISIBLE);
        	name=getAppName(pm, "com.baidu.BaiduMap");
        	tv_baidu.setText(TextUtils.isEmpty(name)?"百度地图":name);
				drawable = getAppIcon(pm,"com.baidu.BaiduMap");
				if(drawable!=null){
					// 这一步必须要做,否则不会显示.
		        	drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		        	tv_baidu.setCompoundDrawables(drawable,null,null,null);
				}
        	tv_baidu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						@SuppressWarnings("deprecation")
						Intent intent = Intent.getIntent("intent://map/direction?" +  
//						        "origin=latlng:"+startLocation[0]+","+startLocation[1]+"&" +   //起点  此处不传值默认选择当前位置
						        "destination=latlng:"+endLocation[0]+","+endLocation[1]+"|name:顾村基地"+        //终点
						        "&mode=driving&" +          //导航路线方式
						        "region=上海" +           //
						        "&src=宝山沪太路顾陈路388号#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
						context.startActivity(intent); //启动调用
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}  
					
				}
			});
        }
        if(isInstallByread("com.tencent.map")){
        	tv_tengxun.setVisibility(View.VISIBLE);
        	name=getAppName(pm, "com.tencent.map");
        	tv_tengxun.setText(TextUtils.isEmpty(name)?"腾讯地图":name);
				drawable = getAppIcon(pm,"com.tencent.map");
				if(drawable!=null){
					/// 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
					tv_tengxun.setCompoundDrawables(drawable,null,null,null);
				}
        	tv_tengxun.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					try {
						@SuppressWarnings("deprecation")
						Intent intent = Intent.getIntent
						        ("qqmap://map/routeplan?type=drive&from=我的位置&fromcoord="+startLocation[0]+","+startLocation[1]+"&to=宝山沪太路顾陈路388号&tocoord="+endLocation[0]+","+endLocation[1]);
						context.startActivity(intent); // 启动调用
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}
			});
        }
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.CENTER_VERTICAL;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
//        dlg.setCancelable(false);
        dlg.setContentView(layout);
        dlg.show();
        return dlg;
    }
    
    //判断是否安装目标应用
    public static boolean isInstallByread(String packageName) {
	 return new File("/data/data/" + packageName)
	         .exists();
	}
	/* 
     * 获取程序 图标 
     */  
    public static Drawable getAppIcon(PackageManager pm,String packname){  
      try {  
             ApplicationInfo info = pm.getApplicationInfo(packname, 0);   
             return info.loadIcon(pm);  
        } catch (NameNotFoundException e) {  
            e.printStackTrace();  
            
        }
	return null;  
    }
    
    /* 
     * 获取程序的名字  
     */  
    public static String getAppName(PackageManager pm,String packname){  
          try {  
                 ApplicationInfo info = pm.getApplicationInfo(packname, 0);   
                 return info.loadLabel(pm).toString();  
            } catch (NameNotFoundException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
               
            }
		return null;  
    }
}

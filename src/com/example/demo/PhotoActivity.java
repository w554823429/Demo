package com.example.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoActivity extends Activity {
	
	private ImageView iv_photo;
	private Uri uriImageview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo);
		int h = getWindowManager().getDefaultDisplay().getHeight();
        int w = getWindowManager().getDefaultDisplay().getWidth();
        iv_photo=(ImageView) findViewById(R.id.iv_photo);
        LayoutParams params = iv_photo.getLayoutParams();  
        params.height=h/3;  
        params.width =w/2;  
        iv_photo.setLayoutParams(params); 
	}
	public void openxc(View view){
		Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
	}
	public void openxj(View view){
		//以系统时间作为该文件 民命
        SimpleDateFormat formatter  =   new    SimpleDateFormat("yyyy年MM月dd日HH-mm-ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String    str    =    formatter.format(curDate);
        Log.e("拍照---->",str);
        //建立file文件用于保存来拍照后的图片
        File outputFile = new File(Environment.getExternalStorageDirectory(),str+".jpg");
        /**
         * 使用隐式intent进行跳转
         */
        try {
            if (outputFile.exists()){
                outputFile.delete();
            }
            outputFile.createNewFile();

        }catch (Exception e){
            e.printStackTrace();
        }
        uriImageview = Uri.fromFile(outputFile);
        //另mainactivity的uri等于本.java 文件中的  uri
        uriImageview = uriImageview;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uriImageview);
        //启动相机程序
        startActivityForResult(intent,2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case 1:
				if (resultCode == RESULT_OK){
                    /**
                     * 判断手机版本，因为在4.4版本都手机处理图片返回的方法就不一样了
                     * 4.4以后返回的不是真实的uti而是一个封装过后的uri 所以要对封装过后的uri进行解析
                     */
 
                    if (Build.VERSION.SDK_INT >=19){
                        //4.4系统一上用该方法解析返回图片
                        handleImageOnKitKat(data);
                    }else{
                        //4.4一下用该方法解析图片的获取
                        handleImageBeforeKitKat(data);
                    }
                }
				break;
			case 2:
				Log.e("case2---->", "22222222222222222222");
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(uriImageview,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,uriImageview);
                    startActivityForResult(intent,3);//启动裁剪程序
                }
				break;
			case 3:
                if (resultCode == RESULT_OK){
                    try{
                        Log.e("case3---->", "3333333333333");
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(uriImageview));
                        //将裁剪后的图片显示出来
                        iv_photo.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
     * 4.4版本一下 直接获取uri进行图片处理
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
 
	
	/**
     * api 19以后
     *  4.4版本后 调用系统相机返回的不在是真实的uri 而是经过封装过后的uri，
     * 所以要对其记性数据解析，然后在调用displayImage方法尽心显示
     * @param data
     */
 
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的uri 则通过id进行解析处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                //解析出数字格式id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                        "content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equals(uri.getScheme())){
            //如果不是document类型的uri，则使用普通的方式处理
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }
    
    /**
     * 通过 uri seletion选择来获取图片的真实uri
     * @param uri
     * @param seletion
     * @return
     */
    private String getImagePath(Uri uri, String seletion){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,seletion,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    
    /**
     * 通过imagepath来绘制immageview图像
     * @param imagPath
     */
    private void displayImage(String imagPath){
        if (imagPath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagPath);
            iv_photo.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"图片获取失败",Toast.LENGTH_SHORT).show();
        }
    }
}

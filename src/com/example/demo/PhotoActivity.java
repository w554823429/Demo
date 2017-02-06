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
		//��ϵͳʱ����Ϊ���ļ� ����
        SimpleDateFormat formatter  =   new    SimpleDateFormat("yyyy��MM��dd��HH-mm-ss");
        Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
        String    str    =    formatter.format(curDate);
        Log.e("����---->",str);
        //����file�ļ����ڱ��������պ��ͼƬ
        File outputFile = new File(Environment.getExternalStorageDirectory(),str+".jpg");
        /**
         * ʹ����ʽintent������ת
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
        //��mainactivity��uri���ڱ�.java �ļ��е�  uri
        uriImageview = uriImageview;
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uriImageview);
        //�����������
        startActivityForResult(intent,2);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode){
			case 1:
				if (resultCode == RESULT_OK){
                    /**
                     * �ж��ֻ��汾����Ϊ��4.4�汾���ֻ�����ͼƬ���صķ����Ͳ�һ����
                     * 4.4�Ժ󷵻صĲ�����ʵ��uti����һ����װ�����uri ����Ҫ�Է�װ�����uri���н���
                     */
 
                    if (Build.VERSION.SDK_INT >=19){
                        //4.4ϵͳһ���ø÷�����������ͼƬ
                        handleImageOnKitKat(data);
                    }else{
                        //4.4һ���ø÷�������ͼƬ�Ļ�ȡ
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
                    startActivityForResult(intent,3);//�����ü�����
                }
				break;
			case 3:
                if (resultCode == RESULT_OK){
                    try{
                        Log.e("case3---->", "3333333333333");
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(uriImageview));
                        //���ü����ͼƬ��ʾ����
                        iv_photo.setImageBitmap(bitmap);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
     * 4.4�汾һ�� ֱ�ӻ�ȡuri����ͼƬ����
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }
 
	
	/**
     * api 19�Ժ�
     *  4.4�汾�� ����ϵͳ������صĲ�������ʵ��uri ���Ǿ�����װ�����uri��
     * ����Ҫ����������ݽ�����Ȼ���ڵ���displayImage����������ʾ
     * @param data
     */
 
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            //�����document���͵�uri ��ͨ��id���н�������
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                //���������ָ�ʽid
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" +id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("" +
                        "content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if ("content".equals(uri.getScheme())){
            //�������document���͵�uri����ʹ����ͨ�ķ�ʽ����
            imagePath = getImagePath(uri,null);
        }
        displayImage(imagePath);
    }
    
    /**
     * ͨ�� uri seletionѡ������ȡͼƬ����ʵuri
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
     * ͨ��imagepath������immageviewͼ��
     * @param imagPath
     */
    private void displayImage(String imagPath){
        if (imagPath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagPath);
            iv_photo.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"ͼƬ��ȡʧ��",Toast.LENGTH_SHORT).show();
        }
    }
}

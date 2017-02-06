package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.example.demo.receiver.AlamrReceiver;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class AlarmManagerActivity extends Activity {
	private TextView tv_time;
    private Calendar c = null; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		tv_time=(TextView) findViewById(R.id.tv_time);
		c = Calendar.getInstance();  
		findViewById(R.id.btn_settings).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				c.setTimeInMillis(System.currentTimeMillis());  
                int hour = c.get(Calendar.HOUR_OF_DAY);  
                int minute = c.get(Calendar.MINUTE);  
				new TimePickerDialog(AlarmManagerActivity.this,new TimePickerDialog.OnTimeSetListener(){  
                    public void onTimeSet(TimePicker view, int hourOfDay,  
                            int minute) {  
                        // TODO Auto-generated method stub  
                        c.setTimeInMillis(System.currentTimeMillis());  
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);  
                        c.set(Calendar.MINUTE, minute);  
                        c.set(Calendar.SECOND, 0);  
                        c.set(Calendar.MILLISECOND, 0);  
                        Intent intent = new Intent(AlarmManagerActivity.this,AlamrReceiver.class);  
                        PendingIntent pi = PendingIntent.getBroadcast(AlarmManagerActivity.this, 0, intent, 0);  
                        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
                        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);//设置闹钟  
                        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), (10*1000), pi);//重复设置  
                        tv_time.setText("设置的闹钟时间为："+hourOfDay+":"+minute);  
                    }
                      
                },hour,minute,true).show();  
			}
		});
		findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AlarmManagerActivity.this,AlamrReceiver.class);  
                PendingIntent pi = PendingIntent.getBroadcast(AlarmManagerActivity.this, 0, intent, 0);  
                AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
                am.cancel(pi);  
                tv_time.setText("闹钟取消");  
                Intent in = new Intent(AlarmManagerActivity.this,AlamrReceiver.class);  
                in.putExtra("iscolse", true);
                sendBroadcast(in);
			}
		});
	}
	public String getTime(){
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		return formatter.format(curDate);
	}
}

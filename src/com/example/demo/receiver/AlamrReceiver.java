package com.example.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.widget.Toast;

public class AlamrReceiver extends BroadcastReceiver {
	public Vibrator vibrator;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		 vibrator = (Vibrator)arg0.getSystemService(Context.VIBRATOR_SERVICE);
		if(arg1.getBooleanExtra("iscolse", false)){
			Toast.makeText(arg0, "����ȡ��", Toast.LENGTH_LONG).show();
			vibrator.cancel();
		}else{
			Toast.makeText(arg0, "����ʱ�䵽", Toast.LENGTH_LONG).show();
			 long [] pattern = {1000,4000,1000,4000}; // ֹͣ ���� ֹͣ ����
			 vibrator.vibrate(pattern,2); //�ظ����������pattern ���ֻ����һ�Σ�index��Ϊ-1
		}
	}

}

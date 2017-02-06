package com.example.demo;

import java.util.List;

import com.example.demo.annotation.InjectView;
import com.example.demo.annotation.InjectViewParser;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends Activity {
	@InjectView(id = R.id.tv_info)
	private TextView tv_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);
		InjectViewParser.inject(this);
		SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> allSensors = sm.getSensorList(Sensor.TYPE_ALL);
		tv_info.setText("�������ֻ���" + allSensors.size() + "�������������Ƿֱ��ǣ�\n");
		for (Sensor s : allSensors) {
			String tempString = "\n" + "  �豸���ƣ�" + s.getName() + "\n"
					+ "  �豸�汾��" + s.getVersion() + "\n" + "  ��Ӧ�̣�"
					+ s.getVendor() + "\n";
			switch (s.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ "���ٶȴ�����accelerometer" + tempString);
				break;
			case Sensor.TYPE_GYROSCOPE:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " �����Ǵ�����gyroscope" + tempString);
				break;
			case Sensor.TYPE_LIGHT:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " �������ߴ�����light" + tempString);
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " ��ų�������magnetic field" + tempString);
				break;
			case Sensor.TYPE_ORIENTATION:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " ���򴫸���orientation" + tempString);
				break;
			case Sensor.TYPE_PRESSURE:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " ѹ��������pressure" + tempString);
				break;
			case Sensor.TYPE_PROXIMITY:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " ���봫����proximity" + tempString);
				break;
			case Sensor.TYPE_TEMPERATURE:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " �¶ȴ�����temperature" + tempString);
				break;
			default:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " δ֪������" + tempString);
				break;
			}
		}

	}
}

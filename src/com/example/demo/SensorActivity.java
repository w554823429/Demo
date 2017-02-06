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
		tv_info.setText("经检测该手机有" + allSensors.size() + "个传感器，他们分别是：\n");
		for (Sensor s : allSensors) {
			String tempString = "\n" + "  设备名称：" + s.getName() + "\n"
					+ "  设备版本：" + s.getVersion() + "\n" + "  供应商："
					+ s.getVendor() + "\n";
			switch (s.getType()) {
			case Sensor.TYPE_ACCELEROMETER:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ "加速度传感器accelerometer" + tempString);
				break;
			case Sensor.TYPE_GYROSCOPE:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 陀螺仪传感器gyroscope" + tempString);
				break;
			case Sensor.TYPE_LIGHT:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 环境光线传感器light" + tempString);
				break;
			case Sensor.TYPE_MAGNETIC_FIELD:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 电磁场传感器magnetic field" + tempString);
				break;
			case Sensor.TYPE_ORIENTATION:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 方向传感器orientation" + tempString);
				break;
			case Sensor.TYPE_PRESSURE:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 压力传感器pressure" + tempString);
				break;
			case Sensor.TYPE_PROXIMITY:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 距离传感器proximity" + tempString);
				break;
			case Sensor.TYPE_TEMPERATURE:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 温度传感器temperature" + tempString);
				break;
			default:
				tv_info.setText(tv_info.getText().toString() + s.getType()
						+ " 未知传感器" + tempString);
				break;
			}
		}

	}
}

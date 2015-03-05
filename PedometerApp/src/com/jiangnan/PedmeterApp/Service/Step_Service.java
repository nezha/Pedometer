package com.jiangnan.PedmeterApp.Service;

import com.jiangnan.PedmeterApp.Util.SensorUtil;
import com.jiangnan.PedmeterApp.Util.StepDetector;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.widget.Toast;


/**
 * 
 * @author zhangyi 1061243497@qq.com
 * @since 2015/2/8
 * @description 主要是用service把计步程序放在服务中运行
 *
 */
public class Step_Service extends Service implements SensorEventListener{
	SensorUtil SU;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(this, "--service started--", Toast.LENGTH_LONG).show();
		SU = new SensorUtil(this);
		SU.registerListener();
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SU.unregisterListener();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent e) {
		// TODO Auto-generated method stub
		SU.routeEvent(e);
		//Toast.makeText(this, StepDetector.CURRENT_SETP, Toast.LENGTH_LONG).show();
		//System.out.println("--"+StepDetector.CURRENT_SETP);
	}

	
	
	

}

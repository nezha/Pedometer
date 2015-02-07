package com.jiangnan.PedmeterApp;

import com.jiangnan.PedmeterApp.Util.SensorUtil;
import com.jiangnan.PedmeterApp.Util.StepDetector;
import com.jiangnan.PedmeterApp.Util.Step_Detector;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;;

public class MainActivity extends Activity implements SensorEventListener{
	/**
	 * @author zhangyi   1061243497@qq.com
	 * @since 2015/2/6/14:26
	 * @description ���������棬��Ҫʵ�ֵĹ�����ʵʱ�Ĳ�����ʾ
	 * 1��ʵ�ֲ�������ʾ
	 * 2��ʵ��Sensor�е�onSensorChanged()�ȷ�����ʵ�ֶ�̬���²���
	 * 3��
	 */

	private TextView stepShow;
	private SensorUtil SU;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SU = new SensorUtil(this);
		setContentView(R.layout.activity_main);
		stepShow = (TextView)findViewById(R.id.steps_show);
		stepShow.setText("0 ��");
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		SU.unregisterListener();
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SU.registerListener();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * �������������仯ʱ�ģ������ı�����ʵ��
	 */
	@Override
	public void onSensorChanged(SensorEvent e) {
		// TODO Auto-generated method stub
		SU.routeEvent(e);
		int Feet = StepDetector.CURRENT_SETP;
		stepShow.setText(Feet + "��");
	}

}

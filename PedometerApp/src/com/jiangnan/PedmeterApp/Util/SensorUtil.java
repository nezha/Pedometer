package com.jiangnan.PedmeterApp.Util;

import java.util.HashMap;
import java.util.Map;

import com.jiangnan.PedmeterApp.Application.SensorMap;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorUtil {
	/**
	 * @author zhangyi 1061243497@qq.com
	 * @description ���ڴ���һЩ���ٶȸ�Ӧ�������룬ע�ᣬע���Ȳ���
	 * 1����ȡSensorManager��ʵ��
	 * 2����ȡSensor��ʵ��
	 * 3��ע��SensorManager
	 * 4��ע��SensorManager
	 */
	private SensorManager sensorManager;
	private Sensor accSensor;
	private SensorEventListener sensorListener;
	private Map<Integer,EventHandler> mHandlers;
	/*
	 * 1�����������ع��캯���������ڲ�ͬ������µĵ��÷���
	 * 2��Ȼ��SensorEventListener���¼���������Activity��ȥʵ�֣���ȻҲ�����ڵ�����һ�����д�����������˵��
	 */
	public SensorUtil(Service s)
	{
		//����service����ʱ����Ҫ�������仯����д��./Service/Step_Detector_Service.java
		sensorManager =(SensorManager)s.getSystemService(Context.SENSOR_SERVICE);
		sensorListener = (SensorEventListener)s;
		//sensorListener = (SensorEventListener)stepDetectorService;
		mHandlers = new HashMap<Integer, EventHandler>();
		
	}
	public SensorUtil(Activity a)
	{
		sensorManager =(SensorManager)a.getSystemService(Context.SENSOR_SERVICE);
		sensorListener = (SensorEventListener)a;
		mHandlers = new HashMap<Integer, EventHandler>();
	}
	
	/*
	 * 1.��ȡsensorʵ��
	 * 2.ע��SensorManager
	 */
	public void registerListener()
	{
		accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(sensorListener, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
		registerHandlers();
		
	}
	/**
	 * ע��������,�ͷŵ�������
	 * �����ͷŵ�map�еľ��
	 */
	
	public void unregisterListener()
	{
		sensorManager.unregisterListener(sensorListener);
		unregisterHandlers();
	}
	
	/**
	 * ʵ�ʷ���map�е��Ƕ�Ӧ�ڲ�ͬ�������Ʋ��㷨�ļ����
	 * AccelerationHandler()ʵ����EventHandler�ӿ��еķ���
	 * ��SensorMap�еľ�̬��������ͬ�����
	 */
	private void registerHandlers()
	{
		mHandlers.put(Integer.valueOf(Sensor.TYPE_ACCELEROMETER), new AccelerationHandler());
		SensorMap.setmHandlers(mHandlers);
	}
	
	private void unregisterHandlers()
	{
		mHandlers.clear();
		SensorMap.getmHandlers().clear();
	}
	/*
	 * ��ȡ���ڼ��ٶȼƲ��㷨�����ʵ������
	 */
	public void routeEvent(SensorEvent e)
	{
		EventHandler h = mHandlers.get(Integer.valueOf(e.sensor.getType()));
		if(h != null)
			h.service(e);
	}
	
	
	

}

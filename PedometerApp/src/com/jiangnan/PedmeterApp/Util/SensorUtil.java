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
	 * @description 用于处理一些加速度感应器的申请，注册，注销等操作
	 * 1、获取SensorManager是实例
	 * 2、获取Sensor的实例
	 * 3、注册SensorManager
	 * 4、注销SensorManager
	 */
	private SensorManager sensorManager;
	private Sensor accSensor;
	private SensorEventListener sensorListener;
	private Map<Integer,EventHandler> mHandlers;
	/*
	 * 1、下面是重载构造函数，用于在不同的情况下的调用方便
	 * 2、然后将SensorEventListener的事件监听放在Activity中去实现，当然也可以在单独的一个类中处理，下面会具体说的
	 */
	public SensorUtil(Service s)
	{
		//当用service处理时，需要将监听变化的类写在./Service/Step_Detector_Service.java
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
	 * 1.获取sensor实例
	 * 2.注册SensorManager
	 */
	public void registerListener()
	{
		accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(sensorListener, accSensor, SensorManager.SENSOR_DELAY_FASTEST);
		registerHandlers();
		
	}
	/**
	 * 注销传感器,释放掉传感器
	 * 并且释放掉map中的句柄
	 */
	
	public void unregisterListener()
	{
		sensorManager.unregisterListener(sensorListener);
		unregisterHandlers();
	}
	
	/**
	 * 实际放入map中的是对应于不同传感器计步算法的检测类
	 * AccelerationHandler()实现了EventHandler接口中的方法
	 * 将SensorMap中的静态数据用于同步句柄
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
	 * 获取属于加速度计步算法的类的实例对象
	 */
	public void routeEvent(SensorEvent e)
	{
		EventHandler h = mHandlers.get(Integer.valueOf(e.sensor.getType()));
		if(h != null)
			h.service(e);
	}
	
	
	

}

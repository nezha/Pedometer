package com.jiangnan.PedmeterApp.Util;

import android.hardware.SensorEvent; 

/**
 *@description 这是一个接口，实现它的内部的方法可以实现不同传感器的计步
 */
public interface EventHandler {
	public void service(SensorEvent e);
}

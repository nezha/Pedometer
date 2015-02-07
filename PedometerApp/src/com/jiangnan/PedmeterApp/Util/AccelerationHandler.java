package com.jiangnan.PedmeterApp.Util;

import android.hardware.SensorEvent;

public class AccelerationHandler implements EventHandler{

	@Override
	public void service(SensorEvent e) {
		// TODO Auto-generated method stub
		StepDetector.accelerationUpdate(e);
	}

}

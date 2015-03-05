package com.jiangnan.PedmeterApp.Application;

import java.util.Map;

import com.jiangnan.PedmeterApp.Util.EventHandler;

public class SensorMap {
	private static Map<Integer,EventHandler> mHandlers;

	public static Map<Integer, EventHandler> getmHandlers() {
		return mHandlers;
	}

	public static void setmHandlers(Map<Integer, EventHandler> mHandlers) {
		SensorMap.mHandlers = mHandlers;
	}
	
	

}

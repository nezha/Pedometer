package com.jiangnan.PedmeterApp.Util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;


public class StepDetector{

	public static int CURRENT_SETP = 0;
	public static float SENSITIVITY = 10; // SENSITIVITY
	private static float mLastValues[] = new float[3 * 2];
	private static float mScale[] = new float[2];
	private static float mYOffset;
	private static long end = 0;
	private static long start = 0;

	private static float mLastDirections[] = new float[3 * 2];
	private static float mLastExtremes[][] = { new float[3 * 2], new float[3 * 2] };
	private static float mLastDiff[] = new float[3 * 2];
	private static int mLastMatch = -1;
	
	public static void accelerationUpdate(SensorEvent event)
	{
		if(CURRENT_SETP == 0)
		{
			int h = 480;
			mYOffset = h * 0.5f;
			mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
			mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
		}
		Sensor sensor = event.sensor;
			if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

				float vSum = 0;
				for (int i = 0; i < 3; i++) {
					final float v = mYOffset + event.values[i] * mScale[1];
					vSum += v;
				}
				int k = 0;
				float v = vSum / 3;

				float direction = (v > mLastValues[k] ? 1
						: (v < mLastValues[k] ? -1 : 0));
				if (direction == -mLastDirections[k]) {
					// Direction changed
					int extType = (direction > 0 ? 0 : 1); 
					mLastExtremes[extType][k] = mLastValues[k];
					float diff = Math.abs(mLastExtremes[extType][k]
							- mLastExtremes[1 - extType][k]);

					if (diff > SENSITIVITY) {
						boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
						boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
						boolean isNotContra = (mLastMatch != 1 - extType);

						if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough
								&& isNotContra) {
							end = System.currentTimeMillis();
							if (end - start > 500) {
								CURRENT_SETP++;
								mLastMatch = extType;
								start = end;
							}
						} else {
							mLastMatch = -1;
						}
					}
					mLastDiff[k] = diff;
				}
				mLastDirections[k] = direction;
				mLastValues[k] = v;
			}
	}
}
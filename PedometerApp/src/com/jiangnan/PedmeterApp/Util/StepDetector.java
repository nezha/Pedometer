package com.jiangnan.PedmeterApp.Util;

import java.util.ArrayList;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;


public class StepDetector{
	/**
	 * 初始化数据
	 */

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
	
	/**过零检测用的数据**/
	static int cz_wcount = 0;
	static int cz_i = 0;
	static double zero = 9.8;
	static int windowsCz = 50;
	
	/**阈值检测的数据**/
	static int thr_wcount = 0;
	static int thr_i = 0;
	static double threshold = 12.0;
	static int windowsThr = 40;
	
	/**卡尔曼参数**/
	static int klm_count = 0;//用于判断初始化
	static double A=1;      //状态转移系数
	static double H=1;      //量测系数
	static double Q=0.01; 	//激励噪声协方差
	//测量噪声协方差。R设置地越小，滤波结果越接近于原始数据；R越大，正弦特征越明显，但此时的滤波结果与原始数据差距越大
	static double R = 0.1;
	private static ArrayList<Double> x_posteri = new ArrayList<Double>();
	//初始化    
	static double cov_posteri = 1.0; //初始化后验误差方差
	
	
	/**FSM参数**/
	static int fsm_count = 0;
	static double pp_static = 11.5; //三状态中的峰值，经验值
	static double np_static = 9; //谷值，经验值
    static double Thr = 9.8;
    static double Neg_Thr = 9.8;
    static int currentState = 1;
	
	
	public static void accelerationUpdate(SensorEvent event)
	{
		/**先频率融合**/
		double acc = Math.sqrt(Math.pow(event.values[0], 2)+Math.pow(event.values[1], 2)+Math.pow(event.values[2], 2));
		
		/**先滤波，klm滤波**/
		//如果是第一次需要初始化数据
		if(klm_count == 0)
		{
			x_posteri.add(9.8);
		}
        //Predictor equations
        double x_priori =A*x_posteri.get(klm_count);   //向前推算状态变量
        double residual=acc-H*x_priori;           //测量残余 
        double cov_priori=A*A*cov_posteri+Q;//向前推算误差协方差。注意cov_posteri在后面经过了一次计算，也就是说这里的值是上一次(k-1)循环带来的
        //Corrector equations
        double k=H*cov_priori/(H*H*cov_priori+R); 	//计算卡尔曼增益
        cov_posteri=cov_priori*(1-H*k);     //更新误差协方差
        x_posteri.add(x_priori+k*residual);	//更新估计，即滤波结果。这是下面调用的加速度基础
        klm_count++;
        //************************************************//
        
        //////////////////下面三者选其一，其他注释掉///////////////////////////////
        /*
        //过零检测算法
        if(cz_wcount<=0 && (x_posteri.get(cz_i) < zero && x_posteri.get(cz_i+1) >= zero))
        {
            cz_wcount = windowsCz;
            CURRENT_SETP++;
        }else{
        	cz_wcount--;
        }
        cz_i++;
		*/
		/*
        ////////////////阈值检测算法
        if(thr_wcount<=0 && (x_posteri.get(thr_i) < threshold && x_posteri.get(thr_i+1) >= threshold))
        {
            thr_wcount = windowsThr;
            CURRENT_SETP++;
        }else{
        	thr_wcount--;
        }
        thr_i++;
        */
        
        //////////////3-FSM
        double input = x_posteri.get(fsm_count);
        fsm_count++;
        //S1
        if(currentState == 1) //初始状态（静止状态）
        {
            if (input > pp_static)
                currentState = 2;  //转入峰值状态	
        }
        //S2
        if(currentState == 2)
        {
            if(input < np_static)
            {
                currentState = 3; //装入谷值状态
                CURRENT_SETP++;
             }	
        }
        //S3
        if(currentState == 3) //由谷值转入静止状态
        {
            if(input >= Thr)
                currentState = 1;	
        }
        
        
        
        
        
        
        
        
        
        
		/*
		 * 这是谷歌到的算法
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
			*/
	} 
}












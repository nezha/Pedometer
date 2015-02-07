package com.jiangnan.PedmeterApp.Util;

import java.util.ArrayList;

import android.hardware.SensorEvent;
import android.util.Log;


public class Step_Detector {
	/**
	 * @description 主要是进行计步算法的运行
	 */

	public static final String TAG = "STEP_ACC";
	public static final String TAG2 = "STEP_Y";
	
	private static double b[] = {9.58526453e-08,5.75115872e-07,1.43778968e-06,1.91705291e-06,1.43778968e-06,5.75115872e-07,9.58526453e-08};
    private static double a[] = {1.,-5.45387056,12.41648819,-15.10249741,10.34999356,-3.78890908,0.57880143};
    
	private static ArrayList<Double> x = new ArrayList<Double>();
	private static ArrayList<Double> y = new ArrayList<Double>();
	/**
	 * 初始化数据
	 */
	public static int mStep = 0;
    private static int step_i = 0;
    private static int step_j = 0;
    private static int flag_step = 0;
	private static double max_acc = 0;
	
	/**
	 * 
	 * @param e
	 * @return mStep  返回的是步数
	 * 计步算法google的
	 */
	public static void accelerationUpdate(SensorEvent e)
	{
		x.add((double)e.values[2]);
        Log.v(TAG, "z轴结果： " + e.values[2]);
        
        if(step_i < 7)
        {
            y.add(0.0);
            step_i = step_i + 1;
        }
        else
        {
            y.add((b[0]*x.get(step_i) + b[1]*x.get(step_i-1) + b[2]*x.get(step_i-2) + b[3]*x.get(step_i-3) + b[4]*x.get(step_i-4) + b[5]*x.get(step_i-5) + b[6]*x.get(step_i-6) - a[1]*y.get(step_i-1) - a[2]*y.get(step_i-2) - a[3]*y.get(step_i-3) - a[4]*y.get(step_i-4) - a[5]*y.get(step_i-5) - a[6]*y.get(step_i-6)) / a[0]);
            Log.v(TAG2, "y结果： " + y.get(step_i));
            step_i = step_i + 1;
        }
        /** 过零阈值检测**/
        if(step_j > 0)
        {
            if ((y.get(step_j) > 0) && (y.get(step_j-1) < 0))
				flag_step = flag_step + 1;
			else if((y.get(step_j) < 0) && (y.get(step_j-1) <0))
				flag_step = 0;
			if ((flag_step > 0 ) && (y.get(step_j) >= y.get(step_j-1)))
				max_acc = y.get(step_j);	
			if ((flag_step >= 1 ) && (max_acc > 1.0))
			{
				mStep = mStep + 1;
				max_acc = 0;
				flag_step = 0;
			}
        }
        step_j = step_j + 1;
	}
	/**
	 * 数据重置,重新开始计步
	 */
	public void resetData()
	{
		mStep = 0;	
		x.clear();
		y.clear();
		step_i = 0;
	    step_j = 0;
	    flag_step = 0;
		max_acc = 0;
	}
	/**
	 * 
	 * @return mStep  返回步数
	 */
	
	public static int getStep()
	{
		return mStep;
	}
	
	
}

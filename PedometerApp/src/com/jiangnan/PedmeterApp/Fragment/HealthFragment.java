package com.jiangnan.PedmeterApp.Fragment;

import java.util.Timer;
import java.util.TimerTask;

import com.jiangnan.PedmeterApp.R;
import com.jiangnan.PedmeterApp.Util.StepDetector;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HealthFragment extends Fragment {
	TextView healthShow;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 222)
			{
				Double calories = 60 * StepDetector.CURRENT_SETP*0.5;
				healthShow.setText("热量消耗是:"+calories+" 卡路里");
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.health_fragment, container,
				false);
		healthShow = (TextView) rootView.findViewById(R.id.health_show);
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(222);
			}
		}, 0, 500);
		return rootView;
	}

}

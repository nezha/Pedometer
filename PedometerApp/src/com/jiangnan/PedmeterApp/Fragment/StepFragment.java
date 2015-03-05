package com.jiangnan.PedmeterApp.Fragment;

import java.util.Timer;
import java.util.TimerTask;

import com.jiangnan.PedmeterApp.R;
import com.jiangnan.PedmeterApp.Util.StepDetector;

import android.app.Activity;
import android.app.Fragment;
import android.app.TaskStackBuilder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;
import android.widget.Toast;

public class StepFragment extends Fragment {
	TextView stepShow;
	Timer timer;
	TimerTask task;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 123) {
				stepShow.setText("¹²¼Æ:"+StepDetector.CURRENT_SETP+"²½");
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.step_fragment, container,
				false);
		stepShow = (TextView) rootView.findViewById(R.id.step_show);
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// stepShow.setText(StepDetector.CURRENT_SETP);
				mHandler.sendEmptyMessage(123);
			}
		};
		timer.schedule(task, 0, 200);

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

	}

}
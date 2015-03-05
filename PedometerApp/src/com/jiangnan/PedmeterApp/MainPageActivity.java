package com.jiangnan.PedmeterApp;

import com.jiangnan.PedmeterApp.Fragment.HealthFragment;
import com.jiangnan.PedmeterApp.Fragment.StepFragment;
import com.jiangnan.PedmeterApp.Service.Step_Service;
import com.jiangnan.PedmeterApp.Util.StepDetector;

import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainPageActivity extends Activity implements OnClickListener {
	private static int if_start = 0; // 判断暂时的运行状态 奇数代表暂停状态 偶数代表运行状态
	/*
	 * 获取展示计步和分析的Fragment
	 */
	private StepFragment stepFragment;

	private HealthFragment healthFragment;

	/*
	 * 获取Tab的view界面
	 */
	private View stepLayout;

	private View healthLayout;

	/*
	 * 获取activity_main_page界面控件
	 */
	private ImageView step_image;

	private ImageView health_image;

	private TextView step_text;

	private TextView health_text;

	/*
	 * 用于对Fragment进行管理
	 */

	private FragmentManager fragmentManage;

	Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_page);
		// 初始化布局
		initView();
		fragmentManage = getFragmentManager();
		// 第一次启动选中第0个Tab
		setTabSelection(0);
		// 默认初始为运行状态
		serviceIntent = new Intent(this, Step_Service.class);
		startService(serviceIntent);

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(getApplicationContext(), "退出程序", Toast.LENGTH_SHORT).show();
		finish();
	}

	private void initView() {
		stepLayout = findViewById(R.id.step_Message);
		healthLayout = findViewById(R.id.Health_Message);
		step_image = (ImageView) findViewById(R.id.step_image);
		health_image = (ImageView) findViewById(R.id.health_image);
		step_text = (TextView) findViewById(R.id.step_text);
		health_text = (TextView) findViewById(R.id.health_text);

		stepLayout.setOnClickListener(this);
		healthLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.step_Message:
			setTabSelection(0);
			break;
		case R.id.Health_Message:
			setTabSelection(1);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据传入的index参数来设置选中的Tab
	 * 
	 * @param index
	 *            0 代表查看计步数 1代表查看卡路里数
	 */
	private void setTabSelection(int index) {
		// TODO Auto-generated method stub
		// 每次选中之前清除上次选中信息
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction fragmentTransaction = fragmentManage
				.beginTransaction();
		// 隐藏Fragment防止多个fragment显示
		hideFragment(fragmentTransaction);

		switch (index) {
		case 0:
			step_image.setImageResource(R.drawable.main_pedometer_selected);
			step_text.setTextColor(Color.BLUE);
			if (stepFragment == null) {
				stepFragment = new StepFragment();
				fragmentTransaction.add(R.id.infoShow, stepFragment);
			} else
				fragmentTransaction.show(stepFragment);
			break;
		case 1:
			health_image.setImageResource(R.drawable.main_analysis_selected);
			health_text.setTextColor(Color.BLUE);
			if (healthFragment == null) {
				healthFragment = new HealthFragment();
				fragmentTransaction.add(R.id.infoShow, healthFragment);
			} else
				fragmentTransaction.show(healthFragment);

			break;
		default:
			break;
		}

		fragmentTransaction.commit();
	}

	private void hideFragment(FragmentTransaction fragmentTransaction) {
		// TODO Auto-generated method stub
		if (stepFragment != null)
			fragmentTransaction.hide(stepFragment);
		if (healthFragment != null)
			fragmentTransaction.hide(healthFragment);
	}

	/**
	 * 清除所有选中的状态
	 */
	private void clearSelection() {
		// TODO Auto-generated method stub
		step_image.setImageResource(R.drawable.main_pedometer);
		step_text.setTextColor(Color.parseColor("#82858b"));
		health_image.setImageResource(R.drawable.main_analysis);
		health_text.setTextColor(Color.parseColor("#82858b"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_page, menu);
		return true;
	}

	int temp_step;

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.startPause:
			if_start++;
			if (if_start % 2 == 1) // 奇书为暂停状态
			{
				item.setTitle("start");
				temp_step = StepDetector.CURRENT_SETP;
				stopService(serviceIntent);
			} else // 偶数为运行状态
			{
				item.setTitle("Pause");
				startService(serviceIntent);
				StepDetector.CURRENT_SETP = temp_step;
			}
			return true;
		case R.id.clear:
			StepDetector.CURRENT_SETP = 0;
			Toast.makeText(this, "重置", Toast.LENGTH_LONG).show();
			return true;
		case R.id.settings:
			Toast.makeText(this, "设置", Toast.LENGTH_LONG).show();
			return true;
		case R.id.quit:
			stopService(serviceIntent);
			Toast.makeText(getApplicationContext(), "--Service 关闭--", Toast.LENGTH_SHORT).show();
			onDestroy();
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}

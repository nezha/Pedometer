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
	private static int if_start = 0; // �ж���ʱ������״̬ ����������ͣ״̬ ż����������״̬
	/*
	 * ��ȡչʾ�Ʋ��ͷ�����Fragment
	 */
	private StepFragment stepFragment;

	private HealthFragment healthFragment;

	/*
	 * ��ȡTab��view����
	 */
	private View stepLayout;

	private View healthLayout;

	/*
	 * ��ȡactivity_main_page����ؼ�
	 */
	private ImageView step_image;

	private ImageView health_image;

	private TextView step_text;

	private TextView health_text;

	/*
	 * ���ڶ�Fragment���й���
	 */

	private FragmentManager fragmentManage;

	Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main_page);
		// ��ʼ������
		initView();
		fragmentManage = getFragmentManager();
		// ��һ������ѡ�е�0��Tab
		setTabSelection(0);
		// Ĭ�ϳ�ʼΪ����״̬
		serviceIntent = new Intent(this, Step_Service.class);
		startService(serviceIntent);

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(getApplicationContext(), "�˳�����", Toast.LENGTH_SHORT).show();
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
	 * ���ݴ����index����������ѡ�е�Tab
	 * 
	 * @param index
	 *            0 ����鿴�Ʋ��� 1����鿴��·����
	 */
	private void setTabSelection(int index) {
		// TODO Auto-generated method stub
		// ÿ��ѡ��֮ǰ����ϴ�ѡ����Ϣ
		clearSelection();
		// ����һ��Fragment����
		FragmentTransaction fragmentTransaction = fragmentManage
				.beginTransaction();
		// ����Fragment��ֹ���fragment��ʾ
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
	 * �������ѡ�е�״̬
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
			if (if_start % 2 == 1) // ����Ϊ��ͣ״̬
			{
				item.setTitle("start");
				temp_step = StepDetector.CURRENT_SETP;
				stopService(serviceIntent);
			} else // ż��Ϊ����״̬
			{
				item.setTitle("Pause");
				startService(serviceIntent);
				StepDetector.CURRENT_SETP = temp_step;
			}
			return true;
		case R.id.clear:
			StepDetector.CURRENT_SETP = 0;
			Toast.makeText(this, "����", Toast.LENGTH_LONG).show();
			return true;
		case R.id.settings:
			Toast.makeText(this, "����", Toast.LENGTH_LONG).show();
			return true;
		case R.id.quit:
			stopService(serviceIntent);
			Toast.makeText(getApplicationContext(), "--Service �ر�--", Toast.LENGTH_SHORT).show();
			onDestroy();
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}

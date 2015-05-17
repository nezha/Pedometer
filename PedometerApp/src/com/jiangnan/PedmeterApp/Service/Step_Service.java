package com.jiangnan.PedmeterApp.Service;

import com.jiangnan.PedmeterApp.R;
import com.jiangnan.PedmeterApp.Util.SensorUtil;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;


/**
 * 
 * @author zhangyi 1061243497@qq.com
 * @since 2015/2/8
 * @description 主要是用service把计步程序放在服务中运行
 *
 */
public class Step_Service extends Service implements SensorEventListener{
	SensorUtil SU;
	
	private PowerManager.WakeLock wakeLock;
    private NotificationManager mNM;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(this, "--service started--", Toast.LENGTH_LONG).show();
		
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //showNotification();
        
        acquireWakeLock();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        
		SU = new SensorUtil(this);
		SU.registerListener();
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		SU.unregisterListener();
		
		mNM.cancel(R.string.app_name);
        wakeLock.release();
		super.onDestroy();
		
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(final SensorEvent e) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SU.routeEvent(e);
			}
		}).start();
		
	}

	/**
     * Show a notification while this service is running.
     */
	/*
    private void showNotification() {
        CharSequence text = getText(R.string.app_name);
        Notification notification = new Notification(R.drawable.ic_notification, null,
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        Intent pedometerIntent = new Intent();
        pedometerIntent.setComponent(new ComponentName(this, MainPageActivity.class));
        pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                pedometerIntent, 0);
        notification.setLatestEventInfo(this, text,
                getText(R.string.notification_subtitle), contentIntent);

        mNM.notify(R.string.app_name, notification);
    }
    */	
	
    // BroadcastReceiver for handling ACTION_SCREEN_OFF.
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Check action just to be on the safe side.
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Unregisters the listener and registers it again.
                SU.unregisterListener();
                SU.registerListener();
                    wakeLock.release();
                    acquireWakeLock();
            }
        }
    };
    
    private void acquireWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int wakeFlags;
        wakeFlags = PowerManager.PARTIAL_WAKE_LOCK;
        wakeLock = pm.newWakeLock(wakeFlags, "lock");
        wakeLock.acquire();
    }
	

}

package com.example.androdimage;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	public MyService() {
	}

	static final int MSG_REGISTER_CLIENT = 1;
	Messenger client = null;
	private Thread thr = null;
	private boolean threadFlag = true;

	@Override
	public void onCreate() {

		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.
		thr = new Thread(null, mTask, "MyService");
		thr.start();
		Toast.makeText(this, "service start", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		// Cancel the notification -- we use the same ID that we had used to
		// start it

		// Tell the user we stopped.
		Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
		if (thr != null) {
			threadFlag = false;
		}
	}

	/**
	 * The function that runs in our worker thread
	 */
	Runnable mTask = new Runnable() {
		public void run() {
			while (threadFlag) {
				synchronized (mMessenger.getBinder()) {
					try {
						mMessenger.getBinder().wait(10000);
					} catch (Exception e) {
					}
				}
				if (client != null) {
					try {
						client.send(Message.obtain(null, MSG_REGISTER_CLIENT,
								"msg:" + getLocation()));
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Log.i("mTask", "mtask run");
				// Toast.makeText(MyService.this, "service destroy",
				// Toast.LENGTH_SHORT).show();
			}
		}
	};

	public String getLocation() {
		String loc = null;
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// 返回所有已知的位置提供者的名称列表，包括未获准访问或调用活动目前已停用的。
		List<String> lp = lm.getAllProviders();
		for (String item : lp) {
			Log.i("8023", "可用位置服务：" + item);
		}

		String providerName = LocationManager.NETWORK_PROVIDER;
		if (providerName != null) {
			Log.i("8023", "位置服务：" + providerName);
			int i = 0;
			Location location = null;
			while (i++ < 1) {
				location = lm.getLastKnownLocation(providerName);
				//Log.i("8023", "-------" + location);
				if (location != null) {
					// 获取维度信息
					double latitude = location.getLatitude();// 经度
					// 获取经度信息
					double longitude = location.getLongitude();// 维度
					double altitude = location.getAltitude(); // 海拔

					loc = "定位方式： " + providerName + "  维度：" + latitude
							+ "  经度：" + longitude + "  海拔 ：" + altitude;
					Log.i("8023", "-------" + loc);
					break;
				}
			}

		} else {
			loc = "1.请检查网络连接   2.请打开我的位置";
		}
		return loc;
	}

	/**
	 * Handler of incoming messages from clients.
	 */
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				client = msg.replyTo;
				break;

			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

}

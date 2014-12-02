package com.example.androdimage;



import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class MyRemoteService extends Service {
	public MyRemoteService() {
	}
	
	  
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mHandler.sendEmptyMessage(MSG_CODE);
		Toast.makeText(this, "service start", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mCallbacks.kill();
		mHandler.removeMessages(MSG_CODE);
		Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
	}


	final RemoteCallbackList<IRemoteServiceCallback> mCallbacks
	            = new RemoteCallbackList<IRemoteServiceCallback>();
	int mValue = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return mBinder;
	}
	
	private final IRemoteService.Stub mBinder = new  IRemoteService.Stub() {
		
		@Override
		public void unregisterCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(cb != null) mCallbacks.unregister(cb);
			
		}
		
		@Override
		public void registerCallback(IRemoteServiceCallback cb)
				throws RemoteException {
			// TODO Auto-generated method stub
			if(cb != null) mCallbacks.register(cb);
			
		}

	};
	
	public static final int MSG_CODE = 1;
	private final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			  case MSG_CODE:
				  int value = ++mValue;
				  final int N = mCallbacks.beginBroadcast();
				  //Log.i("mCallbacks.beginBroadcast()",N+"");
				  for(int i = 0;i < N;i++){
					  try {
						mCallbacks.getBroadcastItem(i).valueChanged(mValue);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				  }
				  mCallbacks.finishBroadcast();
				  sendMessageDelayed(obtainMessage(MSG_CODE), 1*1000);
				  break;
			  default:
				  super.handleMessage(msg);
			}
			
		}
		
	};

}

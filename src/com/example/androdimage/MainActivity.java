package com.example.androdimage;




import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
@SuppressLint("ValidFragment")
public class MainActivity extends Activity {
	TextView tv;
	   static final private int GET_CODE = 0;
	   
	  private final ServiceConnection mConn= new MyServiceConnection();
	  
	  Messenger mService = null;
	  
	 
      class IncomingHandler extends Handler {
          @Override
          public void handleMessage(Message msg) {
              switch (msg.what) {
                  case MyService.MSG_REGISTER_CLIENT:
                	 
                      tv.setText("Received from service: " + msg.obj);
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv = (TextView)this.findViewById(R.id.textView1);
		tv.setText("welcome hello world!");
		final Button b1 = (Button)this.findViewById(R.id.button1);
		final Button b2 = (Button)this.findViewById(R.id.button2);
		final Button b3 = (Button)this.findViewById(R.id.button3);
		final Button b4 = (Button)this.findViewById(R.id.button4);
		final Button b5 = (Button)this.findViewById(R.id.button5);
		final Button b6 = (Button)this.findViewById(R.id.button6);
		final Button b7 = (Button)this.findViewById(R.id.button7);
		final Button b8 = (Button)this.findViewById(R.id.button8);
		final Button b9 = (Button)this.findViewById(R.id.button9);

		Resources res = this.getBaseContext().getResources();
		final Drawable red= res.getDrawable(R.drawable.red);
		final Drawable blue= res.getDrawable(R.drawable.blue);
		final Drawable yellow= res.getDrawable(R.drawable.yellow);
		b1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, SendActivity.class);
	            startActivityForResult(intent, GET_CODE);
			}
		  
		});
		b2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = "You have clicked " + b2.getText().toString();
				tv.setText(str);
				if(tv.getBackground() != blue)
				   tv.setBackground(blue);
			}
			
		});
		b3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str = "You have clicked " + b3.getText().toString();
				tv.setText(str);
				if(tv.getBackground() != yellow)
				   tv.setBackground(yellow);
			}
			
		});
		
		b4.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startService(new Intent(MainActivity.this,MyService.class));
			}
			
		});
		b5.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopService(new Intent(MainActivity.this,MyService.class));
			}
			
		});
		b6.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bindService(new Intent(MainActivity.this,MyService.class), mConn, BIND_AUTO_CREATE);
			}
			
		});
		b7.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				unbindService(mConn);
			}
			
		});
		
		
		b8.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				bindService(new Intent(MainActivity.this,MyRemoteService.class), mRemoteConn, BIND_AUTO_CREATE);
			}
			
		});
		b9.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				unbindService(mRemoteConn);
			}
			
		});
		 final ActionBar bar = getActionBar();
	        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

	        bar.addTab(bar.newTab()
	                .setText("Menus").setTabListener(new TabListener(new TabContentFragment("Menus"))));
	        bar.addTab(bar.newTab()
	                .setText("Args").setTabListener(new TabListener(new TabContentFragment("Args"))));
	                
	        bar.addTab(bar.newTab()
	                .setText("Stack").setTabListener(new TabListener(new TabContentFragment("Stack"))));     
	        bar.addTab(bar.newTab()
	                .setText("Tabs").setTabListener(new TabListener(new TabContentFragment("Tabs"))));

	        if (savedInstanceState != null) {
	            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
	        }
	    
		
	}
	
	public boolean OnKeyDown(int keyCode,KeyEvent event){
		CharSequence c = "you have pressed" ;
		c = c + " a key";
		tv.setText(c);
		return super.onKeyDown(keyCode, event);
	}
	
	public boolean OnKeyUp(int keyCode,KeyEvent event){
		CharSequence c = "change you color here" ;
		tv.setText(c);
		tv.setBackgroundColor(Color.WHITE);
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		  menu.add("Normal item").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		  MenuItem actionItem = menu.add("Action Button");
		  actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		  actionItem.setIcon(android.R.drawable.ic_menu_share);
		return true;
	}
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	      Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
	      return true;
	 }
	 
	 @Override
		protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		 
	 }
	 
	 private final class MyServiceConnection  implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			 mService = new Messenger(service);
			 try {
                 Message msg = Message.obtain(null,
                         MyService.MSG_REGISTER_CLIENT);
                 msg.replyTo = mMessenger;
                 mService.send(msg);
                 
                 // Give it some value as an example.
        
             } catch (RemoteException e) {
                 // In this case the service has crashed before we could even
                 // do anything with it; we can count on soon being
                 // disconnected (and then reconnected if it can be restarted)
                 // so there is no need to do anything here.
             }
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			
		}
		 
		 
	 }
	 
	 private final Handler mHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			  case MyRemoteService.MSG_CODE:
				  tv.setText("Received from service: " + msg.obj);
				  break;
			  default:
				  super.handleMessage(msg);
			}
		}
		 
	 };
	 private final IRemoteServiceCallback mCallBack = new IRemoteServiceCallback.Stub() {
		
		@Override
		public void valueChanged(int value) throws RemoteException {
			// TODO Auto-generated method stub
			mHander.sendMessage(Message.obtain(null,MyRemoteService.MSG_CODE,value));
			
			
		}
	};
	 private final ServiceConnection mRemoteConn = new  ServiceConnection(){

		 IRemoteService mService = null;
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			
		    mService = IRemoteService.Stub.asInterface(service);
			try {
				mService.registerCallback(mCallBack);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			mService = null;
			
		}
		 
	 };
	 
	 private class TabListener implements ActionBar.TabListener {
	        private TabContentFragment mFragment;

	        public TabListener(TabContentFragment fragment) {
	            mFragment = fragment;
	        }

	        public void onTabSelected(Tab tab, FragmentTransaction ft) {
	            ft.add(R.id.fragment_content, mFragment, mFragment.getText());
	        }

	        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	            ft.remove(mFragment);
	        }

	        public void onTabReselected(Tab tab, FragmentTransaction ft) {
	            Toast.makeText(MainActivity.this, "Reselected!", Toast.LENGTH_SHORT).show();
	        }

	    }
	 
	 private class TabContentFragment extends Fragment {
	        private String mText;

	        public TabContentFragment(String text) {
	            mText = text;
	        }

	        public String getText() {
	            return mText;
	        }

	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	            View fragView = inflater.inflate(R.layout.action_bar_tab_content, container, false);

	            TextView text = (TextView) fragView.findViewById(R.id.text);
	            text.setText(mText);

	            return fragView;
	        }

	    }

}

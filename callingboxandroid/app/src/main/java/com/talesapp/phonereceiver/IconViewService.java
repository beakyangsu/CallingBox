package com.talesapp.phonereceiver;

import com.talesapp.phonereceiver.common.Tools;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class IconViewService extends Service {
	
	public final static String ACTION_INCOMING = "incoming";
	public final static String ACTION_OUTGOING = "outgoing";
	public final static String ACTION_STOP = "stop";
	
	private FrameLayout mRootLayout;
	
	private WindowManager mWindowManager;
	private LayoutInflater mLayoutInflater;
	
	private FrameLayout mIconLayoutLyt;
	private ImageView mIconImgV;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		setTheme(0);
		
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mRootLayout = new FrameLayout(this);
		mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootLayout.setVisibility(View.GONE);
		mWindowManager.addView(mRootLayout, getLayoutParams());
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		if(intent != null){
			String action = intent.getAction();
			if(action.equals(ACTION_INCOMING)){
				setupWidget();
				
				Intent i = new Intent(this, ViewCtrlService.class);
	        	i.setAction(ViewCtrlService.ACTION_INCOMING);
	        	i.putExtras(intent);
	            
	            startService(i);
			}else if(action.equals(ACTION_OUTGOING)){
				setupWidget();
				
				Intent i = new Intent(this, ViewCtrlService.class);
	        	i.setAction(ViewCtrlService.ACTION_OUTGOING);
	            i.putExtras(intent);
	            startService(i);
			}else if(action.equals(ACTION_STOP)){
				hideIcon();
			}
		}
		return START_NOT_STICKY;
	}
	
	private void hideIcon(){
		mRootLayout.setVisibility(View.GONE);
		stopSelf();
	}
	
	private void setupWidget(){
		
		View parentView = mLayoutInflater.inflate(R.layout.show_icon, mRootLayout, true);
		mIconLayoutLyt = (FrameLayout) parentView.findViewById(R.id.lyt_icon_layout);
		mIconImgV = (ImageView) parentView.findViewById(R.id.imgv_icon_image);
		
		int width = Tools.getScreenWidth(mWindowManager);
		int height = Tools.getScreenHeight(mWindowManager);
		int layoutSize;
		if(width <= height){
			layoutSize = width;
		}else{
			layoutSize = height;
		}
		
		mIconLayoutLyt.getLayoutParams().width = layoutSize;
		mIconLayoutLyt.getLayoutParams().height = layoutSize;
		
		
		mIconImgV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideIcon();
			}
		});
		
		mRootLayout.setVisibility(View.VISIBLE);
		
	}
	
	private WindowManager.LayoutParams getLayoutParams(){
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.FILL_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT, 
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.TOP;
		return params;
	}
	
	
	@Override
	public void onDestroy() {
		mLayoutInflater = null;
		mRootLayout = null;
		mWindowManager = null;
		
		super.onDestroy();
	}
}
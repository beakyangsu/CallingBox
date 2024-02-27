package com.talesapp.phonereceiver;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class SettingUiAct extends Activity{

	private View mTabbarAbout;
	public ImageView mCheckKakaProfileImgV, mCheckCustomProfileImgV;
	public Button mNextBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_setting_ui);
		
		setupWidget();
	}
	
	@Override
	protected void onResume() {
		this.overridePendingTransition(0, 0);
		super.onResume();
	}

	private void setupWidget(){
		SettingUIActClickListener listener = new SettingUIActClickListener(this, this);
		
		mTabbarAbout = (View) findViewById(R.id.view_top_tabbar_about);
		mCheckKakaProfileImgV = (ImageView) findViewById(R.id.imgv_checkbox_kakao_profile);
		mCheckCustomProfileImgV = (ImageView) findViewById(R.id.imgv_checkbox_custom_profile);
		mNextBtn = (Button) findViewById(R.id.btn_next);
		
		mCheckKakaProfileImgV.setOnClickListener(listener);
		mCheckCustomProfileImgV.setOnClickListener(listener);
		mNextBtn.setOnClickListener(listener);
		mTabbarAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mNextBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action == MotionEvent.ACTION_UP){
					mNextBtn.setBackgroundResource(R.drawable.ring_button_d);
				}else{
					mNextBtn.setBackgroundResource(R.drawable.ring_button_p);
				}
				action = 0;
				return false;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
}

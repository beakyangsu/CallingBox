package com.talesapp.phonereceiver;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class SettingPayAct extends Activity{

	private View mTabbarAbout;
	private ImageView mCheckOneMonthImgV, mCheckTwoMonthsImgV;
	private Button mOkBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_settings_pay);
		
		setupWidget();
	}
	
	private void setupWidget(){
		mTabbarAbout = (View) findViewById(R.id.view_top_tabbar_about);
		mCheckOneMonthImgV = (ImageView) findViewById(R.id.imgv_checkbox_use_one_month);
		mCheckTwoMonthsImgV = (ImageView) findViewById(R.id.imgv_checkbox_use_two_months);
		mOkBtn = (Button) findViewById(R.id.btn_ok);
		
		mTabbarAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mCheckOneMonthImgV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCheckOneMonthImgV.setImageResource(R.drawable.check_button_1);
				mCheckTwoMonthsImgV.setImageResource(R.drawable.check_button_2);
			}
		});
		
		mCheckTwoMonthsImgV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCheckOneMonthImgV.setImageResource(R.drawable.check_button_2);
				mCheckTwoMonthsImgV.setImageResource(R.drawable.check_button_1);
			}
		});
		
		mOkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mOkBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action == MotionEvent.ACTION_UP){
					mOkBtn.setBackgroundResource(R.drawable.ring_button_d);
				}else{
					mOkBtn.setBackgroundResource(R.drawable.ring_button_p);
				}
				action = 0;
				return false;
			}
		});
	}

	@Override
	protected void onResume() {
		this.overridePendingTransition(0, 0);
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}

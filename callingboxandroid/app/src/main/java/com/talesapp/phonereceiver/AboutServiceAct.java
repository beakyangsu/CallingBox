package com.talesapp.phonereceiver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;

public class AboutServiceAct extends Activity{

	private Button mNextBtn;
	private TextView mAboutTxtV;
	private View mSettingsView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_about_service);
		
		setupWidget();
	}
	
	@Override
	protected void onResume() {
		this.overridePendingTransition(0, 0);
		super.onResume();
	}

	private void setupWidget(){
		
		mNextBtn = (Button) findViewById(R.id.btn_next);
		mAboutTxtV = (TextView) findViewById(R.id.txtv_abt_visual_lettering);
		mSettingsView = (View) findViewById(R.id.view_top_tabbar_settings);
		
		mNextBtn.setText("다 음");
		mNextBtn.setPaintFlags(mNextBtn.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		mNextBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_UP){
					mNextBtn.setBackgroundResource(R.drawable.ring_button_d);
				}else{
					mNextBtn.setBackgroundResource(R.drawable.ring_button_p);
				}
				return false;
			}
		});
		mNextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		/** 
		 * 1. 설정페이지가 한 개 이상 떠있을 경우 현재 페이지를 맨 밑으로 보내고 설정의 마지막 페이지를 호출한다.
		 * 2. 그렇지 않을 경우 설정 UI 페이지를 호출한다.
		 * */
		mSettingsView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(AboutServiceAct.this, SettingUiAct.class));
			}
		});
//		String string = "<font color=\"#624434\">는 고객들이 전화<br>" + 
//				"통화시&nbsp;&nbsp;자신이&nbsp;&nbsp;설정해&nbsp;&nbsp;놓은&nbsp;&nbsp;이미지가<br>" + 
//				"상대방에게 보여지는 통화 부가 서비스<br>" + 
//				"입니다.</font>";
		String text = "는 고객들이 전화 통화시 자신이 설정해 놓은 이미지가 상대방에게 보여지는 통화 부가 서비스 입니다.";
		mAboutTxtV.setText(
				Html.fromHtml("<font color=\"#242221\">비쥬얼 레터링 서비스</font>" + text));
		
		text = null;
	}
	
	@Override
	protected void onDestroy(){
		mNextBtn = null;
		mAboutTxtV = null;
		super.onDestroy();
	}
}

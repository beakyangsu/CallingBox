package com.talesapp.phonereceiver;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class SettingUIActClickListener implements OnClickListener{

	SettingUiAct mSettingUiAct;
	Context mContext;
	
	public SettingUIActClickListener(Context context, SettingUiAct settingUiAct) {
		mSettingUiAct = settingUiAct;
		mContext = context;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imgv_checkbox_kakao_profile:
			((ImageView)v).setImageResource(R.drawable.check_button_1);
			mSettingUiAct.mCheckCustomProfileImgV.setImageResource(R.drawable.check_button_2);
			break;
		case R.id.imgv_checkbox_custom_profile:
			mSettingUiAct.mCheckKakaProfileImgV.setImageResource(R.drawable.check_button_2);
			((ImageView)v).setImageResource(R.drawable.check_button_1);
			break;
		case R.id.btn_next:
			((Activity)mContext).startActivity(new Intent(mContext, SetInfoAct.class));
			((Activity)mContext).finish();
		}
	}

}

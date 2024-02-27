package com.talesapp.phonereceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class OutgoingCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();

		if(null == bundle)
		return;

		String phonenumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		final Editor et=settings.edit();
		et.putString("outgoing_phone", phonenumber);
		et.commit();

//		String info = "Detect Calls sample application\nOutgoing number: " + phonenumber;
	}
}

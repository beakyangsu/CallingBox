package com.talesapp.phonereceiver.receivers;

import com.talesapp.phonereceiver.IconViewService;
import com.talesapp.phonereceiver.ViewCtrlService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.d("IncomingCallReceiver", "onReceive: " + state);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
//        	try {
//        		Thread.sleep(1000);
//        	} catch (InterruptedException e) {
//        		// TODO Auto-generated catch block
//        		e.printStackTrace();
//        	}
            
        	Intent i = new Intent(context, IconViewService.class);
        	i.setAction(IconViewService.ACTION_INCOMING);
        	i.putExtras(intent);
        	context.startService(i);
        }
        else if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
        	System.out.println("OFF HOOK!!");
//        	try {
//        		Thread.sleep(1000);
//        	} catch (InterruptedException e) {
//        		// TODO Auto-generated catch block
//        		e.printStackTrace();
//        	}
            Intent i = new Intent(context, IconViewService.class);
            i.setAction(IconViewService.ACTION_OUTGOING);

            context.startService(i);
        	
        }else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
        	Intent i = new Intent(context, ViewCtrlService.class);
        	i.setAction(ViewCtrlService.ACTION_DROP_CALL);
        	System.out.println("==============start drop call service");
        	context.startService(i);
//        	if(StaticValue.sIncomingAct != null) StaticValue.sIncomingAct.finish();
//        	if(StaticValue.sOutgoingAct != null) StaticValue.sOutgoingAct.finish();
        }

	}

}


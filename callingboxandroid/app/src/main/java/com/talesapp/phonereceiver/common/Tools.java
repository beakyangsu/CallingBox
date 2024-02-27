package com.talesapp.phonereceiver.common;

import java.text.DecimalFormat;

import com.talesapp.phonereceiver.ViewCtrlService;

import android.content.Context;
import android.os.Debug;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class Tools {
	public static int getScreenWidth(WindowManager windowManager){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}
	
	public static int getScreenHeight(WindowManager windowManager){
		DisplayMetrics displaymetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.heightPixels;
	}
	
	public static String getThisPhoneNum(Context context){
		TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return manager.getLine1Number();
	}
	
	public static boolean isSdCardMounted(){
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED)) return true;
		
		return false;
	}
	
	public static void showAllocatedMemory(){
		double maxMemory = Runtime.getRuntime().maxMemory() / ( 1024.0f ) / 1000;
		double allocatedMemory = Debug.getNativeHeapAllocatedSize() / (1024.0f) / 1000;
		DecimalFormat format = new DecimalFormat("#.#");
		String cookedMaxMemory = format.format(maxMemory);
		String cookedallocatedMemory = format.format(allocatedMemory);
		String text = "Max: " + cookedMaxMemory + "MB" +
				"\nAllocated: " + cookedallocatedMemory + "MB";
		
		if(ViewCtrlService.sMemoryCheckTxtv != null){
			ViewCtrlService.sMemoryCheckTxtv.setText(text);
		}
	}
}

package com.talesapp.phonereceiver.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UserInfoStore {
	
	private static String NAME = "user_info";
	
	private static void write(Context context, String key, String value){
		SharedPreferences prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	private static void remove(Context context, String key){
		SharedPreferences prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.remove(key);
		editor.commit();
	}
	
	private static String read(Context activity, String key, String defaultValue){
		SharedPreferences prefs = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE);
		return prefs.getString(key, defaultValue);
	}
	
	public static class Name{
		private final static String KEY = "name";
		public final static String DEFAULT = "Put your name...";
		
		public static void write(Context activity, String value){
			UserInfoStore.write(activity, KEY, value);
		}
		public static void remove(Context context){
			UserInfoStore.remove(context, KEY);
		}
		public static String read(Context context){
			return UserInfoStore.read(context, KEY, DEFAULT);
		}
	}
	
	public static class Desc{
		private final static String KEY = "desc";
		public final static String DEFAULT = "Note about you~!";
		
		public static void write(Context context, String value){
			UserInfoStore.write(context, KEY, value);
		}
		public static void remove(Context context){
			UserInfoStore.remove(context, KEY);
		}
		public static String read(Context context){
			return UserInfoStore.read(context, KEY, DEFAULT);
		}
	}
}
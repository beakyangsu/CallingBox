package com.talesapp.phonereceiver.common;

import java.util.ArrayList;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactTableDB extends SQLiteOpenHelper{
	
	private final static String DB_NAME = "visual_lettering.db";
	private final String TABLE_NAME = "contact";
	private final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
	private final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME +"(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"phone TEXT, " +
			"name TEXT, " +
			"profile_pic TEXT, " +
			"bg_img TEXT, " +
			"desc TEXT, " +
			"url TEXT );";
	
	private final String[] COLUMN = new String[]{"_id", "phone", "name", "profile_pic", "bg_img", "desc" , "url"};
	
	public ContactTableDB(Context context){
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}
	
	public Contacts query(String phoneNum){
		String selection = COLUMN[1] + " = '" + phoneNum + "'";
		Cursor cursor = queryPrivate(selection);
    	
    	Contacts contacts = null;
    	
    	if(cursor.moveToFirst()){
    		contacts = new Contacts();
    		
    		contacts.setPhone(cursor.getString(1));
    		contacts.setName((cursor.getString(2)));
    		contacts.setProfilePicPath(cursor.getString(3));
    		contacts.setBgImgPath(cursor.getString(4));
    		contacts.setDesc(cursor.getString(5));
    		contacts.setUrl(cursor.getString(6));
    	}
    	cursor.close();
    	return contacts;
	}
	
	public ArrayList<Contacts> query(){
		ArrayList<Contacts> contactsList = new ArrayList<Contacts>();
		Cursor cursor = queryPrivate(null);
		
		Contacts contacts;
		
		while(cursor.moveToNext()){
			contacts = new Contacts();
			
			contacts.setPhone(cursor.getString(1));
    		contacts.setName((cursor.getString(2)));
    		contacts.setProfilePicPath(cursor.getString(3));
    		contacts.setBgImgPath(cursor.getString(4));
    		contacts.setDesc(cursor.getString(5));
    		contacts.setUrl(cursor.getString(6));
    		
    		contactsList.add(contacts);
    		
    		contacts = null;
		}
		
		return contactsList;
	}
	
	
	private Cursor queryPrivate(String selection){
		SQLiteDatabase db = getReadableDatabase();
		return db.query(TABLE_NAME, COLUMN, selection, null, null, null, null);
	}
	
	public void insert(ArrayList<Contacts> contactsList){
		
		SQLiteDatabase db = getWritableDatabase();
		int size = contactsList.size();
		
		for(int i = 0; i < size; i++){
			
			ContentValues row = new ContentValues();
			
			row.put(COLUMN[1], contactsList.get(i).getPhone());
			row.put(COLUMN[2], contactsList.get(i).getName());
			row.put(COLUMN[3], contactsList.get(i).getProfilePicPath());
			row.put(COLUMN[4], contactsList.get(i).getBgImgPath());
			row.put(COLUMN[5], contactsList.get(i).getDesc());
			row.put(COLUMN[6], contactsList.get(i).getUrl());
			
			db.insert(TABLE_NAME, null, row);
			
			row = null;
		}
	}
	
	public void insert(Contacts contacts){
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues row = new ContentValues();
		
		row.put(COLUMN[1], contacts.getPhone());
		row.put(COLUMN[2], contacts.getName());
		row.put(COLUMN[3], contacts.getProfilePicPath());
		row.put(COLUMN[4], contacts.getBgImgPath());
		row.put(COLUMN[5], contacts.getDesc());
		row.put(COLUMN[6], contacts.getUrl());
		
		db.insert(TABLE_NAME, null, row);
	}
	
	public void update(Contacts contacts){
		SQLiteDatabase db = getWritableDatabase();
		String where;
		
		ContentValues row = new ContentValues();
		
		row.put(COLUMN[2], contacts.getName());
		row.put(COLUMN[3], contacts.getProfilePicPath());
		row.put(COLUMN[4], contacts.getBgImgPath());
		row.put(COLUMN[5], contacts.getDesc());
		row.put(COLUMN[6], contacts.getUrl());
		
		where = COLUMN[0] + " = '" + contacts.getPhone() + "'";
		
		db.update(TABLE_NAME, row, where, null);
	}
	
	public void delete(String phoneNum){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TABLE_NAME, COLUMN[1] + " = '" + phoneNum + "'", null);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		onCreate(db);
	}
}
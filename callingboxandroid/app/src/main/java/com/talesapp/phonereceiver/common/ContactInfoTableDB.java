package com.talesapp.phonereceiver.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactInfoTableDB extends SQLiteOpenHelper{

	private final static String DB_NAME = "kakao_ring.db";
	private final String TABLE_NAME = "contact_info";
	private final String SQL_DROP = "DROP TABLE IF EXISTS contact_info";
	private final String[] COLUMN = new String[]{"idx", "phone", "name", "desc"};
	private final String SQL_CREATE = "CREATE TABLE contact_info(" + 
			"idx INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"phone TEXT, " +
			"name TEXT, " +
			"desc TEXT);";
	
	public ContactInfoTableDB(Context context){
		super(context, DB_NAME, null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	public ContactInfo query(String phone){
		Cursor cursor = query_(phone);
		
		ContactInfo contactInfo = null;
		
		if(cursor.moveToFirst()){
			contactInfo = new ContactInfo();
			contactInfo.setId(cursor.getInt(0));
			contactInfo.setPhone(cursor.getString(1));
			contactInfo.setName(cursor.getString(2));
			contactInfo.setDesc(cursor.getString(3));
		}
		
		cursor.close();
		
		return contactInfo;
	}
	
	private Cursor query_(String phone){
		SQLiteDatabase db = getReadableDatabase();
		String selection = COLUMN[1] + " = '" + phone + "'";
		return db.query(TABLE_NAME, COLUMN, selection, null, null, null, null);
	}
	
	public long insert(ContactInfo contactInfo){
		SQLiteDatabase db = getWritableDatabase();
		
		ContentValues row = new ContentValues();
		row.put(COLUMN[1], contactInfo.getPhone());
		row.put(COLUMN[2], contactInfo.getName());
		row.put(COLUMN[3], contactInfo.getDesc());
		
		return db.insert(TABLE_NAME, null, row);
	}
	
	public int update(ContactInfo contactInfo){
		SQLiteDatabase db = getWritableDatabase();
		String where = COLUMN[1] + " = '" + contactInfo.getPhone() + "'";
		
		ContentValues row = new ContentValues();
		
		row.put(COLUMN[2], contactInfo.getName());
		row.put(COLUMN[3], contactInfo.getDesc());
		
		return db.update(TABLE_NAME, row, where, null);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		onCreate(db);
	}
}
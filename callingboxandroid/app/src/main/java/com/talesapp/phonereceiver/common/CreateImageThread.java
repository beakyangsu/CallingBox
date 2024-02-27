package com.talesapp.phonereceiver.common;


import android.content.Context;
import android.os.AsyncTask;

public class CreateImageThread extends AsyncTask<Object, Void, Void>{

	Context mContext;
	
	public CreateImageThread(Context context){
		mContext = context;
	}
	@Override
	protected Void doInBackground(Object... params) {
		Contacts contact = (Contacts) params[1];
		
		ContactTableDB db = new ContactTableDB(mContext);
		Contacts oldContact = db.query(contact.getPhone());
		if(oldContact == null){
			db.insert(contact);
		}else{
			db.update(contact);
		}
		return null;
	}

}

package com.talesapp.phonereceiver.network;

import java.net.URLDecoder;

import org.json.JSONObject;

import com.talesapp.phonereceiver.common.Contacts;
import com.talesapp.phonereceiver.common.FinalValues;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetContactFromServer extends AsyncTask<String, Void, Contacts>{

	private OnGetContactListener mListener;
	final String TAG = "GetContactFrom";
	private final String ENC = "UTF-8";
	private Context mContext;
	
	public GetContactFromServer(Context context){
		mContext = context;
	}
	
	public GetContactFromServer(){}
	
	@Override
	protected Contacts doInBackground(String... params) {
		String phoneNum = params[0];
		Contacts contact = null;
		try {
			System.out.println(Protocol.URL_GET_CONTACT + 
					Protocol.PARAM_GET_CONTACT + phoneNum);
			JSONObject jsonObject = GetJSON.getJSONfromURL(
					Protocol.URL_GET_CONTACT + 
					Protocol.PARAM_GET_CONTACT + phoneNum);
			String name = URLDecoder.decode(jsonObject.getString(FinalValues.JSON_NAME_NAME), ENC);
			String url = URLDecoder.decode(jsonObject.getString(FinalValues.JSON_NAME_URL), ENC);
			String phone = URLDecoder.decode(jsonObject.getString(FinalValues.JSON_NAME_PHONE_NUM), ENC);
			String desc = URLDecoder.decode(jsonObject.getString(FinalValues.JSON_NAME_DESC), ENC);
			String profilePicUrl = URLDecoder.decode(jsonObject.getString(FinalValues.JSON_NAME_PROFILE_PIC_URL), ENC);
			String bgImgUrl = URLDecoder.decode(jsonObject.getString(FinalValues.JSON_NAME_BG_IMG_URL), ENC);
			
			if(phone == null || phone.equals("")) return null;

			contact = new Contacts();
			contact.setName(name);
			contact.setUrl(url);
			contact.setPhone(phone);
			contact.setDesc(desc);
			contact.setProfilePicUrl(profilePicUrl);
			contact.setBgImgUrl(bgImgUrl);
			
			DrawPicture drawPicture = new DrawPicture();
			contact.setProFilePic(drawPicture.getImageFromWeb(mContext.getResources(), profilePicUrl));
			contact.setBgImg(drawPicture.getImageFromWeb(mContext.getResources(), bgImgUrl));
			drawPicture = null;
		} catch (Exception e) {
			Log.e(TAG, "Fail to parse JSON: " + e);
			return null;
		}
		
		return contact;
	}

	@Override
	protected void onPostExecute(Contacts contact) {
		super.onPostExecute(contact);
		if(mListener != null) mListener.onGetContact(contact);
	}
	
	public void setOnGetContactListener(OnGetContactListener listener){
		mListener = listener;
	}
}
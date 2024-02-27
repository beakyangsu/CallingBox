package com.talesapp.phonereceiver;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.talesapp.phonereceiver.network.Protocol;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;

public class UploadContact {
	final String TAG = "UploadContact";
	
	SetInfoAct mSetInfoAct;
	String mPhoneNum;
	String mName;
	String mDesc;
	
	UploadContact(SetInfoAct setInfoAct, String phoneNum, String name, String desc) {
		mSetInfoAct = setInfoAct;
		mPhoneNum = phoneNum;
		mName = name;
		mDesc = desc;
	}
	
	JSONObject init(){
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(Protocol.URL_UPLOAD_CONTACT);
		
		Charset charset = Charset.forName("UTF-8");
		MultipartEntity entity = new MultipartEntity();
		InputStream inputStream = null;
		String responseResult = null;
		JSONObject jsonObject = null;
		
		Bitmap bgImgBitmap = getBitmap(mSetInfoAct.mBgImgV);
		Bitmap profileBitmap = getBitmap(mSetInfoAct.mProfileImgV);
		
		File bgImg = getImageFile(SetInfoActClickListener.PATH_BG_TEMP, bgImgBitmap);
		File profileImg = getImageFile(SetInfoActClickListener.PATH_PROFILE_IMG_TEMP, profileBitmap);
		
		if(!bgImg.exists()){
			bgImg = null;
			bgImg = new File(SetInfoActClickListener.PATH_BG);
		}
		
		if(!profileImg.exists()){
			profileImg = null;
			profileImg = new File(SetInfoActClickListener.PATH_PROFILE_IMG);
		}
		
		try {
			entity.addPart("user_name", new StringBody(mName, charset));
			entity.addPart("user_desc", new StringBody(mDesc, charset));
//			entity.addPart("url", new StringBody("http://www.naver.com", charset));
			entity.addPart("cell_no", new StringBody(mPhoneNum));
			entity.addPart("profileFile", new FileBody(profileImg));
			entity.addPart("bgFile", new FileBody(bgImg));
//			entity.addPart("", new FileBody(file));
			post.setEntity(entity);
			HttpResponse response = client.execute(post);
			HttpEntity httpEntity = response.getEntity();
			inputStream = httpEntity.getContent();
		
		} catch (UnknownHostException e){
			return null;
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "Endoing error: " + e);
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			Log.e(TAG, "" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			
			inputStream.close();
			responseResult = stringBuilder.toString();
			System.out.println("result: "+responseResult);
		} catch (Exception e) {
			Log.e(TAG, "Error converting result " + e.toString());
		}

		try {
			jsonObject = new JSONObject(responseResult);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing data " + e.toString());
		}
		
		bgImg = null;
		profileImg = null;
		
		return jsonObject;
	}
	
	private File getImageFile(String filePath, Bitmap bitmap){
		File file = new File(filePath);
		try {
			file.createNewFile();
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 0, bos);
			byte[] bitmapData = bos.toByteArray();
			
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bitmapData);
			
			bos.close();
			fos.close();
			bos = null;
			fos = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
	
	private Bitmap getBitmap(ImageView view){
		BitmapDrawable bitmapDrawable = (BitmapDrawable)view.getDrawable();
		return (Bitmap)bitmapDrawable.getBitmap();
	}
}

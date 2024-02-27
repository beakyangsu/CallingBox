package com.talesapp.phonereceiver.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class GetJSON {

	public static JSONObject getJSONfromURL(String url) throws Exception{
		final String TAG = "GetJSON";
		
		InputStream inputStream = null;
		String result = null;
		JSONObject jsonObject = null;

		/** http get */
		try {
//			HttpClient httpclient = new DefaultHttpClient();
//			HttpGet httpget = new HttpGet(url);
//			HttpResponse response = httpclient.execute(httpget);
//			HttpEntity httpEntity = response.getEntity();
//			inputStream = httpEntity.getContent();
			URL url_ = new URL(url);
			URLConnection connection = url_.openConnection();
			connection.setConnectTimeout(2000);
			connection.setReadTimeout(3000);
			inputStream = connection.getInputStream();

		} catch (Exception e) {
			throw e;
		}

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;
			
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			
			inputStream.close();
			result = stringBuilder.toString();
			System.out.println("result: "+result);
		} catch (Exception e) {
			Log.e(TAG, "Error converting result " + e.toString());
		}

		try {
			jsonObject = new JSONObject(result);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing data " + e.toString());
		}

		return jsonObject;
	}
}
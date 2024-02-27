package com.talesapp.phonereceiver.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;

public final class DrawPicture {

	public Drawable getImageFromWeb(Resources resources, String url){
		
		InputStream is;
		Drawable picture = null;
//		Bitmap picture = null;
//		URL imgUrl;
		
		try {
			is = (InputStream) new URL(url).getContent();
			picture = Drawable.createFromResourceStream(resources, null, is, null);
			is.close();
			is = null;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Object[] params = new Object[]{is, contact};
//		new CreateImageThread(context).execute(params);
//		CreateImage.write(is, contact);
		return picture;
	}
}

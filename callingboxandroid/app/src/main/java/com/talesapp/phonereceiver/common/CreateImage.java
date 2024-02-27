package com.talesapp.phonereceiver.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class CreateImage {
	private final static String FOLDER_NAME = "/VisualLetteringImg/";
	
	private final static String IMG_TYPE_JPG = ".jpg";
	private final static String IMG_TYPE_BMP = ".bmp";
	private final static String IMG_TYPE_PNG = ".png";
	
	public static String write(InputStream is, String url) {
		File root = Environment.getExternalStorageDirectory();
		File dir = new File(root.getAbsolutePath() + FOLDER_NAME);
		String fileType = null;
		if(url.endsWith(IMG_TYPE_JPG)) fileType = IMG_TYPE_JPG;
		else if(url.endsWith(IMG_TYPE_PNG)) fileType = IMG_TYPE_PNG;
		else if(url.endsWith(IMG_TYPE_BMP)) fileType = IMG_TYPE_BMP;
		else fileType = IMG_TYPE_JPG;	
		String fileName = System.currentTimeMillis() + fileType;
		File file = new File(dir, fileName);
		
		boolean madeDir = false;
		
		if(!dir.exists()){
			madeDir = dir.mkdir();
		}
		
		if(madeDir){
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		byte[] buffer = new byte[1024];
		int length = 0;
		try {
			while((length = is.read(buffer)) > 0){
				try {
					out.write(buffer, 0, length);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return dir.getPath() + fileName;
	}
}

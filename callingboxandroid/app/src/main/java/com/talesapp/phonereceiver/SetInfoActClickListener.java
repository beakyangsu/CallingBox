package com.talesapp.phonereceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.talesapp.phonereceiver.common.Tools;
import com.talesapp.phonereceiver.common.UserInfoStore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class SetInfoActClickListener implements OnClickListener{

	static final String PATH_ROOT = Environment.getExternalStorageDirectory() + "";
	static final String PATH_PROFILE_IMG = PATH_ROOT + "/profile_img.jpg";
	static final String PATH_PROFILE_IMG_TEMP = PATH_ROOT + "/profil_img_temp.jpg";
	static final String PATH_BG = PATH_ROOT + "/bg_img.jpg";
	static final String PATH_BG_TEMP = PATH_ROOT + "/bg_img_temp.jpg";
	static final String[] PATH_LIST_SMALL_IMG = new String[]{PATH_ROOT + "/small_img_01.png", PATH_ROOT + "/small_img_02.png", PATH_ROOT + "/small_img_03.png"};
	
	static final int REQ_CODE_GET_BG = 2;
	static final int REQ_CODE_GET_PROFILE_IMG = 3;
	static final int REQ_CODE_GET_BG_FROM_CAMERA = 4;
	static final int REQ_CODE_GET_PROFILE_IMG_FROM_CAMERA = 5;

	private Context mContext;
	private Activity mActivity;
	private SetInfoAct mSetInfoAct;
	
	private TextView mTargetTxtV;
	
	public SetInfoActClickListener(Context context, SetInfoAct setInfoAct){
		mContext = context;
		mActivity = (Activity)context;
		mSetInfoAct = setInfoAct;
	}
	
	public SetInfoActClickListener(){}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.txtv_desc:
			showKeyboard(v);
			break;
		case R.id.txtv_name:
			showKeyboard(v);
			break;
		case R.id.imgv_bg:
			if(isSDCardMounted())
//				imageGetterDialog(REQ_CODE_GET_BG, REQ_CODE_GET_BG_FROM_CAMERA, PATH_BG_TEMP);
				getImageFromGallery(mActivity, REQ_CODE_GET_BG, PATH_BG_TEMP);
			break;
		case R.id.imgv_profile_img:
			if(isSDCardMounted())
				getImageFromGallery(mActivity, REQ_CODE_GET_PROFILE_IMG, PATH_PROFILE_IMG_TEMP);
//				imageGetterDialog(REQ_CODE_GET_PROFILE_IMG, REQ_CODE_GET_PROFILE_IMG_FROM_CAMERA, PATH_PROFILE_IMG_TEMP);
			break;
		case R.id.btn_ok:
			inputTextOnView();
			break;
		case R.id.btn_save:
			uploadUserInfo();
		}
		v = null;
	}
	
	private void imageGetterDialog(final int reqCodeGallery, final int reqCodeCamera, final String filePath){
    	CharSequence[] imageGetters = {"Gallery", "Camera"};
    	AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
    	builder.setItems(imageGetters, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case 0:
					getImageFromGallery(mActivity, reqCodeGallery, filePath);
					break;
				case 1:
					getImageFromCamera(mActivity, reqCodeCamera, filePath);
					break;
				}
				dialog.dismiss();
			}
		});
    	AlertDialog dialog = builder.create();
    	dialog.show();
    }
	
	private void getImageFromCamera(Activity activity, int requestCode, String path){
		int screenWidth = Tools.getScreenHeight(activity.getWindowManager());
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		
//		intent.setClassName
//		intent.putExtra("aspectX", )
		intent.putExtra("outputX", screenWidth);
		intent.putExtra("outputY", screenWidth);
		intent.putExtra("output", getImageUri(path));
		intent.putExtra("outputFormat", "PNG");
		activity.startActivityForResult(intent, requestCode);
	}
	
	private void uploadUserInfo(){
		String phoneNum = mSetInfoAct.mPhoneNumTxtV.getText().toString();
		String name = mSetInfoAct.mNameTxtV.getText().toString();
		String desc = mSetInfoAct.mDescTxtV.getText().toString();
		Object[] params = new Object[]{mSetInfoAct, phoneNum, name, desc};
		new UploadUserInfoAsyncTask(mContext).execute(params);
	}
	
	private void inputTextOnView(){
		String input = mSetInfoAct.mInputBoxEdtT.getText().toString();
		if(input == null){
			input = "";
		}
		mTargetTxtV.setText(input);
		mSetInfoAct.mInputBoxEdtT.setText("");
		mSetInfoAct.mInputMethodManager.hideSoftInputFromWindow(mSetInfoAct.mInputBoxEdtT.getWindowToken(), 0);
	}
	
	private void showKeyboard(View v){
		mTargetTxtV = (TextView) v;
		mSetInfoAct.mInputBoxEdtT.requestFocus();
		String text = mTargetTxtV.getText().toString();
		if(text == null){
			text = "";
		}
		mSetInfoAct.mInputBoxEdtT.setText(text);
		
		mSetInfoAct.mInputBarLyt.setVisibility(View.VISIBLE);
		mSetInfoAct.mInputMethodManager.showSoftInput(mSetInfoAct.mInputBoxEdtT, 0);
	}
	
	private void getImageFromGallery(Activity activity, int requestCode, String path){
		Intent intent = new Intent(
				Intent.ACTION_GET_CONTENT,
				Media.EXTERNAL_CONTENT_URI);
		
		int screenWidth = Tools.getScreenHeight(activity.getWindowManager());
		
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
//		intent.putExtra("scale", true);
		intent.putExtra("outputX", 720);
		intent.putExtra("outputY", 720);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("scale", true);
//		intent.putExtra("return-data", true);
//		intent.putExtra();
//		intent.putExtra();
		intent.putExtra("output", getImageUri(path));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		
		activity.startActivityForResult(intent, requestCode);
		
		intent = null;
		activity = null;
		requestCode = 0;
		path = null;
	}
	
	private Uri getImageUri(String path) {
		File image = new File(path);
		
		try {
			image.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return Uri.fromFile(image);
    }
	
	private boolean isSDCardMounted(){
    	String state = Environment.getExternalStorageState();
    	if(state.equals(Environment.MEDIA_MOUNTED)){
    		return true;
    	}else{
    		return false;
    	}
    }
	
//	private void createSmallImage(){
//		
//		String imageDataPath = null;
//        String[] projection = {MediaStore.Images.Media.DATA};
//        Cursor imageCursor = mActivity.managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                projection, null, null, null);
//        ArrayList<String> pathList = new ArrayList<String>();
//        
//        if (imageCursor != null && imageCursor.moveToFirst()){
//            if (imageCursor.getCount() > 0){
//                pathList.add(imageCursor.getString(0));
//            }
//        }
//        imageCursor.close();
//        
//        int size = pathList.size();
//        for(int i = 0; i < size; i++){
//        	if(i == 3)return;
//        	createImageFile(PATH_LIST_SMALL_IMG[i], getBitmap(pathList.get(i)));
//        }
//	}
//	
//	private Bitmap getBitmap(String filePath){
//		BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inSampleSize = 10;
//		
//		return BitmapFactory.decodeFile(filePath, options);
//	}
//	
//	private void createImageFile(String filePath, Bitmap bitmap){
//		File file = new File(filePath);
//		try {
//			file.createNewFile();
//			
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			bitmap.compress(CompressFormat.PNG, 0, bos);
//			byte[] bitmapData = bos.toByteArray();
//			
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(bitmapData);
//			
//			bos.close();
//			fos.close();
//			bos = null;
//			fos = null;
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

}

class UploadUserInfoAsyncTask extends AsyncTask<Object, Void, Integer>{

	private Context mContext;
	private Activity mActivity;
	private ProgressDialog mDialog;
	private String mName;
	private String mDesc;
	
	public UploadUserInfoAsyncTask(Context context){
		mContext = context;
		mActivity = (Activity) mContext;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mDialog = ProgressDialog.show(mContext, "","Please wait...");
	}
	
	@Override
	protected Integer doInBackground(Object... params) {
		SetInfoAct setInfoAct = (SetInfoAct) params[0];
		String phoneNum = params[1] + "";
		String name = params[2] + "";
		String desc = params[3] + "";
		
		UploadContact uploadContact = new UploadContact(setInfoAct, phoneNum, name, desc);
		JSONObject jsonObject = uploadContact.init();
		
		int result = -100;
		if(jsonObject != null){
			
			try {
				result = jsonObject.getInt("RESULT");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		if(result == 0){
			mName = name;
			mDesc = desc;
		}else{
			
		}
		phoneNum = null;
		name = null;
		desc = null;
		
		return result;
//		return 0;
	}

	private void saveNewFile(String oldFilePath, String newFilePath){
		File newFile = new File(newFilePath);
		File oldFile = new File(oldFilePath);
		File renamedFilePath = new File(oldFilePath);
		
		if(!newFile.exists()){
			return;
		}
		
		if(oldFile.exists()){	// 두번 이상 저장
			if(oldFile.delete()){
				newFile.renameTo(renamedFilePath);
				File garbage = new File(newFilePath);
				garbage.delete();
			}
		}else{					// 처음 저장
			newFile.renameTo(renamedFilePath);
			File garbage = new File(newFilePath);
			garbage.delete();
		}
		
		oldFilePath = null;
		newFilePath = null;
		newFile = null;
		oldFile = null;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(result == 0){
			UserInfoStore.Name.write(mContext, mName);
			UserInfoStore.Desc.write(mContext, mDesc);
			saveNewFile(SetInfoActClickListener.PATH_BG, SetInfoActClickListener.PATH_BG_TEMP);
			saveNewFile(SetInfoActClickListener.PATH_PROFILE_IMG, SetInfoActClickListener.PATH_PROFILE_IMG_TEMP);
			
//			createSmallIm
		}else if(result == -100){
			Toast.makeText(mContext, "Fail to save your profile!", Toast.LENGTH_SHORT).show();
			Toast.makeText(mContext, "Please, check your internet connection~!", Toast.LENGTH_SHORT).show();
		}
		mActivity.startActivity(new Intent(mContext, SettingPayAct.class));
		mDialog.dismiss();
		mActivity.finish();
	}
}
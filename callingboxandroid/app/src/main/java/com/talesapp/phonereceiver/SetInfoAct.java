package com.talesapp.phonereceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.talesapp.phonereceiver.MyScrollView.OnSizeChangedListener;
import com.talesapp.phonereceiver.common.Contacts;
import com.talesapp.phonereceiver.common.Tools;
import com.talesapp.phonereceiver.common.UserInfoStore;
import com.talesapp.phonereceiver.network.GetContactFromServer;
import com.talesapp.phonereceiver.network.OnGetContactListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetInfoAct extends Activity{

	/////////////////////////////////////
	ProgressDialog mDialog;
	//=====================================
	private View mTabbarAbout;
	MyScrollView mMyScrollView;
	ImageView mBgImgV, mProfileImgV;
	TextView mPhoneNumTxtV, mNameTxtV, mDescTxtV;
	
	LinearLayout mInputBarLyt;
	EditText mInputBoxEdtT;
	Button mOkBtn, mSaveBtn;
	
	InputMethodManager mInputMethodManager;
	
	SetInfoActClickListener mListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_setting_info);
		
		mDialog = ProgressDialog.show(this, "","Please wait...");
		
		deleteTempFile(SetInfoActClickListener.PATH_BG_TEMP);
		deleteTempFile(SetInfoActClickListener.PATH_PROFILE_IMG_TEMP);
		
		setupWidget();
	}
	
	@Override
	protected void onResume() {
		this.overridePendingTransition(0, 0);
		super.onResume();
	}
	
	private void setupWidget(){
		SetInfoActClickListener listener = new SetInfoActClickListener(this, this);
		
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		mTabbarAbout = (View) findViewById(R.id.view_top_tabbar_about);
		mMyScrollView = (MyScrollView) findViewById(R.id.scrllv_parent_layout);
		mBgImgV = (ImageView) findViewById(R.id.imgv_bg);
		mProfileImgV = (ImageView) findViewById(R.id.imgv_profile_img);
		mPhoneNumTxtV = (TextView) findViewById(R.id.txtv_phone_num);
		mNameTxtV = (TextView) findViewById(R.id.txtv_name);
		mDescTxtV = (TextView) findViewById(R.id.txtv_desc);

		mInputBarLyt = (LinearLayout) findViewById(R.id.lyt_input_bar);
		mInputBoxEdtT = (EditText) findViewById(R.id.edt_input_box);
		mOkBtn = (Button) findViewById(R.id.btn_ok);
		mSaveBtn = (Button) findViewById(R.id.btn_save);
		
//		Bitmap bgImage = BitmapFactory.decodeFile(SetInfoActClickListener.PATH_BG);
//		Bitmap profileImage = BitmapFactory.decodeFile(SetInfoActClickListener.PATH_PROFILE_IMG);
		
//		int bgImageWidth = bgImage.getWidth();
//		int profileImageWidth = profileImage.getWidth();
//		int screenWidth = Tools.getScreenWidth(getWindowManager());
		
//		if(bgImageWidth > screenWidth ){
//			Bitmap resizedBgImage = Bitmap.createScaledBitmap(bgImage, screenWidth, screenWidth, true);
//			bgImage.recycle();
//			bgImage = null;
//			bgImage = resizedBgImage;
//			resizedBgImage = null;
//		}
//		
//		if(profileImageWidth > screenWidth){
//			Bitmap resizeProfileImage = Bitmap.createScaledBitmap(profileImage, screenWidth, screenWidth, true);
//			profileImage.recycle();
//			profileImage = null;
//			profileImage = resizeProfileImage;
//			resizeProfileImage = null;
//		}
		
		
//		Drawable bgImage = Drawable.createFromPath(SetInfoActClickListener.PATH_BG);
//		Drawable profileImage = Drawable.createFromPath(SetInfoActClickListener.PATH_PROFILE_IMG);
//		if(bgImage == null){
//			mBgImgV.setImageResource(R.drawable.bg_default);
//		}else{
//			mBgImgV.setImageBitmap(bgImage);
////			mBgImgV.setImage(bgImage);
//			bgImage = null;
//		}
//		
//		if(profileImage == null){
//			mProfileImgV.setImageResource(R.drawable.profile_default);
//		}else{
//			mProfileImgV.setImageBitmap(profileImage);
////			mBgImgV.setImageDrawable(profileImage);
//			profileImage = null;
//		}
		
		String rawPhoneNum = Tools.getThisPhoneNum(this);
		String cookedPhoneNum = getCookedPhoneNum(rawPhoneNum);
		mPhoneNumTxtV.setText(cookedPhoneNum);
		mNameTxtV.setText(UserInfoStore.Name.read(this));
		mDescTxtV.setText(UserInfoStore.Desc.read(this));
		
		mBgImgV.setOnClickListener(listener);
		mProfileImgV.setOnClickListener(listener);
		mNameTxtV.setOnClickListener(listener);
		mDescTxtV.setOnClickListener(listener);
		
		mOkBtn.setOnClickListener(listener);
		mSaveBtn.setOnClickListener(listener);
		
		mTabbarAbout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mMyScrollView.setOnSizeChangedListener(new OnSizeChangedListener() {
			
			@Override
			public void onSizeChanged(int originalHeight, int newHeight) {
				if(originalHeight == mInputBarLyt.getHeight() + newHeight){
					mInputBarLyt.setVisibility(View.GONE);
					mInputBoxEdtT.setText("");
				}
			}
		});
		
		mSaveBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action == MotionEvent.ACTION_UP){
					mSaveBtn.setBackgroundResource(R.drawable.ring_button_d);
				}else{
					mSaveBtn.setBackgroundResource(R.drawable.ring_button_p);
				}
				action = 0;
				return false;
			}
		});
		
		GetContactFromServer getContactFromServer = new GetContactFromServer(this);
		getContactFromServer.setOnGetContactListener(new OnGetContactListener() {
			@Override
			public void onGetContact(Contacts contact) {
				mDialog.dismiss();
				String name;
				String desc;
				if(contact == null){
					Bitmap bgImage = BitmapFactory.decodeFile(SetInfoActClickListener.PATH_BG);
					Bitmap profileImage = BitmapFactory.decodeFile(SetInfoActClickListener.PATH_PROFILE_IMG);
					
					if(bgImage == null){
						mBgImgV.setImageResource(R.drawable.bg_default);
					}else{
						mBgImgV.setImageBitmap(bgImage);
//						mBgImgV.setImage(bgImage);
						bgImage = null;
					}
					
					if(profileImage == null){
						mProfileImgV.setImageResource(R.drawable.profile_default);
					}else{
						mProfileImgV.setImageBitmap(profileImage);
//						mBgImgV.setImageDrawable(profileImage);
						profileImage = null;
					}
					
					bgImage = null;
					profileImage = null;
				}else{
					mNameTxtV.setText(contact.getName());
					mDescTxtV.setText(contact.getDesc());
					mBgImgV.setImageDrawable(contact.getBgImg());
					mProfileImgV.setImageDrawable(contact.getProfilePic());
					
					saveImages();
					UserInfoStore.Name.write(SetInfoAct.this, contact.getName());
					UserInfoStore.Desc.write(SetInfoAct.this, contact.getDesc());
				}
			}
		});
		getContactFromServer.execute(cookedPhoneNum);
		rawPhoneNum = null;
		listener = null;
	}
	
	private void saveImages(){
		Bitmap bgBitmap = getBitmapFromView(mBgImgV);
		Bitmap profileBitmap = getBitmapFromView(mProfileImgV);
		
		createImageFile(SetInfoActClickListener.PATH_BG, bgBitmap);
		createImageFile(SetInfoActClickListener.PATH_PROFILE_IMG, profileBitmap);
		
	}
	
	private void createImageFile(String filePath, Bitmap bitmap){
		File file = new File(filePath);
		try {
			file.createNewFile();
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 80, bos);
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
	}
	
	private Bitmap getBitmapFromView(ImageView view){
		if(view != null){
			BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getDrawable();
			if(bitmapDrawable != null){
				return bitmapDrawable.getBitmap();
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data == null) return;
		if(requestCode == SetInfoActClickListener.REQ_CODE_GET_BG){
			if(resultCode == RESULT_OK){
				recycleImageView(mBgImgV);
				System.gc();
				
				setImageOnView(mBgImgV, SetInfoActClickListener.PATH_BG_TEMP);
//				Bitmap photo = (Bitmap) data.getExtras().get("data");
//				mBgImgV.setImageBitmap(photo);
			}
		}else if(requestCode == SetInfoActClickListener.REQ_CODE_GET_PROFILE_IMG){
			if(resultCode == RESULT_OK){
				recycleImageView(mProfileImgV);
				System.gc();
				setImageOnView(mProfileImgV, SetInfoActClickListener.PATH_PROFILE_IMG_TEMP);
			}
		}else if(requestCode == SetInfoActClickListener.REQ_CODE_GET_BG_FROM_CAMERA){
			if(resultCode == RESULT_OK){
				
			}
		}else if(requestCode == SetInfoActClickListener.REQ_CODE_GET_PROFILE_IMG_FROM_CAMERA){
			if(resultCode == RESULT_OK){
				
			}
		}
	}
    
    private void recycleImageView(ImageView view){
		if(view != null){
			BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getDrawable();
			if(bitmapDrawable != null){
				Bitmap bitmap = bitmapDrawable.getBitmap();
				if(bitmap != null)bitmap.recycle();
				view.setImageBitmap(null);
			}
		}
	}
    
    private void setImageOnView(ImageView imageView, String imagePath){
    	Drawable temporaryImage = Drawable.createFromPath(imagePath);
    	imageView.setImageDrawable(temporaryImage);
    	
    	temporaryImage = null;
    	imageView = null;
    }
    
    private String getCookedPhoneNum(String phoneNum){
		if(phoneNum.contains("+")){
        	String bucket = phoneNum.substring(3);
        	phoneNum = null;
        	phoneNum = "0" + bucket;
        	bucket = null;
        }
		return phoneNum;
	}
    
	private void deleteTempFile(String tempFilePath){
		File file = new File(tempFilePath);
		if(file.exists()){
			file.delete();
		}
		file = null;
	}
    
	@Override
	protected void onDestroy() {
		recycleImageView(mBgImgV);
		recycleImageView(mProfileImgV);
		
		mTabbarAbout = null;
		mMyScrollView = null;
		mBgImgV = null;
		mProfileImgV = null;
		mPhoneNumTxtV = null;
		mNameTxtV = null;
		mDescTxtV = null;
		
		mInputBarLyt = null;
		mInputBoxEdtT = null;
		mOkBtn = null;
		mSaveBtn = null;
		
		mInputMethodManager = null;
		
		mListener = null;
		System.gc();
		super.onDestroy();
	}
}
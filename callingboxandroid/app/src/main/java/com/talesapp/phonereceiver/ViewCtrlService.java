package com.talesapp.phonereceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.talesapp.phonereceiver.common.ContactTableDB;
import com.talesapp.phonereceiver.common.Contacts;
import com.talesapp.phonereceiver.common.Tools;
import com.talesapp.phonereceiver.network.GetContactFromServer;
import com.talesapp.phonereceiver.network.OnGetContactListener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCtrlService extends Service {
	public static boolean sIsRecycled;
	public static boolean sIsDestroyed;

	public final static String ACTION_OUTGOING = "outgoing";
	public final static String ACTION_INCOMING = "incoming";
	public final static String ACTION_DROP_CALL = "drop_call";

	public static TextView sMemoryCheckTxtv;

	private final String NAME_DEFAULT = "Unknown";

	boolean mGotContact;

	private GetContactFromServer mGetContactGetter;

	private FrameLayout mRootLayout;

	private WindowManager mWindowManager;
	private LayoutInflater mLayoutInflater;

	private ImageView mProfilePicImgV, mBgImgImgV;
	private TextView mNameTxtV, mPhoneNumTxtV, mDescTxtV;
	private ImageView mXBtnImgV;

	private Contacts mContacts;

	final String TAG = "VisualL";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sIsDestroyed = false;

		Log.e(TAG, "===================onCreate()");
		setTheme(0);

		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mRootLayout = new FrameLayout(this);
		mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mRootLayout.setVisibility(View.GONE);
		mWindowManager.addView(mRootLayout, getLayoutParams());

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		if (intent != null) {
			String action = intent.getAction();
			if (action.equals(ACTION_OUTGOING)) {
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				Log.e(TAG, "==============ACTION_OUTGOING");
				outgoing(intent);
			} else if (action.equals(ACTION_INCOMING)) {
				// try {
				// Thread.sleep(5000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				Log.e(TAG, "==============ACTION_INCOMING");
				incoming(intent);
			} else if (action.equals(ACTION_DROP_CALL)) {
				Log.e(TAG, "==============ACTION_DROP_CALL");
				hideIcon();
				dropCall();
			}
		}
		return START_NOT_STICKY;
	}

	private void outgoing(Intent intent) {
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		String rawPhoneNum = settings.getString("outgoing_phone", "");
		String phoneNum = getCookedPhoneNum(rawPhoneNum);

		setupOutgoingView();
		
		ContactTableDB db = new ContactTableDB(this);
		Contacts contact = db.query(phoneNum);
		db.close();
		if(contact != null){
			setOldContactOnView(contact);
		}			
		
		runContactGetter(phoneNum);
	}

	private void incoming(Intent intent) {

		String rawPhoneNum = intent
				.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		String phoneNum = getCookedPhoneNum(rawPhoneNum);

		setupIncomingView();

		ContactTableDB db = new ContactTableDB(this);
		Contacts contact = db.query(phoneNum);
		db.close();
		if(contact != null){
			setOldContactOnView(contact);
		}
		
		runContactGetter(phoneNum);
	}

	private void dropCall() {
		if (mGetContactGetter != null) {
			mGetContactGetter.cancel(false);
			mGetContactGetter = null;
		}
		mRootLayout.setVisibility(View.GONE);
		stopSelf();
	}

	private void setupOutgoingView() {

		View parentView = mLayoutInflater.inflate(R.layout.outgoing_call,
				mRootLayout, true);

		mProfilePicImgV = (ImageView) parentView
				.findViewById(R.id.imgv_profile_img);
		mBgImgImgV = (ImageView) parentView.findViewById(R.id.imgv_bg);
		mPhoneNumTxtV = (TextView) parentView.findViewById(R.id.txtv_phone_num);
		mNameTxtV = (TextView) parentView.findViewById(R.id.txtv_name);
		mDescTxtV = (TextView) parentView.findViewById(R.id.txtv_desc);

		mXBtnImgV = (ImageView) parentView.findViewById(R.id.imgv_x_btn);
		// ////////////////// check memory ///////////////////////////
		// sMemoryCheckTxtv = (TextView)
		// parentView.findViewById(R.id.txtv_memory_check);
		// ================= check memory ============================
		Log.e(TAG, "setupOutoingView sMemoryCheckTxtv: " + sMemoryCheckTxtv);
		int width = Tools.getScreenWidth(mWindowManager);
		int height = Tools.getScreenHeight(mWindowManager);
		int layoutSize;
		Log.e(TAG, "width: " + width + " height: " + height);
		if (width <= height) {
			Log.e(TAG, "========width");
			layoutSize = width;
		} else {
			Log.e(TAG, "========height");
			layoutSize = height;
		}

		mBgImgImgV.getLayoutParams().width = layoutSize;
		mBgImgImgV.getLayoutParams().height = layoutSize;

		mXBtnImgV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRootLayout.setVisibility(View.GONE);
				stopSelf();
			}
		});
	}

	private void setupIncomingView() {
		View parentView = mLayoutInflater.inflate(R.layout.incoming_call,
				mRootLayout, true);

		mProfilePicImgV = (ImageView) parentView
				.findViewById(R.id.imgv_profile_img);
		mBgImgImgV = (ImageView) parentView.findViewById(R.id.imgv_bg);
		mPhoneNumTxtV = (TextView) parentView.findViewById(R.id.txtv_phone_num);
		mNameTxtV = (TextView) parentView.findViewById(R.id.txtv_name);
		mDescTxtV = (TextView) parentView.findViewById(R.id.txtv_desc);

		mXBtnImgV = (ImageView) parentView.findViewById(R.id.imgv_x_btn);
		// //////////////////check memory ///////////////////////////
		// sMemoryCheckTxtv = (TextView)
		// parentView.findViewById(R.id.txtv_memory_check);
		// ================= check memory ============================

		int width = Tools.getScreenWidth(mWindowManager);
		int height = Tools.getScreenHeight(mWindowManager);
		int layoutSize;
		Log.e(TAG, "width: " + width + " height: " + height);
		if (width <= height) {
			Log.e(TAG, "========width");
			layoutSize = width;
		} else {
			Log.e(TAG, "========height");
			layoutSize = height;
		}
		mBgImgImgV.getLayoutParams().width = layoutSize;
		mBgImgImgV.getLayoutParams().height = layoutSize;

		mXBtnImgV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRootLayout.setVisibility(View.GONE);
				stopSelf();
			}
		});
	}

	private void hideIcon() {
		Intent i = new Intent(this, IconViewService.class);
		i.setAction(IconViewService.ACTION_STOP);
		startService(i);
	}

	private void runContactGetter(final String phoneNum) {
		mGetContactGetter = new GetContactFromServer(this);
		mGetContactGetter.setOnGetContactListener(new OnGetContactListener() {
			@Override
			public void onGetContact(Contacts contact) {
				if (sIsDestroyed)
					return;
				mGotContact = true;
				if (contact == null) {
					 ContactTableDB db = new ContactTableDB(ViewCtrlService.this);
					 Contacts contactOnly4CheckCache = db.query(phoneNum);
					 db.close();
					 if(contactOnly4CheckCache == null){
						 contact = getCookedContact(contactOnly4CheckCache, phoneNum);
						 setContactOnView(contact);
						 mContacts = null;
						 return;
					 }else{
						 mContacts = null;
						 return;
					 }
				}

				setContactOnView(contact);
				mContacts = contact;
				contact = null;
				// hideIcon();
				// mRootLayout.setVisibility(View.VISIBLE);
			}
		});

		// Handler getContactHandler = new Handler();
		// getContactHandler.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// if(!mGotContact){
		// mGetContactGetter.cancel(true);
		// Contacts rawContact = getContactFromDB(phoneNum);
		// Contacts contact = getCookedContact(rawContact, phoneNum);
		// System.out.println("contact: " + contact.getName());
		// setContactOnView(contact);
		// mRootLayout.setVisibility(View.VISIBLE);
		// }
		// }
		// }, RESPONSE_LATENCY);

		Toast.makeText(this, "Loading contact from server..", Toast.LENGTH_LONG)
				.show();
		mGetContactGetter.execute(phoneNum);
	}

	private void setContactOnView(Contacts contact) {
		recycleImageView(mBgImgImgV);
		recycleImageView(mProfilePicImgV);
		System.gc();
		
		mProfilePicImgV.setImageDrawable(contact.getProfilePic());
		mBgImgImgV.setImageDrawable(contact.getBgImg());

		final String url = contact.getUrl();

		if (url != null) {
			mBgImgImgV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					runBrowser(url);
				}
			});
		}

		mNameTxtV.setText(contact.getName());
		mPhoneNumTxtV.setText(contact.getPhone());
		String desc = contact.getDesc();
		if (desc == null) {
			mDescTxtV.setVisibility(View.GONE);
		} else {
			mDescTxtV.setText(desc);
		}
		mDescTxtV.setText(contact.getDesc());
		contact = null;

		mRootLayout.setVisibility(View.VISIBLE);
		hideIcon();
		// Tools.showAllocatedMemory();
	}
	
	private void setOldContactOnView(Contacts contact){
//		recycleImageView(mBgImgImgV);
//		recycleImageView(mProfilePicImgV);
//		System.gc();
		
		String profileImgPath = SetInfoActClickListener.PATH_ROOT + "/" + contact.getPhone() + "_profile_img.jpg";
		String bgImgPath = SetInfoActClickListener.PATH_ROOT + "/" + contact.getPhone() + "_bg_img.jpg";
		Uri profileImgUri = Uri.parse(profileImgPath);
		Uri bgImgUri = Uri.parse(bgImgPath);
		
		mProfilePicImgV.setImageURI(profileImgUri);
		mBgImgImgV.setImageURI(bgImgUri);
		
		final String url = contact.getUrl();

		if (url != null) {
			mBgImgImgV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					runBrowser(url);
				}
			});
		}

		mNameTxtV.setText(contact.getName());
		mPhoneNumTxtV.setText(contact.getPhone());
		String desc = contact.getDesc();
		if (desc == null) {
			mDescTxtV.setVisibility(View.GONE);
		} else {
			mDescTxtV.setText(desc);
		}
		mDescTxtV.setText(contact.getDesc());

		Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				mRootLayout.setVisibility(View.VISIBLE);
				hideIcon();
			}
		};
		
		handler.postDelayed(runnable, 1000);
		
		contact = null;
		profileImgPath = null;
		bgImgPath = null;
		profileImgUri = null;
		bgImgUri = null;
	}

	private Contacts getCookedContact(Contacts contact, String phoneNum) {
		if (contact == null) {
			contact = new Contacts();

			contact.setBgImg(getResources().getDrawable(R.drawable.bg_default));
			contact.setProFilePic(getResources().getDrawable(
					R.drawable.profile_default));
			contact.setPhone(phoneNum);
			String name = getName(phoneNum);
			if (name == null || name.equals(""))
				contact.setName(NAME_DEFAULT);
			else
				contact.setName(name);
		}
		return contact;
	}

	private WindowManager.LayoutParams getLayoutParams() {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.FILL_PARENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);
		params.gravity = Gravity.TOP;
		return params;
	}

	protected void runBrowser(String url) {
		Uri uri = Uri.parse(url);
		if (uri != null)
			return;
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		dropCall();
	}

	private Contacts getContactFromDB(String phoneNum) {
		ContactTableDB db = new ContactTableDB(this);
		Contacts contact = db.query(phoneNum);
		db.close();
		return contact;
	}

	private String getName(String phoneNum) {
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNum));
		String[] projection = new String[] { PhoneLookup.DISPLAY_NAME };
		Cursor cursor = getContentResolver().query(uri, projection, null, null,
				null);
		String name = null;
		if (cursor.moveToFirst()) {
			name = cursor.getString(0);
		}
		return name;
	}

	private String getCookedPhoneNum(String phoneNum) {
		if (phoneNum.contains("+")) {
			String bucket = phoneNum.substring(3);
			phoneNum = null;
			phoneNum = "0" + bucket;
			bucket = null;
		}
		return phoneNum;
	}

	private void recycleImageView(ImageView view) {
		if (view != null) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) view.getDrawable();
			if (bitmapDrawable != null) {
				Bitmap bitmap = bitmapDrawable.getBitmap();
				if (bitmap != null)
					bitmap.recycle();
				view.setImageBitmap(null);
			}
		}
	}
	
	private void saveNewInfo(Contacts contact){
		ContactTableDB db = new ContactTableDB(this);
		Contacts result = db.query(contact.getPhone());
		
		if(result == null){
			db.insert(contact);
		}else{
			db.update(contact);
		}
		
		db.close();
		
		String profileImgPath = SetInfoActClickListener.PATH_ROOT + "/" + contact.getPhone() + "_profile_img.jpg";
		String bgImgPath = SetInfoActClickListener.PATH_ROOT + "/" + contact.getPhone() + "_bg_img.jpg";
		
		saveImages(profileImgPath, bgImgPath);
	}
	
	private void saveImages(String profileImgPath, String bgImgPath){
		Bitmap bgBitmap = getBitmapFromView(mBgImgImgV);
		Bitmap profileBitmap = getBitmapFromView(mProfilePicImgV);
		
		createImageFile(bgImgPath, bgBitmap);
		createImageFile(profileImgPath, profileBitmap);
		
	}
	
	private void createImageFile(String filePath, Bitmap bitmap){
		File file = new File(filePath);
		try {
			file.createNewFile();
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, bos);
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
	public void onDestroy() {
		if(mContacts != null){
			saveNewInfo(mContacts);
		}
		
		sIsDestroyed = true;
		sIsRecycled = true;
		recycleImageView(mBgImgImgV);
		recycleImageView(mProfilePicImgV);
		mBgImgImgV = null;
		mDescTxtV = null;
		mGetContactGetter = null;
		mLayoutInflater = null;
		mNameTxtV = null;
		mPhoneNumTxtV = null;
		mProfilePicImgV = null;
		mRootLayout = null;
		mWindowManager = null;

		super.onDestroy();
		System.gc();
		Log.e(TAG, "==========onDestroy()");
	}
}

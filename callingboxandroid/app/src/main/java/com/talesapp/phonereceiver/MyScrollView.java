package com.talesapp.phonereceiver;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView{

	private OnSizeChangedListener mOnSizeChangedListener;
	private int mOriginalHeight = -1;
	
	public MyScrollView(Context context) {
		super(context);
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(mOriginalHeight == -1) mOriginalHeight = h;
		
		if(oldh < h){
			if(mOnSizeChangedListener != null){
				mOnSizeChangedListener.onSizeChanged(mOriginalHeight, h);
			}
		}
	}
	
	public void setOnSizeChangedListener(OnSizeChangedListener listener){
		mOnSizeChangedListener = listener;
	}

	public interface OnSizeChangedListener{
		public void onSizeChanged(int originalHeight, int newHeight);
	}
}

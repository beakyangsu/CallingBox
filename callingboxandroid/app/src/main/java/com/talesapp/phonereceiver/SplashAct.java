package com.talesapp.phonereceiver;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashAct extends Activity {
	
	private long mDelay = 1500;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_splash);
        
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent().setClass(SplashAct.this, AboutServiceAct.class);
                startActivity(intent);
                intent = null;
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, mDelay);
        task = null;
        timer = null;
    }
    
	@Override
	protected void onResume() {
		this.overridePendingTransition(0, 0);
		super.onResume();
	}

    @Override
    protected void onDestroy(){
    	mDelay = 0;
    	super.onDestroy();
    }
}
package com.laplasz.pebblecontrol;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class Pebblecontrol  extends Application {
	
	private Pebblecontrol singleton;
	
	@Override
	public void onCreate() {
		Log.d("Blubuk Application","onCreate");
		super.onCreate();
		singleton = this;
		
		 
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public Pebblecontrol getInstance(){
		return singleton;
	}
 
	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}
 
	@Override
	public void onTerminate() {
		Log.d("Blubuk","onTerminate");
		super.onTerminate();
	}

}
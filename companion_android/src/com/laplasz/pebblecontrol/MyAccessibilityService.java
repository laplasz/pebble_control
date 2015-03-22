package com.laplasz.pebblecontrol;


import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService  {

	private static final int
    KEY_BUTTON_EVENT = 0,
    BUTTON_EVENT_UP = 1,
    BUTTON_EVENT_DOWN = 2,
    BUTTON_EVENT_SELECT = 3;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d("service", "crated");
			
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("service", "started");
		Bundle extras = intent.getExtras();
		Integer value = -1;
		if (extras != null) {
		    value = extras.getInt("control");
		}
		
		switch(value) {
            case BUTTON_EVENT_UP:
                //The UP button was pressed
            	 boolean result = performGlobalAction(GLOBAL_ACTION_BACK);
            	 Log.d("make back action result: ", Boolean.toString(result));
                break;
            case BUTTON_EVENT_DOWN:
                //The DOWN button was pressed
            	
                break;
            case BUTTON_EVENT_SELECT:
                //The SELECT button was pressed

                break;
            default:
            	Log.d("keyboard","no such");
            	
            }
		stopSelf();
		return Service.START_STICKY;
		
	}
	
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
		// TODO Auto-generated method stub
		//nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);

	}

	@Override
	public void onInterrupt() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void onDestroy() {
        // Cancel the persistent notification.
		super.onDestroy();
		Log.d("service", "destroyed");
        
    }


}

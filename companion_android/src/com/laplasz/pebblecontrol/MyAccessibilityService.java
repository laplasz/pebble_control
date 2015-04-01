package com.laplasz.pebblecontrol;


import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

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
		AccessibilityNodeInfo node = getRootInActiveWindow();
		
		/*while (node != null && !node.isClickable()) {
			
			Log.d("no clisckable node found in layout ","fasz");
			node = node.focusSearch(View.FOCUS_FORWARD);
			 
		}
		
		if(node != null) {
		  Log.d("actions can be performed result: ", Integer.toString(node.getActions()));
		  Log.d("node clickable: ", Integer.toString(node.getActions())); 
			  
		} else {
			Log.d("no clisckable node found in layout ","that is life"); 
			
		}
		node = getRootInActiveWindow();*/
		Log.d("child count of root: ",Integer.toString(node.getChildCount()));
		
		for(int i = 0; i < node.getChildCount(); i ++ ) {
			Log.d("Next child: "+Integer.toString(i),node.getChild(i).toString());
			for (int j = 0; j < node.getChild(i).getChildCount(); j++) {
				Log.d("Next child: "+Integer.toString(i)+" "+Integer.toString(j),node.getChild(i).getChild(j).toString());
			}
		}
		
		
		switch(value) {
            case BUTTON_EVENT_UP:
                //The UP button was pressed
            	
            	 boolean result = performGlobalAction(GLOBAL_ACTION_BACK);
            	 Log.d("make back action result: ", Boolean.toString(result));
                break;
            case BUTTON_EVENT_DOWN:
                //The DOWN button was pressed
            	if(node.getChild(2) != null && node.getChild(2).isClickable()) {
            		node.getChild(2).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            		Log.d("Click done on ", node.getChild(2).toString());
            	} else {
            		Log.d("Click done on ", "not!");
            	}
                break;
            case BUTTON_EVENT_SELECT:
            	if(node.getChild(0) != null && node.getChild(0).isClickable()) {
            		node.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            		Log.d("Click done on ", node.getChild(0).toString());
            	} else {
            		Log.d("Click done on ", "not!");
            	}
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

package com.laplasz.pebblecontrol;


import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView.FindListener;
import android.widget.Toast;

public class MyAccessibilityService extends AccessibilityService {

	private static final int
    KEY_BUTTON_EVENT = 0,
    BUTTON_EVENT_UP = 1,
    BUTTON_EVENT_DOWN = 2,
    BUTTON_EVENT_SELECT = 3,
	BUTTON_EVENT_BACK = 4;
	
	public static final String PREF_CURRENT_NODE_POS = "pref_current_node_pos";
	public static final String APP_PREF = "app_pref";
	public static final String APP_DB_CONTROL = "app_db_control";
	String cur_level = null;
	boolean foundNode = false;
	boolean foundNext = false;
	AccessibilityNodeInfo root;
	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d("service", "crated");
			
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("service", "started");
		if (intent == null) {
			return Service.START_STICKY;
		}
		Bundle extras = intent.getExtras();
		Integer value = -1;
		if (extras != null) {
		    value = extras.getInt("control");
		}
		root = getRootInActiveWindow();
		String mode = "select";
		if (root == null) {
			Log.d("service","perhaps no permission to get Accesibility info");
			Toast.makeText(this, "No permission", Toast.LENGTH_LONG).show();
			stopSelf();
			return Service.START_STICKY;
		}
		AccessibilityManager am = (AccessibilityManager)getSystemService(ACCESSIBILITY_SERVICE);
		if(am.isTouchExplorationEnabled()) {
			Log.d("service","Accesibility  active");
		} else {
			mode = "control";
			Log.d("service","Accesibility info not active");
		}
		root.getPackageName();
			
		SharedPreferences pref = this.getSharedPreferences(APP_PREF, MODE_PRIVATE);
		cur_level = pref.getString(PREF_CURRENT_NODE_POS, "0_0");
		AccessibilityNodeInfo node;
		switch(value) {
            case BUTTON_EVENT_UP:
            	
            	
        		Editor editor = pref.edit();
        		
        		Log.d("saved node: ",cur_level);
            	//AccessibilityNodeInfo node1 = root.findAccessibilityNodeInfosByViewId("com.laplasz.pebblecontrol:id/button1").get(0);
            	//Log.d("Click done on button", node1.toString());
        		foundNode = false;
        		foundNext = false;
        		nodetraversal(root,"0");
            	if(!foundNext) {
            	  cur_level = "0_0";
            	  //perhaps the end of the travelsal or if !foundNode then other app - both case start again
            	  foundNode = false;
              	  foundNext = false;
            	  nodetraversal(root,"0");
            	  
            	}
            	editor.putString(PREF_CURRENT_NODE_POS, cur_level);
        		editor.commit();
        	    //The UP button was pressed
            	//AccessibilityNodeInfo node1 = NextFocusAbleChild();
            	//
            	//node.getChild(0);
            	//node = node.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY);
            	
                break;
            case BUTTON_EVENT_DOWN:
            	node = getStoredNode();
            	if (node != null) {
            	node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            	Log.d("Down","node:"+node.toString());
            	} else {
            		Log.d("Down","not found");
            	}
                break;
            case BUTTON_EVENT_SELECT:
            	SharedPreferences db_control = this.getSharedPreferences(APP_DB_CONTROL, MODE_PRIVATE);
            	if (mode == "select") {
            	node = getStoredNode();
            	if (node != null) {
            	
            	Editor editor2 = db_control.edit();
            	editor2.putString((String) node.getPackageName(), cur_level);
            	editor2.commit();
            	Log.d("select","node saved: "+cur_level);
            	} else {
            		Log.d("select","not stored node to select");
            	}
            	} else {
            		cur_level = db_control.getString((String )root.getPackageName(), "0_0");
            		node = getStoredNode();
                	if (node != null) {
                	node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                	
                	Log.d("contorl","node pushed "+cur_level);
                	} else {
                		Log.d("select","node in control mode not found");
                	}
            	}
                break;
            case BUTTON_EVENT_BACK:    
            	boolean result = performGlobalAction(GLOBAL_ACTION_BACK);
           	 Log.d("make back action result: ", Boolean.toString(result));
            default:
            	Log.d("keyboard","no such");
            	
            }
		stopSelf();
		return Service.START_STICKY;
		
	}
	
	private AccessibilityNodeInfo getStoredNode() {
		//TOTO check packagename
		AccessibilityNodeInfo node = root;
		String [] pos = TextUtils.split(cur_level, "_");
    	//skip first level
    	for (int i = 1; i < pos.length && node != null; i++ ) {
    		node=node.getChild(Integer.parseInt(pos[i]));
    	}
		return node;
	}
	
	private void nodetraversal(AccessibilityNodeInfo node1, String level) {
       // Log.d("Curr Level:",level);
        String nextNode = "";
        AccessibilityNodeInfo nextNode1;
        
		for(int i = 0; i < node1.getChildCount() && !foundNext; i++ ) {
			nextNode1 = node1.getChild(i);
			nextNode = level+"_"+Integer.toString(i);
			
			if (foundNode && (nextNode1.isClickable() || nextNode1.isScrollable() )) {
				if (!foundNext) {
				  nextNode1.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
				  Log.d("found clickable!", nextNode+nextNode1.toString());
				  foundNext = true;
				  cur_level = nextNode;
				  return;
				} else {
				  Log.d("found clickable again, not returned!", nextNode);
				}
			}
			
			if (cur_level.equals(nextNode)) {
				foundNode = true;
				Log.d("Found last node!", nextNode);
			}
			if(nextNode1.getChildCount() > 0) {
				nodetraversal(nextNode1, nextNode);
			}
		}
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

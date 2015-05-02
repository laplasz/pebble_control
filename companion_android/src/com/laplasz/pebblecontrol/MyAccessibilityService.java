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
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView.FindListener;

public class MyAccessibilityService extends AccessibilityService  {

	private static final int
    KEY_BUTTON_EVENT = 0,
    BUTTON_EVENT_UP = 1,
    BUTTON_EVENT_DOWN = 2,
    BUTTON_EVENT_SELECT = 3,
	BUTTON_EVENT_BACK = 4;
	
	public static final String PREF_CURRENT_NODE_POS = "pref_current_node_pos";
	public static final String APP_PREF = "app_pref";
	
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
		
		/*for (int i = 0; i < node.getChildCount(); i++) {
			Log.d("root child: "+Integer.toString(i), node.getChild(i).toString());
			Log.d("root child childcount: ", Integer.toString(node.getChild(i).getChildCount()));
		}
		
		if(node != null) {
			List<AccessibilityNodeInfo> nodes = node.findAccessibilityNodeInfosByViewId("com.laplasz.pebblecontrol:id/button1");
		Log.d("button1 ciled count", Integer.toString(nodes.get(0).getChildCount()));
		}*/
		
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
		//Log.d("child count of root: "," "+Integer.toString(node.getChildCount()));		
		
		switch(value) {
            case BUTTON_EVENT_UP:
                //The UP button was pressed
            	AccessibilityNodeInfo node1 = NextFocusAbleChild();
            	if(node1 != null) {
            		node1.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS);
            		Log.d("Accesibily done on ", node1.toString());
            	} else {
            		Log.d("Accesibiliy done on ", "not!");
            	}
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
            case BUTTON_EVENT_BACK:    
            	boolean result = performGlobalAction(GLOBAL_ACTION_BACK);
           	 Log.d("make back action result: ", Boolean.toString(result));
            default:
            	Log.d("keyboard","no such");
            	
            }
		stopSelf();
		return Service.START_STICKY;
		
	}
	
	private AccessibilityNodeInfo NextFocusAbleChild() {
		
		SharedPreferences pref = this.getSharedPreferences(APP_PREF, MODE_PRIVATE);
		Editor editor = pref.edit();
		String posString = pref.getString(PREF_CURRENT_NODE_POS, "");
		Log.d("saved string"," "+posString);
		String [] pos = TextUtils.split(posString, ";");
		AccessibilityNodeInfo node = getRootInActiveWindow();
		//node = node.getChild(0);
		//node.recycle();
		//Log.d("root node 0. cihlder 8 child", node.getChild(0).getChild(8).toString());
		//check node is exist
		//pocs
		//FIXME getchild come back with root if overindexed
		for (int i = 0; i < pos.length && node.getChild(Integer.parseInt(pos[i])) != null; i++) {
			
			if(node.getChild(Integer.parseInt(pos[i])) == null) {
				pos = new String[0];
				Log.d("not valid", "pos deleted");
				node = getRootInActiveWindow();
			} else {
				Log.d("valid", node.getChild(Integer.parseInt(pos[i])).toString());
				node = node.getChild(Integer.parseInt(pos[i]));
				
			}
		}
		
		String [] newPos = new String[0];

		
		int childPos = 0;
		
		//get parent node
		//for(int i = 0; i < pos.length - 1; i++ ) {
		//	node = node.getChild(Integer.parseInt(pos[i]));
		//}
		//Log.d("position: ","child: "+Integer.toString(childPos));
		
		//Where to move:
		
		
		if(node != null) {
			Log.d("child count: ", Integer.toString(node.getChildCount()));
		do {
			Log.d("curr Pos: ", " "+TextUtils.join(";",pos));
			Log.d("curr node: ", node.toString());
		  if(pos.length != 0) {
				childPos = Integer.parseInt(pos[pos.length-1]);
				Log.d("childpos",pos[pos.length-1]);
		   } else {
			   Log.d("warning","no child pos");
		   }
		//right
		if(node.getChildCount() != 0) {
			node = node.getChild(0);
			newPos = new String[pos.length + 1];
			for (int i = 0; i < pos.length; i++) {
				newPos[i] = pos[i];
			}
			newPos[pos.length] = "0";
			Log.d("newPos","right");
		} 
		//down
		else if (childPos + 1 < node.getParent().getChildCount() ) {
			node = node.getParent().getChild(childPos + 1);
			newPos = new String[pos.length];
			for (int i = 0; i < pos.length; i++) {
				newPos[i] = pos[i];
			}
			newPos[pos.length - 1] = Integer.toString(childPos + 1);
			Log.d("newPos","down");
		} 
		//back and down
		else {
			boolean found = false;
			int i = 1;
			do  {
				Log.d("step back curr pos in loop: ", TextUtils.join(";",pos));
				Log.d("node: ", node.toString());
				if(pos.length - i > 0) {
					Log.d("cild num at parent", Integer.toString(node.getParent().getParent().getChildCount()));
					Log.d("might next child pos: ",Integer.toString(Integer.parseInt(pos[pos.length - i - 1] + 1)));
				}
				if(pos.length - i > 0 && node.getParent().getParent().getChildCount() > Integer.parseInt(pos[pos.length - i - 1] + 1)) {
					node = node.getParent().getParent().getChild(Integer.parseInt(pos[pos.length - i - 1] + 1));
					Log.d("Found: ", node.toString());
					found = true;
					newPos = new String[pos.length -1];
					for (int j = 0; j < pos.length -1; j++) {
						newPos[j] = pos[j];
					}
					newPos[pos.length - 2] = Integer.toString(Integer.parseInt(pos[pos.length - i - 1] + 1));
				} else if (pos.length - i == 0 ) {
				    node = getRootInActiveWindow();
				    Log.d("Found root", node.toString());
				    found = true;
				    newPos = new String[0];
				} else {
				  i++;
			      node = node.getParent();
			      Log.d("not Found: go to parent", node.toString());
			      newPos = new String[pos.length -1];
					for (int j = 0; j < pos.length -1; j++) {
						newPos[j] = pos[j];
					}
					pos = new String [newPos.length];
					for (int j = 0; j < newPos.length; j++) {
						pos[j] = newPos[j];
					}
				}
				Log.d("newPos in loop: ", " "+TextUtils.join(";",newPos));
			} while (!found);
			Log.d("newPos","back");
		}
		Log.d("newPos: ", " "+TextUtils.join(";",newPos));
		pos = new String [newPos.length];
		for (int i = 0; i < newPos.length; i++) {
			pos[i] = newPos[i];
		}
		Log.d("new node:", node.toString());
		} while (!node.isClickable());
		} else {
			Log.d("null point node  ", "null");
		}
		
		editor.putString(PREF_CURRENT_NODE_POS, TextUtils.join(";",pos));
		editor.commit();
		return node;
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

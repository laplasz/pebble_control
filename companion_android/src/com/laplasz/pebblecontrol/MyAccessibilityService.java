package com.laplasz.pebblecontrol;

import java.util.UUID;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService  {

	private final static UUID PEBBLE_APP_UUID = UUID.fromString("6954047f-48b4-467c-815e-f2fe666b1388");
	private PebbleDataReceiver mReceiver;
	
	private static final int
    KEY_BUTTON_EVENT = 0,
    BUTTON_EVENT_UP = 1,
    BUTTON_EVENT_DOWN = 2,
    BUTTON_EVENT_SELECT = 3;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("service", "started");
		mReceiver = new PebbleDataReceiver(PEBBLE_APP_UUID) {
			 
		    @Override
		    public void receiveData(Context context, int transactionId, PebbleDictionary data) {
		    	//ACK the message
		        PebbleKit.sendAckToPebble(context, transactionId);
		        //Check the key exists
		        if(data.getUnsignedIntegerAsLong(KEY_BUTTON_EVENT) != null) {
		            int button = data.getUnsignedIntegerAsLong(KEY_BUTTON_EVENT).intValue();
		     
		            switch(button) {
		            case BUTTON_EVENT_UP:
		                //The UP button was pressed
		            	 performGlobalAction(GLOBAL_ACTION_BACK);
		                break;
		            case BUTTON_EVENT_DOWN:
		                //The DOWN button was pressed
		            	
		                break;
		            case BUTTON_EVENT_SELECT:
		                //The SELECT button was pressed

		                break;
		            }
		        }
		    }

		};

		PebbleKit.registerReceivedDataHandler(this, mReceiver);		
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
		unregisterReceiver(mReceiver);
        
    }


}

package com.laplasz.pebblecontrol;

import java.util.UUID;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

public class MyBroadCastReceiver extends PebbleDataReceiver {
	

	public final static UUID PEBBLE_APP_UUID = UUID.fromString("6954047f-48b4-467c-815e-f2fe666b1388");
	public static final int
    KEY_BUTTON_EVENT = 0,
    BUTTON_EVENT_UP = 1,
    BUTTON_EVENT_DOWN = 2,
    BUTTON_EVENT_SELECT = 3;
	
	public MyBroadCastReceiver() {
		super(PEBBLE_APP_UUID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void receiveData(Context arg0, int arg1, PebbleDictionary arg2) {
		// TODO Auto-generated method stub
		   Log.d("receiver","receiveData");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
	   if (intent.getAction().equals(Constants.INTENT_APP_RECEIVE)) {
	        final UUID receivedUuid = (UUID) intent.getSerializableExtra(Constants.APP_UUID);
	           
	        // Pebble-enabled apps are expected to be good citizens and only inspect broadcasts containing their UUID
	        if (!PEBBLE_APP_UUID.equals(receivedUuid)) {
	            Log.d("receiver","not my UUID");
	            return;
	        }
	           
	        final int transactionId = intent.getIntExtra(Constants.TRANSACTION_ID, -1);
	        final String jsonData = intent.getStringExtra(Constants.MSG_DATA);
	        if (jsonData == null || jsonData.isEmpty()) {
	        	Log.d("receiver","nData null");
	            return;
	        }
	           
	        try {
	            final PebbleDictionary data = PebbleDictionary.fromJson(jsonData);
	            // do what you need with the data
	            PebbleKit.sendAckToPebble(context, transactionId);
	            
	            if(data.getUnsignedIntegerAsLong(KEY_BUTTON_EVENT) != null) {
	                int button = data.getUnsignedIntegerAsLong(KEY_BUTTON_EVENT).intValue();
	                startService(context, button);
	            }
	            
	        } catch (JSONException e) {
	            Log.d("receiver","failed reived -> dict" + e);
	            return;
	        }
	    } else if(intent.getAction().equals("com.laplasz.pebblecontrol.intent.RECEIVE")) {
	    	int button = intent.getIntExtra("button", -1);
	    	startService(context, button);
	    }
	}
	
	
private void startService(Context context, int button) {
	
	Intent service = new Intent(context, MyAccessibilityService.class);
    
    switch(button) {
    case BUTTON_EVENT_UP:
        //The UP button was pressed
    	Toast.makeText(context, "UP!", Toast.LENGTH_LONG).show();
    	service.putExtra("control", BUTTON_EVENT_UP);
		context.startService(service);
        break;
    case BUTTON_EVENT_DOWN:
        //The DOWN button was pressed
    	Toast.makeText(context, "DOWN!", Toast.LENGTH_LONG).show();
    	service.putExtra("control", BUTTON_EVENT_DOWN);
		context.startService(service);
        break;
    case BUTTON_EVENT_SELECT:
        //The SELECT button was pressed
    	Toast.makeText(context, "SELECT!", Toast.LENGTH_LONG).show();
    	service.putExtra("control", BUTTON_EVENT_SELECT);
		context.startService(service);
        break;
    }
	
}
}

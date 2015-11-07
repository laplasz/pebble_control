package com.laplasz.pebblecontrol;

import java.util.UUID;

import org.json.JSONException;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.getpebble.android.kit.Constants;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

public class MyBroadCastReceiver extends PebbleDataReceiver {
	

	public final static UUID PEBBLE_APP_UUID = UUID.fromString("6954047f-48b4-467c-815e-f2fe666b1388");
	public final static int KEY_BUTTON_EVENT = 0;
	
	public MyBroadCastReceiver() {
		super(PEBBLE_APP_UUID);
	}

	@Override
	public void receiveData(Context arg0, int arg1, PebbleDictionary arg2) {
		   Log.d("receiver","receiveData");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
	   String action = intent.getAction();
	   int control = -1;
		
	   if (action.equals(Constants.INTENT_APP_RECEIVE)) {
		   control = getControlFromPebble(context, intent);
	   } 
	   else if(action.equals("com.laplasz.pebblecontrol.intent.RECEIVE") ||
	    		 action.equals("com.google.android.c2dm.intent.RECEIVE")) {
	    	
	       control = getControlFromBroadCast(context, intent);
	   }
	   
	   if(control != -1) {
		   Intent service = new Intent(context, MyAccessibilityService.class);
           service.putExtra("control", control);
		   context.startService(service);
	   } else {
		   Log.d("receiver","not received useful data"); 
	   }
	}
	
    private int getControlFromPebble(Context context, Intent intent) {
	final UUID receivedUuid = (UUID) intent.getSerializableExtra(Constants.APP_UUID);
    
    // Pebble-enabled apps are expected to be good citizens and only inspect broadcasts containing their UUID
    if (!PEBBLE_APP_UUID.equals(receivedUuid)) {
        Log.d("receiver","not my UUID");
        return -1;
    }
       
    final int transactionId = intent.getIntExtra(Constants.TRANSACTION_ID, -1);
    final String jsonData = intent.getStringExtra(Constants.MSG_DATA);
    if (jsonData == null || jsonData.isEmpty()) {
    	Log.d("receiver","nData null");
        return -1;
    }
       
    try {
        final PebbleDictionary data = PebbleDictionary.fromJson(jsonData);
        // do what you need with the data
        PebbleKit.sendAckToPebble(context, transactionId);
        
        if(data.getUnsignedIntegerAsLong(KEY_BUTTON_EVENT) != null) {
            return data.getUnsignedIntegerAsLong(KEY_BUTTON_EVENT).intValue();
            //sendNotification(context);
            
        }
        
    } catch (JSONException e) {
        Log.d("receiver","failed reived -> dict" + e);
        return -1;
    }
	return -1;
   }

    private int getControlFromBroadCast(Context context, Intent intent) {
	Log.d("receiver","got "+intent.getAction()+" "+intent.getStringExtra("message"));
	try {
	return Integer.parseInt(intent.getStringExtra("message"));
	} catch (NumberFormatException e) {
	return -1;	
	}
	
    }
	

    static void sendNotification(Context context) {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT);

    Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("GCM Message")
            .setContentText("hali")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent);

    NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
}
}

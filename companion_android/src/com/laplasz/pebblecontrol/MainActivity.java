package com.laplasz.pebblecontrol;


import java.io.IOException;
import java.util.UUID;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.laplasz.pebblecontrol.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class MainActivity extends Activity {
	
	private final static UUID PEBBLE_APP_UUID = UUID.fromString("6954047f-48b4-467c-815e-f2fe666b1388");
	private PebbleDataReceiver mReceiver;
	
	private static final int
    KEY_BUTTON_EVENT = 0,
    BUTTON_EVENT_UP = 1,
    BUTTON_EVENT_DOWN = 2,
    BUTTON_EVENT_SELECT = 3;
	
	TextView tAllDevice;
	
	GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = " 496352229871";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Log.d("App", "oncrate");
		boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
		tAllDevice = new TextView(this);
    	tAllDevice = (TextView)findViewById(R.id.text);
    	tAllDevice.setText("Pebble is " + (connected ? "connected" : "not connected"));
		Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));
		PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
		
		Button button = (Button)findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	Log.d("main activity","button pressed");
		    	Intent dialogIntent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
		    	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    	startActivityForResult(dialogIntent, 1);
		    }
		});
		
		button = (Button)findViewById(R.id.button2);
		button.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
		    	getRegId();
		    	Log.d("main activity","button pressed");
		    	Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); 
				sharingIntent.setType("text/plain");
				String shareBody = (String) tAllDevice.getText();
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivityForResult(Intent.createChooser(sharingIntent, "Share via"),2);
		    }
		});
		
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
	                	tAllDevice.setText("UP button pressed!");
	                    break;
	                case BUTTON_EVENT_DOWN:
	                    //The DOWN button was pressed
	                	tAllDevice.setText("Down button pressed!");
	                    break;
	                case BUTTON_EVENT_SELECT:
	                    //The SELECT button was pressed
	                	tAllDevice.setText("Select button pressed!");
	                    break;
	                }
	            }
	        }
	 
	    };
	 
	    PebbleKit.registerReceivedDataHandler(this, mReceiver);

	}
	
	
	public void getRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    //register is depreceted use this: InstanceID.getToken().
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            	tAllDevice.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d("app","onREsume");
	}
	
	@Override
	public void onPause() {
		super.onResume();
		Log.d("app","onPause");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		PebbleKit.closeAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
		unregisterReceiver(mReceiver);
		Log.d("app","onDestroy");
	}

}

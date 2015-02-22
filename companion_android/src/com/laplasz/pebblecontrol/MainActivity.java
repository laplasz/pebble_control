package com.laplasz.pebblecontrol;


import java.util.UUID;

import com.getpebble.android.kit.PebbleKit;
import com.laplasz.pebblecontrol.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private final static UUID PEBBLE_APP_UUID = UUID.fromString("6954047f-48b4-467c-815e-f2fe666b1388");
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		Log.d("App", "oncrate");
		boolean connected = PebbleKit.isWatchConnected(getApplicationContext());
		TextView tAllDevice = new TextView(this);
    	tAllDevice = (TextView)findViewById(R.id.text);
    	tAllDevice.setText("Pebble is " + (connected ? "connected" : "not connected"));
		Log.i(getLocalClassName(), "Pebble is " + (connected ? "connected" : "not connected"));
		PebbleKit.startAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		PebbleKit.closeAppOnPebble(getApplicationContext(), PEBBLE_APP_UUID);
	}

}

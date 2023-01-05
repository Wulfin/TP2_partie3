package com.thexcoders.tp3_partie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button toSendBtn;
    TextView textViewBatteryStatus;
    TextView textViewCallerNumber;
    private final static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1 ;

    MyBroadcastBatteryLow myBroadcastBatteryLow = new MyBroadcastBatteryLow();

    private final IntentFilter filter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toSendBtn = findViewById(R.id.to_send_btn);
        toSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("FAKE_EVENT_INFO");
                sendBroadcast(intent);
            }
        });

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
            }
        }


        filter.addAction(Intent.ACTION_BATTERY_LOW);

        textViewBatteryStatus = findViewById(R.id.text_view_battery_low);

        textViewCallerNumber = findViewById(R.id.text_view_caller_number);



    }

    public class MyBroadcastBatteryLow extends MyReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            textViewBatteryStatus.setText("Evenement Batterie faible reçu");
        }
    }

    private class MyBroadcastCallReceiver extends MyReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE)) {
            if (intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {
                String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    textViewCallerNumber.setText("Incoming call from " + incomingNumber);
                    Toast.makeText(context, "Incoming call from " + incomingNumber, Toast.LENGTH_SHORT).show();
                }
            }
//            } else {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//            }

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myBroadcastBatteryLow, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myBroadcastBatteryLow);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[]
                                                   grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }
}
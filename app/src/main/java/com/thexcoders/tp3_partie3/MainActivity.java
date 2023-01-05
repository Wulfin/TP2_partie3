package com.thexcoders.tp3_partie3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

        toSendBtn = findViewById(R.id.to_send_btn);
        textViewBatteryStatus = findViewById(R.id.text_view_battery_low);
        textViewCallerNumber = findViewById(R.id.text_view_caller_number);

        toSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("FAKE_EVENT_INFO");
                sendBroadcast(intent);
            }
        });
        // Vérifie si la permission est accordée
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Demande la permission si elle n'est pas accordée
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
        filter.addAction(Intent.ACTION_BATTERY_LOW);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // Si la demande est annulée, le tableau de résultats est vide
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // La permission est accordée, enregistrement du broadcast receiver
                    IntentFilter filter = new IntentFilter();
                    filter.addAction("android.intent.action.PHONE_STATE");
                    registerReceiver(new MyBroadcastCallReceiver(), filter);
                } else {
                    // La permission est refusée, affiche un message d'erreur
                    Toast.makeText(getApplicationContext(), "Permission refused", Toast.LENGTH_SHORT).show();
                }
                return;
            }
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


    public class MyBroadcastBatteryLow extends MyReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            textViewBatteryStatus.setText("Evenement Batterie faible reçu");
        }
    }

    private class MyBroadcastCallReceiver extends MyReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String incomingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            textViewCallerNumber.setText("Appel entrant de : " + incomingNumber);
        }
    }
}
package com.niccher.spget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;

public class MainActivity extends AppCompatActivity  {

    Button strt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strt = findViewById(R.id.StartBtn);

        strt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WiFi();
            }
        });

    }

    private void WiFi(){
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            Toast.makeText(this, "Wifi Present", Toast.LENGTH_LONG).show();
        }else {
            NetworkInfo[] networkInfo = connManager.getAllNetworkInfo();

            for (NetworkInfo netInfo : networkInfo) {
                //if (netInfo.getTypeName().equalsIgnoreCase("WIFI")){
                    //if (netInfo.isConnected()) {txtview.setText("Connected to WiFi")}
                // };
                if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
                    if (netInfo.isConnected())
                        Toast.makeText(this, "Connected to Mobile Data", Toast.LENGTH_LONG).show();
                }
            }
        }
        //WiFiStats();
    }

    private void WiFiStats(){
        ConnectivityManager connectivitymanager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfo = connectivitymanager.getAllNetworkInfo();

        for (NetworkInfo netInfo : networkInfo) {

            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))

                //if (netInfo.isConnected()) txtview.setText("Connected to WiFi");

            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))

                if (netInfo.isConnected())

                    //txtview.setText("Connected to Mobile Data");
                    Toast.makeText(this, "Connected to Mobile Data", Toast.LENGTH_LONG).show();
        }

    }

}

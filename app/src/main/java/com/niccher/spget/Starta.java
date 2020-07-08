package com.niccher.spget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.niccher.spget.usables.Konstants;

public class Starta extends AppCompatActivity  {

    Konstants kon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starta);

        kon = new Konstants();

        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intt = new Intent(Starta.this,MainActivity.class);
                startActivity(intt);
                finish();
            }
        }, kon.Splash_Time);

    }

    public void InitMain(){
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, Starta.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

    }

}

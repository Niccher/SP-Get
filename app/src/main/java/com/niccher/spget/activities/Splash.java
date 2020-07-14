package com.niccher.spget.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.niccher.spget.R;
import com.niccher.spget.usables.Konstants;

import gr.net.maroulis.library.EasySplashScreen;

public class Splash extends AppCompatActivity {

    Konstants kon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        kon = new Konstants();

        try {
            InitMain();
        }catch (Exception es){
            Log.e(kon.TAGGED, "Hiding Icon failed as \n"+es.getMessage());
        }

        try {
            LoadSplash();
        }catch (Exception es){
            Log.e(kon.TAGGED, "Loading Splash failed as \n"+es.getMessage());
        }

    }

    public void InitMain(){
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, Splash.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    public void LoadSplash(){
        View easySplashScreenView = new EasySplashScreen(Splash.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(kon.Splash_Time)
                .withBackgroundResource(android.R.color.background_light)
                .withHeaderText("SP-Get")
                .withFooterText("Copyright 2020")
                .withBeforeLogoText("SP-Get Client for all your basic needs")
                .withLogo(R.drawable.ic_logo)
                .withAfterLogoText("Some more details")
                .create();

        setContentView(easySplashScreenView);
    }
}

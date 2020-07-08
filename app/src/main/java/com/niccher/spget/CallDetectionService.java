package com.niccher.spget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class CallDetectionService extends Service {

    private CallDetect callDetector;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callDetector = new CallDetect(this);
        int r = super.onStartCommand(intent, flags, startId);
        callDetector.start();
        return r;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callDetector.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

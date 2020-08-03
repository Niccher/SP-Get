package com.niccher.spget.activities;

import android.content.Context;
import android.util.Log;

import com.niccher.spget.usables.Konstants;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import net.gotev.uploadservice.MultipartUploadRequest;

public class Upload {
    public static final String upurl = "https://chegecache.000webhostapp.com/Ha/Some.php";
    String stamp, PdfID,flogs;
    Calendar cal=new GregorianCalendar();
    Konstants kon;
    Context cnt;

    private void Postage(String targt){
        kon = new Konstants();

        try {
            PdfID = UUID.randomUUID().toString();
            Thread.sleep(150);
            try {
                new MultipartUploadRequest(cnt, PdfID, upurl)
                        .addFileToUpload(targt, "pdf")
                        .addParameter("name", stamp.trim())
                        .setMaxRetries(10)
                        .addParameter("","")
                        .startUpload();

                Thread.sleep(150);
                Log.e(kon.TAGGED,"<MultipartUploadRequest Uploaded Succcesfully --------");
            } catch (Exception ex) {
                Log.e(kon.TAGGED,"<MultipartUploadRequest errored as -------->\n" +ex.getMessage());
            }
        } catch (Exception exception) {
            Log.e(kon.TAGGED,"<Postage --------"+exception.getMessage());
        }
    }

}

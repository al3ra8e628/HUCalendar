package com.example.al3ra8e.hucalendar.connection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.webkit.MimeTypeMap;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UploadMaterial {

    private Intent data  ;
    private int eventId ;
    private AppCompatActivity activity ;
    private String materialTitle  ;
    public UploadMaterial() {
    }

    public UploadMaterial(Intent data, int eventId , AppCompatActivity activity) {
        this.data = data;
        this.eventId = eventId;
        this.activity = activity ;
    }

    public UploadMaterial setMaterialTitle(String materialTitle) {
        this.materialTitle = materialTitle;
        return this;
    }

    public UploadMaterial setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this ;
    }

    public UploadMaterial setData(Intent data) {
        this.data = data;
        return this ;
    }

    public UploadMaterial setEventId(int eventId) {
        this.eventId = eventId;
        return this ;
    }



    public void uploadFile(){
        final ProgressDialog progress;
        progress = new ProgressDialog(activity);
        progress.setTitle("Uploading");
        progress.setMessage("Please wait...");
        progress.show();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                File f  = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                String content_type  = getMimeType(f.getPath());

                String file_path = f.getAbsolutePath();
                OkHttpClient client = new OkHttpClient();
                RequestBody file_body = RequestBody.create(MediaType.parse(content_type),f);

                String fileName = file_path.substring(file_path.lastIndexOf("/")+1).replace(" " , "").replace("%" ,"") ;
                fileName = eventId+fileName ;
                RequestBody request_body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("type",content_type)
                        .addFormDataPart("event_id"  , eventId+"")
                        .addFormDataPart("material_title" ,materialTitle)
                        .addFormDataPart("uploaded_file", fileName, file_body)
                        .build();

                Request request = new Request.Builder()
                        .url(AccessLinks.UPLOAD_MATERIAL+"?event_id="+eventId)
                        .post(request_body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    if(!response.isSuccessful()){
                        throw new IOException("Error : "+response);
                    }

                    progress.dismiss();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        t.start();
    }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }

}

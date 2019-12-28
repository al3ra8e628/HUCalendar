package com.example.al3ra8e.hucalendar.connection;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ImageUploader {

    Bitmap imageToUpload ;
    AppCompatActivity context ;
    Uri path ;
    HashMap<String , String> params ;

    public ImageUploader() {
    }

    public ImageUploader(Bitmap imageToUpload, AppCompatActivity context, Uri path, HashMap<String, String> params) {
        this.imageToUpload = imageToUpload;
        this.context = context;
        this.path = path;
        this.params = params;
    }

    public Bitmap getImageToUpload() {
        return imageToUpload;
    }

    public ImageUploader setContext(AppCompatActivity context) {
        this.context = context;
        return this ;
    }

    public ImageUploader setPath(Uri path) {
        this.path = path;
        return this ;
    }

    public ImageUploader setParams(HashMap<String, String> params) {
        this.params = params;
        return this ;
    }


    public ImageUploader uploadImage(){
        try {

            imageToUpload = MediaStore.Images.Media.getBitmap(context.getContentResolver() , path) ;
            ByteArrayOutputStream bas = new ByteArrayOutputStream() ;
            imageToUpload.compress(Bitmap.CompressFormat.JPEG , 25 , bas) ;
            byte [] img = bas.toByteArray() ;
            String sImg = Base64.encodeToString(img , Base64.DEFAULT);
            params.put("image" ,sImg) ;

            new RequestBuilder()
                    .setUrl(AccessLinks.UPLOAD_IMAGE)
                    .setMethod(Request.Method.POST)
                    .setActivity(context)
                    .setParams(params)
                    .onStringResponse(new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    })
                    .onError(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    })
                    .executeStringRequest();

        } catch (IOException e) {
            e.printStackTrace();
        }

    return this ;
    }


}

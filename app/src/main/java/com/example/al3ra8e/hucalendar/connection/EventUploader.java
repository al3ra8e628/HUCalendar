package com.example.al3ra8e.hucalendar.connection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.al3ra8e.hucalendar.creatorsPackage.AddEventActivity;
import com.example.al3ra8e.hucalendar.other.DateFormat;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class EventUploader {

    AddEventActivity activity ;
    String eventDate , eventTime  , eventTitle  ;
    int categoryId , facultyId , creatorId ;
    Intent pictureIntent ;

    public EventUploader() {
        activity = null ;
    }

    public EventUploader setActivity(AddEventActivity activity) {
        this.activity = activity;
        return this ;
    }

    public EventUploader initial(){
        eventDate     =  activity.eventDate.getText().toString() ;
        eventTime     =  activity.eventTime.getText().toString() ;
        eventTitle    =  activity.eventTitle.getText().toString() ;
        categoryId    =  activity.categories.getSelectedItemPosition()+1 ;
        facultyId     =  activity.faculties.getSelectedItemPosition()+1 ;
        creatorId     =  activity.creatorId ;
        pictureIntent =  activity.pictureIntent ;
        return this ;
    }

    private Bitmap uploadImage(int eventId){
        Uri path = pictureIntent.getData();
        String fileName = new File(path.toString()).getName().replace(" " , "").replace("%" ,"") ;
        HashMap<String , String> params = new HashMap<>();
        params.put("file_name" , fileName) ;
        params.put("state" , "2") ;
        params.put("event_id" , eventId+"") ;

        return new ImageUploader().setContext(activity)
                .setParams(params).setPath(path)
                .uploadImage().getImageToUpload();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean isReady(){

        if(pictureIntent == null) {
            Toast.makeText(activity.getApplicationContext(), " event cover must be selected ", Toast.LENGTH_LONG).show();
            return false;
        }else if(creatorId == 0 || eventDate.equals("Event Date") || eventTime.equals("Event Time") || eventTitle.equals("")) {
            Toast.makeText(activity.getApplicationContext(), "all event info must be entered", Toast.LENGTH_LONG).show();
            return false;
        }else if(!DateFormat.checkOnDate(eventDate , eventTime)){
            Toast.makeText(activity.getApplicationContext() , " bad timing " , Toast.LENGTH_LONG).show();
            return false ;
        }
        return true ;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void insertEventData(){
        if(isReady()){
            HashMap<String , String> params = new HashMap<>();
            params.put("eventTime"  , eventTime+"")  ;
            params.put("eventTitle" , eventTitle+"") ;
            params.put("categoryId" , categoryId+"") ;
            params.put("facultyId"  , facultyId+"")  ;
            params.put("eventDate"  , eventDate+"")  ;
            params.put("creatorId"  , creatorId+"")  ;

            String url = new UrlBuilder(AccessLinks.ADD_EVENT).getUrl() ;

            new RequestBuilder()
                    .setUrl(url)
                    .setActivity(activity)
                    .setMethod(Request.Method.POST)
                    .setParams(params)
                    .onStringResponse(new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response) ;
                                uploadImage(res.getInt("event_id")) ;
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .onError(new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    })
                    .executeStringRequest();
                activity.finish();
        }
    }

}

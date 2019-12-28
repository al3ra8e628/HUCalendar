package com.example.al3ra8e.hucalendar.other;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

public class Permissions {

    private AppCompatActivity context ;

    public Permissions(AppCompatActivity context){
        this.context = context ;
    }


    public   boolean hasPermission(){
        int res = 0 ;
        String [] params = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} ;
        for(String pp : params){
            res = context.checkCallingOrSelfPermission(pp) ;
            if(res != PackageManager.PERMISSION_GRANTED){
                return  false ;
            }
        }
        return true ;
    }
    public void requestPermission(){
        String [] params = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            context.requestPermissions(params , 1234);
        }
    }





}

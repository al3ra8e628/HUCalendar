package com.example.al3ra8e.hucalendar.other;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.servicesPackage.CommentNotificationService;
import com.example.al3ra8e.hucalendar.servicesPackage.EventNotificationService;
import com.example.al3ra8e.hucalendar.servicesPackage.EventRequestNotificationService;

/**
 * Created by al3ra8e on 10/20/2017.
 */

public class Logout {


public static void logOut(final AppCompatActivity activity){

            new AlertDialog.Builder(activity)
                    .setCancelable(false)
                    .setTitle("log out")
                    .setMessage("are you sure !")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SharedPreferences sp = activity.getSharedPreferences("LOGGED_IN" , activity.MODE_PRIVATE) ;
                            SharedPreferences.Editor spe = sp.edit() ;
                            spe.remove("loggedIn") ; spe.remove("person_id") ; spe.remove(Keys.LOG_IN_STATE_KEY) ;
                            spe.commit() ;

                            CommentNotificationService.ServiceIsRun = false ;
                            EventNotificationService.ServiceIsRun = false ;
                            EventRequestNotificationService.ServiceIsRun = false ;

                            Intent intent = new Intent(activity , Home.class) ;
                            activity.startActivity(intent);

                            activity.finish();

                        }
                    })
                    .setNegativeButton("no" , null)
                    .show() ;

}


}

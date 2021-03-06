package com.example.al3ra8e.hucalendar.servicesPackage;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.AppController;
import com.example.al3ra8e.hucalendar.eventPackage.Comment;
import com.example.al3ra8e.hucalendar.eventPackage.EventActivity;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;

public class CommentNotificationBuilder {
    private  final String NOTIFICATION_TAG = "" ; //"New Comment :";
    NotificationCompat.Builder builder ;
    CommentNotificationBuilder currnet = this ;



    public  void notify(final Context context, final Comment comment) {

        final Resources res = context.getResources();
        final String title = ""+comment.getStudent().getFullName()+" reply your commented " ;  //res.getString(R.string.new_message_notification_title_template, comment.getComment());
        final String text =   comment.getComment() ;

                 builder = new NotificationCompat.Builder(context)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(text).setBigContentTitle(title))
                        .setAutoCancel(true)
                        .setContentIntent(getPendingIntent(comment , context))

                        .setSmallIcon(R.drawable.search_icon)
                        .setContentTitle(title)
                        .setContentText(text)
                        ;



        final String url = new UrlBuilder(AccessLinks.PHOTOS_DIRECTORY).setUrlPath(comment.getStudent().getImage()).getUrl() ;

               AppController.getInstance().getImageLoader().get(url, new ImageLoader.ImageListener() {
                   @Override
                   public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap largeIcon = response.getBitmap() ;
                       //largeIcon = Bitmap.createScaledBitmap(largeIcon, 120, 120, false) ;
                    builder.setLargeIcon(largeIcon) ;
                    currnet.notify(comment.getCommentId() ,context ,  builder.build());
                   }
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       error.printStackTrace();
                   }
               }) ;

            }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private  void notify(int notificationId , final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            nm.notify(NOTIFICATION_TAG, notificationId, notification);
        } else {
            nm.notify(notificationId, notification);
        }
    }


    private PendingIntent getPendingIntent(Comment comment , Context context){
        Intent intent = new Intent(context , EventActivity.class) ;

        intent.putExtra("person_id" , comment.getStudent().getPersonId()+"");
        intent.putExtra(Keys.LOG_IN_STATE_KEY, Keys.STUDENT_LOG_IN+"");
        intent.putExtra("eventId" , comment.getEventId()+"") ;

        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT) ;
    }




}

package com.example.al3ra8e.hucalendar.connection;

import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;


/**
 * Created by al3ra8e on 6/9/2017.
 */

public class ImageParser {

    AppController app ;
    String url ;

    public String getUrl() {
        return url;
    }

    public ImageParser setUrl(String url) {
        this.url = url;
        return this ;
    }

    public ImageParser(AppController app) {
        this.app = app;
    }

    public ImageParser(AppController app, String url) {
        this.app = app;
        this.url = url;
    }

    public void setImage(final ImageView imageView){
        app.getImageLoader().get(url,
                new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        imageView.setImageBitmap(response.getBitmap());
                    }
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) ;
    }




    public void setImage(final ConstraintLayout constraintLayout){
        app.getImageLoader().get(url,
                new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        constraintLayout.setBackgroundDrawable(new BitmapDrawable(app.getResources() , response.getBitmap()));
                    }
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) ;
    }


    //picasso image library @_@

/*                             Picasso.with(getApplicationContext())
                                     .load(AccessLinks.PHOTOSDIRECTORY + response.getString("student_image"))
//                                     .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                                     .error(R.drawable.defalt_image)
                                     .resize(200, 200).centerCrop().into(studentPicture);
*/



}

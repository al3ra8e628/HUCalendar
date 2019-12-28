package com.example.al3ra8e.hucalendar.studentPackage;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.al3ra8e.hucalendar.R;
import com.example.al3ra8e.hucalendar.connection.AccessLinks;
import com.example.al3ra8e.hucalendar.connection.AppController;
import com.example.al3ra8e.hucalendar.connection.ImageParser;
import com.example.al3ra8e.hucalendar.connection.ImageUploader;
import com.example.al3ra8e.hucalendar.connection.RequestBuilder;
import com.example.al3ra8e.hucalendar.eventPackage.CustomEvent;
import com.example.al3ra8e.hucalendar.eventPackage.RecycleEventAdapter;
import com.example.al3ra8e.hucalendar.other.Keys;
import com.example.al3ra8e.hucalendar.other.Logout;
import com.example.al3ra8e.hucalendar.other.Permissions;
import com.example.al3ra8e.hucalendar.other.UrlBuilder;
import com.example.al3ra8e.hucalendar.searchPackage.SearchActivity;
import com.example.al3ra8e.hucalendar.servicesPackage.CommentNotificationService;
import com.example.al3ra8e.hucalendar.servicesPackage.EventNotificationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentActivity extends AppCompatActivity  implements View.OnClickListener {
    int studentId , personId , logInState;
    private final int PICTURE_REQUEST_CODE = 1 , COVER_REQUEST_CODE = 2 ;
    ImageButton updateCoverButton , changePic ;
    String coverPicName , fileName ;
    RecyclerView eventList ;
    ArrayList<CustomEvent> events;
    TextView studentName ;
    CircleImageView studentPicture ;
    ConstraintLayout coverLayout ;
    SwipeRefreshLayout swipeRefreshLayout ;
    FloatingActionButton goToSearch ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        getSupportActionBar().hide();
        initial();
        setStudentInfo();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
             @Override
             public void onRefresh() {
                 getAttendedEvents();
                 swipeRefreshLayout.setRefreshing(false);
              }
         });


    }

    private void setNoteServiceOn(int lastComment , int lastEvent){
        CommentNotificationService.CommentID = lastComment ;
        CommentNotificationService.student_id = studentId ;
        CommentNotificationService.person_id = personId ;

        EventNotificationService.lastAcceptedEvent = lastEvent ;
        EventNotificationService.studentID = studentId ;
        EventNotificationService.personID = personId ;

        if(EventNotificationService.ServiceIsRun == false) {
            EventNotificationService.ServiceIsRun  = true;

            Intent intent = new Intent(this, EventNotificationService.class);

            startService(intent);

        }
       if(CommentNotificationService.ServiceIsRun==false) {
            CommentNotificationService.ServiceIsRun  = true;

            Intent intent = new Intent(this, CommentNotificationService.class);
            startService(intent);

        }
    }
    public void setStudentInfo(){
        final AppController app  = AppController.getInstance() ;

        String url = new UrlBuilder(AccessLinks.STUDENT_INFO_LINK).setUrlParameter("person_id" , personId+"").getUrl() ;

        new RequestBuilder()
                .setUrl(url)
                .setMethod(Request.Method.GET)
                .setActivity(this)
                .onResponse(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getString("first_name")+" "+response.getString("last_name");

                            studentId = response.getInt("std_id") ;

                            studentName.setText(name);
                            coverPicName = response.getString("cover_image") ;

                            ImageParser imageParser = new ImageParser(app) ;

                            String profilePicUrl = new UrlBuilder(AccessLinks.PHOTOS_DIRECTORY)
                                    .setUrlPath(response.getString("student_image"))
                                    .getUrl() ;
                            imageParser.setUrl(profilePicUrl).setImage(studentPicture);

                            String coverPicUrl = new UrlBuilder(AccessLinks.PHOTOS_DIRECTORY)
                                    .setUrlPath(response.getString("cover_image"))
                                    .getUrl() ;
                            imageParser.setUrl(coverPicUrl).setImage(coverLayout);
                            getAttendedEvents();
                            setNoteServiceOn(response.getInt("last_comment") , response.getInt("last_event"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onError(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                .execute();
    }

    public void getAttendedEvents(){
         events = new ArrayList<>() ;
         eventList.removeAllViews();
         String url = new UrlBuilder(AccessLinks.ATTENDED_EVENTS).setUrlParameter("id" , studentId+"").getUrl() ;
         JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        new RecycleEventAdapter()
                                .setPersonId(personId)
                                .setLogInState(logInState)
                                .setContext(getApplicationContext())
                                .setEventList(eventList)
                                .setEvents(events).setResponse(response)
                                .fillEventsIntoTheList();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        AppController.getInstance().addToRequestQueue(request);
    }

    public void changePicture(int resultId) {
        Permissions permissions = new Permissions(this) ;


        if(permissions.hasPermission()){
            Intent i = new Intent() ;
             i.setType("image/*") ;
             i.setAction(Intent.ACTION_GET_CONTENT);
             startActivityForResult(i , resultId);
         }else{
             permissions.requestPermission();
         }




    }



    private void uploadImage(int state, Intent data){
       Uri path = data.getData();
       fileName = new File(path.toString()).getName().replace(" " , "").replace("%" ,"") ;

       HashMap<String , String> params = new HashMap<>();

       params.put("std_id" , studentId+"") ;
       params.put("file_name" , fileName) ;
       params.put("state" , state+"") ;

       Bitmap imageToUpload = new ImageUploader().setContext(this)
                                    .setParams(params).setPath(path)
                                    .uploadImage().getImageToUpload();

       if(state == 0){
           studentPicture.setImageBitmap(imageToUpload);
       }else if (state == 1){
           coverLayout.setBackgroundDrawable(new BitmapDrawable(getResources() , imageToUpload));
       }

   }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK && data!= null){
            if(requestCode == PICTURE_REQUEST_CODE){
                uploadImage(0 , data);
           }

        else if(requestCode == COVER_REQUEST_CODE){
                uploadImage(1 , data);
        }
        }
}

    // request for permission start in this line @_@
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          boolean allows = true ;
          switch (requestCode){
              case 1234 :
                for (int res : grantResults){
                   allows = allows && (res == PackageManager.PERMISSION_GRANTED) ;
                }
                  break;
              default:
                  allows = false ;
                  break;
          }
          if(allows){
               changePicture(PICTURE_REQUEST_CODE);
          }
    }
    // request for permission end in this line @_@

    private void initial(){
        personId = Integer.parseInt(getIntent().getStringExtra("person_id")) ;
        logInState = Integer.parseInt(getIntent().getStringExtra(Keys.LOG_IN_STATE_KEY)) ;
        events =  new ArrayList<>() ;
        eventList = (RecyclerView) findViewById(R.id.smallEventsList);
        studentName = (TextView) findViewById(R.id.studentName);
        studentPicture = (CircleImageView) findViewById(R.id.studentProfilePicture);
        coverLayout = (ConstraintLayout) findViewById(R.id.coverLayout);
        updateCoverButton = (ImageButton) findViewById(R.id.updateCoverButton);
        changePic = (ImageButton) findViewById(R.id.fab);
        goToSearch = (FloatingActionButton) findViewById(R.id.goToSearchActivityButton);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.studentRefreshLayout);
        updateCoverButton.setOnClickListener(this);
        changePic.setOnClickListener(this);
        goToSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.updateCoverButton :
                changePicture(COVER_REQUEST_CODE);
                break;
            case R.id.fab :
                changePicture(PICTURE_REQUEST_CODE);
                break;
            case R.id.goToSearchActivityButton :
                Intent i = new Intent(StudentActivity.this ,SearchActivity.class) ;
                i.putExtra("person_id" , personId+"");
                i.putExtra(Keys.LOG_IN_STATE_KEY, logInState+"");
                startActivity(i);
                break;
        }
    }

    public void logout(View view) {

        Logout.logOut(this);


    }
}
